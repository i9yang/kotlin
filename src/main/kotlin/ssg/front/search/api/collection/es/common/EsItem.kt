package ssg.front.search.api.collection.es.common

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.base.Joiner
import com.google.common.collect.HashMultimap
import org.elasticsearch.action.search.SearchResponse
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.es.EsCollection
import ssg.front.search.api.collection.es.EsQuery
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Item
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CamelCaseToUppperCaseWithUnderscores
import ssg.front.search.api.util.CollectionUtils
import java.util.StringTokenizer
import kotlin.collections.ArrayList
import kotlin.collections.component1
import kotlin.collections.component2


open class EsItem : EsCollection(), Sortable, Pageable {
    override fun sortVo(parameter: Parameter): SortVo {
        var sorts: Sorts? = Sorts.BEST
        var strSort: String = parameter.sort!!
        var sortList: ArrayList<Sort?> = ArrayList()
        try{
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch(e: IllegalArgumentException){}

        if (sorts == Sorts.BEST) {
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else if (sorts == Sorts.SALE) {
            sortList.add(Sorts.SALE.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else if (sorts == Sorts.PRCASC || sorts == Sorts.PRCDSC) {
            sortList.add(sorts.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
        } else if (sorts == Sorts.CNT) {
            sortList.add(sorts.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else if (sorts == Sorts.REGDT) {
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else if (sorts == Sorts.SCR) {
            sortList.add(sorts.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else if (sorts == Sorts.CTGORDR) {
            sortList.add(sorts.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        }

        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var page = parameter.page
        var count = parameter.count
        if(count == "" || count == "0"){
            count = "40"
        }

        return CollectionUtils.getPageInfo(page, count)
    }

    override fun getQuery(parameter: Parameter): List<String> {
        val queryMap = HashMultimap.create<String, String>()

        // field
        for (query in listOf(
                EsQuery.SRCH_PREFIX_DISP
                , EsQuery.DISP_CTG_LST
                , EsQuery.DISP_CTG_SUB_LST
                , EsQuery.BRAND_ID
                , EsQuery.SALESTR_LST
                , EsQuery.SIZE
                , EsQuery.COLOR
                , EsQuery.DEVICE_CD
                , EsQuery.MBR_CO_TYPE
                , EsQuery.SCOM_EXPSR_YN
                , EsQuery.PRC_FILTER
                , EsQuery.SPL_VEN_ID
                , EsQuery.GRP_ADDR_ID
                , EsQuery.SHPPCST_ID
                , EsQuery.ITEM_SITE_NO
        )) {
            if (query.getValue(parameter).isNotEmpty()) {
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
        var (itemList, srchItemIds) = getItemResultPair(searchResponse, parameter)

        result.itemList = itemList
        result.itemCount = searchResponse.hits.totalHits.toInt()
        result.srchItemIds = srchItemIds
    }

    fun getItemResultPair(searchResponse: SearchResponse, parameter: Parameter): Pair<List<Item>, String> {
        var searchItemList = mutableListOf<Item>()
        var srchItemIds = StringBuilder()

        searchResponse.hits.hits.forEach { hit ->
            var mapper = jacksonObjectMapper()
                    .setPropertyNamingStrategy(CamelCaseToUppperCaseWithUnderscores())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            val item = mapper.readValue<Item>(hit.sourceAsString)
            searchItemList.add(item)
        }

        for (item in searchItemList) {
            val userInfo = parameter.userInfo
            val strSiteNo = item.siteNo!!
            val itemRegDivCd = item.itemRegDivCd!!
            var salestrLst = item.salestrLst!!
            var strSalestrNo = ""

            if (strSiteNo == "6009" && itemRegDivCd == "30") {
                if (salestrLst.indexOf("0001") > -1) {
                    var idx = 0
                    salestrLst = salestrLst.replace("0001,", "").trim({ it <= ' ' })
                    salestrLst = salestrLst.replace("0001", "").trim({ it <= ' ' })
                    val st = StringTokenizer(salestrLst, " ")
                    while (st.hasMoreTokens()) {
                        if (idx > 1) break
                        strSalestrNo = st.nextToken().replace("D", "")
                        idx++
                    }
                    item.sellSalestrCnt = 1
                } else {
                    strSalestrNo = salestrLst.replace("\\p{Space}".toRegex(), "").replace("D", "").replace("Y", "")
                    item.sellSalestrCnt = 1
                }
            } else if (strSiteNo == "6001" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.emSaleStrNo
                item.sellSalestrCnt = 1
            } else if (strSiteNo == "6002" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.trSaleStrNo
                item.sellSalestrCnt = 1
            } else if (strSiteNo == "6003" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.bnSaleStrNo
                item.sellSalestrCnt = 1
            } else {
                strSalestrNo = "6005"
                item.sellSalestrCnt = 1
            }// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.

            item.salestrNo = strSalestrNo

            val ids = StringBuilder().append(strSiteNo).append(":").append(item.itemId).append(":").append(strSalestrNo).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                srchItemIds.append(ids)
            }

        }

        return Pair(searchItemList, srchItemIds.toString())
    }
}