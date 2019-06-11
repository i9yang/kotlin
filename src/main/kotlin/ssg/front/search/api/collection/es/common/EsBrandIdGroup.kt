package ssg.front.search.api.collection.es.common

import com.google.common.base.Joiner
import com.google.common.collect.HashMultimap
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import ssg.front.search.api.collection.es.EsCollection
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.util.ResultUtils


open class EsBrandIdGroup : EsCollection(), Groupable {
    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("BRAND_ID|BRAND_NM.raw")
    }

    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        for (query in listOf(
                EsQuery.SRCH_PREFIX_DISP_FIX_SITE_NO,
                EsQuery.DISP_CTG_LST,
                EsQuery.DISP_CTG_SUB_LST,
                EsQuery.SALESTR_LST,
                EsQuery.DEVICE_CD,
                EsQuery.MBR_CO_TYPE,
                EsQuery.SCOM_EXPSR_YN,
                EsQuery.SPL_VEN_ID,
                EsQuery.GRP_ADDR_ID,
                EsQuery.SHPPCST_ID,
                EsQuery.ITEM_SITE_NO
        )) {
            if (!query.getValue(parameter).isNullOrEmpty()) {
                queryMap.put(query.getField(parameter), query.getValue(parameter))
            }
        }

        val queryList = mutableListOf<String>()
        for ((key, value) in queryMap.entries()) {
            if (!value.isNullOrEmpty()) {
                queryList.add("$key:($value)")
            }
        }

        return queryList
    }

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        val brandInfoList = mutableListOf<String>()
        for (bucket in searchResponse.aggregations.get<ParsedStringTerms>("BRAND_ID").buckets) {
            val brandId = bucket.key.toString()
            val brandNm = bucket.aggregations.get<ParsedStringTerms>("BRAND_NM.raw").buckets[0].key.toString()

            if (brandNm != "") {
                val itemCount = bucket.docCount.toString()
                brandInfoList.add("$brandId^$brandNm^$itemCount")
            }
        }
        var brandList = ResultUtils.getBrandGroup(Joiner.on("@").join(brandInfoList))
        result.brandList = brandList
    }
}
