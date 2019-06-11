package ssg.front.search.api.collection.es.bundle

import com.google.common.collect.HashMultimap
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.collection.es.common.EsCtgIdGroup
import ssg.front.search.api.dto.Parameter

open class EsBundleCtgIdGroup : EsCtgIdGroup() {
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
}