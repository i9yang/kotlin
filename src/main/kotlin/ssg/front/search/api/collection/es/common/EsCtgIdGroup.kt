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

open class EsCtgIdGroup : EsCollection(), Groupable {
    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("DISP_CTG_CLS_INFO")
    }

    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        for (query in listOf(
                EsQuery.SRCH_PREFIX_DISP,
                EsQuery.SALESTR_LST,
                EsQuery.DEVICE_CD,
                EsQuery.MBR_CO_TYPE,
                EsQuery.SPL_VEN_ID,
                EsQuery.GRP_ADDR_ID,
                EsQuery.SHPPCST_ID,
                EsQuery.ITEM_SITE_NO,
                EsQuery.SCOM_EXPSR_YN
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
        var categoryList = getCategoryList(searchResponse)

        if (parameter.userInfo.deviceDivCd == "20") {
            result.categoryList = ResultUtils.getNewBrandCategoryGroup(parameter.siteNo, parameter.dispCtgId, Joiner.on("@").join(categoryList))
        } else {
            result.categoryList = ResultUtils.getBrandCategoryGroup(parameter.siteNo, parameter.dispCtgId, Joiner.on("@").join(categoryList))
        }
    }

    fun getCategoryList(searchResponse: SearchResponse): List<String> {
        var categoryList = mutableListOf<String>()
        for (bucket in searchResponse.aggregations.get<ParsedStringTerms>("DISP_CTG_CLS_INFO").buckets) {
            categoryList.add("${bucket.key}^${bucket.docCount}")
        }

        return categoryList
    }
}