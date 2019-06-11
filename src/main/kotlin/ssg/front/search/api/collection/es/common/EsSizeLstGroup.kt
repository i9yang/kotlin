package ssg.front.search.api.collection.es.common

import com.google.common.collect.HashMultimap
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import ssg.front.search.api.collection.es.EsCollection
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.function.Groupable
import ssg.search.result.Size

class EsSizeLstGroup : EsCollection(), Groupable {
    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("SIZE_LST")
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
                EsQuery.SCOM_EXPSR_YN
        )) {
            if (query.getValue(parameter).isNotEmpty()) {
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
        val sizeList = mutableListOf<Size>()
        for (bucket in searchResponse.aggregations.get<ParsedStringTerms>("SIZE_LST").buckets) {

            val size = Size()
            size.sizeCd = bucket.key.toString()
            size.sizeCount = bucket.docCount.toString()
            sizeList.add(size)
        }

        result.sizeList = sizeList
    }
}
