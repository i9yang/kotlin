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


open class EsMallCount : EsCollection(), Groupable {
    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        for (query in listOf(
                EsQuery.SRCH_PREFIX_MALL,
                EsQuery.DISP_CTG_LST,
                EsQuery.DISP_CTG_SUB_LST,
                EsQuery.BRAND_ID,
                EsQuery.SALESTR_LST_MALL,
                EsQuery.SIZE,
                EsQuery.COLOR,
                EsQuery.DEVICE_CD,
                EsQuery.MBR_CO_TYPE,
                EsQuery.PRC_FILTER,
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

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("SITE_NO,SCOM_EXPSR_YN")
    }

    fun getMallCountMap(searchResponse: SearchResponse): HashMap<String, String> {
        val mallCountMap = HashMap<String, String>()
        for (bucket in searchResponse.aggregations.get<ParsedStringTerms>("SITE_NO").buckets) {
            mallCountMap[bucket.key.toString()] = bucket.docCount.toString()
        }

        for (bucket in searchResponse.aggregations.get<ParsedStringTerms>("SCOM_EXPSR_YN").buckets) {
            if (bucket.key.toString() == "Y") {
                mallCountMap["6005"] = bucket.docCount.toString()
            } else {
                mallCountMap[bucket.key.toString()] = bucket.docCount.toString()
            }
        }

        return mallCountMap
    }

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        result.mallCountMap = getMallCountMap(searchResponse)
    }
}
