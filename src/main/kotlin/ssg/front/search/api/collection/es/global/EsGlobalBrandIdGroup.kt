package ssg.front.search.api.collection.es.global

import com.google.common.collect.HashMultimap
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.collection.es.common.EsBrandIdGroup
import ssg.front.search.api.dto.Parameter

class EsGlobalBrandIdGroup : EsBrandIdGroup() {
    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        for (query in listOf(
                EsQuery.SRCH_PREFIX_GLOBAL
                , EsQuery.DISP_CTG_LST
                , EsQuery.DISP_CTG_SUB_LST
                , EsQuery.SALESTR_LST
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
}