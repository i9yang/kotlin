package ssg.front.search.api.collection.es.bshop

import com.google.common.collect.HashMultimap
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.collection.es.common.EsItem
import ssg.front.search.api.dto.Parameter

class EsBshopItem : EsItem() {
    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        for (query in listOf(
                EsQuery.SRCH_PREFIX_BSHOP
                , EsQuery.DISP_CTG_LST
                , EsQuery.DISP_CTG_SUB_LST
                , EsQuery.SALESTR_LST
                , EsQuery.DEVICE_CD
                , EsQuery.MBR_CO_TYPE
                , EsQuery.BSHOP_ID
        )) {
            if (query.getValue(parameter).isNotEmpty()) {
                queryMap.put(query.getField(parameter), query.getValue(parameter))
            }
        }

        val strShpp = parameter.shpp ?: ""
        val strPickuSalestr = parameter.pickuSalestr ?: ""
        if (strShpp.indexOf("picku") > -1) {
            if (strPickuSalestr != "") {
                queryMap.put("SHPP", "PICKU$strPickuSalestr")
            } else {
                queryMap.put("SHPP", "PICKU")
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