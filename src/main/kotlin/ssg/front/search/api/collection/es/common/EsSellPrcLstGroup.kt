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
import ssg.search.result.Prc


class EsSellPrcLstGroup : EsCollection(), Groupable {
    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("SELLPRC_LST")
    }

    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()
        for (query in listOf(
                EsQuery.SRCH_PREFIX_DISP,
                EsQuery.DISP_CTG_LST,
                EsQuery.DISP_CTG_SUB_LST,
                EsQuery.BRAND_ID,
                EsQuery.SALESTR_LST,
                EsQuery.SIZE,
                EsQuery.DEVICE_CD,
                EsQuery.MBR_CO_TYPE,
                EsQuery.SCOM_EXPSR_YN
        )) {
            if (!query.getValue(parameter).isNullOrEmpty()) {
                queryMap.put(query.getField(parameter), query.getValue(parameter))
            }
        }

        // cls
        val strCls = parameter.cls ?: ""
        val clsList = mutableListOf<String>()

        if (strCls.indexOf("emart") > -1) {
            clsList.add("EM")
        }

        if (strCls.indexOf("department") > -1) {
            clsList.add("SD")
        }

        if (clsList.size > 0) {
            queryMap.put("CLS", Joiner.on(" OR ").join(clsList))
        }

        // benefit
        val strBenefit = parameter.benefit ?: ""
        val benefitList = ArrayList<String>()

        if (strBenefit != "") {
            for (benefit in strBenefit.split("\\|")) {
                benefitList.add(benefit.toUpperCase())
            }
            queryMap.put("FILTER", Joiner.on(" OR ").join(benefitList))
        }

        // shpp
        val shppList = mutableListOf<String>()

        val strShpp = parameter.shpp ?: ""
        val strPickuSalestr = parameter.pickuSalestr ?: ""
        if (strShpp.indexOf("picku") > -1) {
            if (strPickuSalestr != "") {
                queryMap.put("SHPP", "PICKU$strPickuSalestr")
            } else {
                queryMap.put("SHPP", "PICKU")
            }
        }

        if (strShpp.indexOf("qshpp") > -1) {
            queryMap.put("SHPP", "QSHPP")
        }
        if (strShpp.indexOf("con") > -1) {
            shppList.add("CON")
        }
        if (strShpp.indexOf("ssgem") > -1) {
            shppList.add("SSGEM")
        }
        if (strShpp.indexOf("deliem") > -1) {
            shppList.add("DELIEM")
        }
        if (strShpp.indexOf("ssgtr") > -1) {
            shppList.add("SSGTR")
        }

        if (shppList.size > 0) {
            queryMap.put("SHPP", Joiner.on(" OR ").join(shppList))
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
        var min = 0
        var max = 0

        var sellprcLst = ArrayList<String>()
        var prcGroupList = listOf<Prc>()

        for (bucket in searchResponse.aggregations.get<ParsedStringTerms>("SELLPRC_LST").buckets) {
            sellprcLst.add(bucket.key.toString() + "^^" + bucket.docCount.toString())
        }

        if (!sellprcLst.isEmpty()) {
            prcGroupList = ResultUtils.getSellprcGroupping(Joiner.on("@").join(sellprcLst))

            if (prcGroupList.isNotEmpty()) {
                max = prcGroupList[prcGroupList.size - 1].maxPrc
            }
        }

        result.minPrc = min
        result.maxPrc = max
        result.prcGroupList = prcGroupList
    }
}
