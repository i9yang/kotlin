package ssg.front.search.api.collection.es.brand

import com.google.common.collect.HashMultimap
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.collection.es.common.EsCtgIdGroup
import ssg.front.search.api.dto.Parameter

open class EsBrandCtgIdGroup : EsCtgIdGroup() {
    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        for (query in listOf(
                EsQuery.SRCH_PREFIX_DISP,
                EsQuery.BRAND_ID,
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
}