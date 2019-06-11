package ssg.front.search.api.collection.es.global

import com.google.common.base.Joiner
import com.google.common.collect.HashMultimap
import org.elasticsearch.action.search.SearchResponse
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.collection.es.common.EsCtgIdGroup
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.util.ResultUtils

class EsGlobalCtgIdGroup : EsCtgIdGroup() {
    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        for (query in listOf(
                EsQuery.SRCH_PREFIX_GLOBAL
                , EsQuery.SALESTR_LST
                , EsQuery.BRAND_ID
                , EsQuery.DEVICE_CD
                , EsQuery.MBR_CO_TYPE
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
        var categoryList = super.getCategoryList(searchResponse)

        result.categoryList = ResultUtils.getGlobalCategoryGroup(parameter.siteNo, parameter.dispCtgId, Joiner.on("@").join(categoryList), "global_brand")
    }
}