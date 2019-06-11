package ssg.front.search.api.collection.wisenut.quality

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.search.WnItem
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Item
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Prefixable
import java.text.SimpleDateFormat

class WnSearchQuality : WnItem(), Prefixable {
    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "srch_qual_item"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "SRCH_INSP_STAT_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return when {
            "itemId" == parameter.prefixFilter -> arrayOf("ITEM_ID")
            "mdlNm" == parameter.prefixFilter -> arrayOf("MDL_NM")
            "brandNm" == parameter.prefixFilter -> arrayOf("BRAND_NM")
            "itemNm" == parameter.prefixFilter -> arrayOf("ITEM_NM")
            else -> arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "BRAND_NM", "TAG_NM", "GNRL_STD_DISP_CTG")
        }
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX, Prefixes.FILTER_SITE_NO, Prefixes.SRCH_CTG_ITEM_PREFIX
        ).forEach {
            sb += it.getPrefix(parameter)
        }

        sb += getSalestrLstPrefix(parameter)       //SALESTR_LST
        sb += getSrchInspStatCdPrefix(parameter)  //SRCH_INSP_STAT_CD
        sb += getScomExpsrYnPrefix(parameter)       //SCOM_EXPSR_YN
        sb += getClsPrefix(parameter)                //CLS
        sb += getModIdFilter(parameter)            //FILTER

        return PrefixVo(sb, 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val count = search.w3GetResultCount(name)
        val totalCount = search.w3GetResultTotalCount(name)
        val itemList = arrayListOf<Item>()
        val srchItemIds = StringBuilder()
        var item: Item
        for (i in 0 until count) {
            item = Item()
            val strItemId = search.w3GetField(name, "ITEM_ID", i)
            val strSrchInspStatCd = search.w3GetField(name, "SRCH_INSP_STAT_CD", i)

            item.itemId = strItemId
            item.srchInspStatCd = strSrchInspStatCd

            itemList.add(item)
        }

        val listSet = HashSet<Item>(itemList)
        val processedList = ArrayList<Item>(listSet)

        for (sItem in processedList) {
            val ids = StringBuilder().append(sItem.itemId).append(":").append(sItem.srchInspStatCd).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                srchItemIds.append(ids)
            }
        }

        result.itemList = processedList
        result.itemCount = totalCount
        result.srchItemIds = srchItemIds.toString()

        return result
    }

    private fun getScomExpsrYnPrefix(parameter: Parameter): String {
        val prefix = "<SCOM_EXPSR_YN:contains:"

        val strSiteNo = parameter.siteNo ?: "6005"

        return if (strSiteNo == "6005") {
            prefix + "Y" + ">"
        } else ""

    }

    private fun getClsPrefix(parameter: Parameter): String {
        val prefix = "<CLS:contains:"

        return if (parameter.cls.isNullOrEmpty()) {
            ""
        } else prefix + parameter.cls + ">"

    }

    private fun getSalestrLstPrefix(parameter: Parameter): String {
        val userInfo = parameter.userInfo

        val prefix = "<SALESTR_LST:contains:"

        val emSalestrNo = userInfo.emSaleStrNo ?: ""
        val emRsvtShppPsblYn = userInfo.emRsvtShppPsblYn ?: "N"
        val trSalestrNo = userInfo.trSaleStrNo ?: ""
        val trRsvtShppPsblYn = userInfo.trRsvtShppPsblYn ?: "N"
        val bnSalestrNo = userInfo.bnSaleStrNo ?: ""
        val bnRsvtShppPsblYn = userInfo.bnRsvtShppPsblYn ?: "N"
        val strSiteNo = parameter.siteNo ?: "6005"
        val deptSalestrNo = parameter.salestrNo ?: "0001"
        val bojungSalestrNo = "2439"
        val gimpoSalestrNo = "2449"
        val trGusungSalestrNo = "2152"
        val hwSalestrNo = "2468"


        if (strSiteNo == "6005") {
            return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + bojungSalestrNo + "Y" + "|" + gimpoSalestrNo + "Y" + "|" + hwSalestrNo + "N" + ">"
        } else if (strSiteNo == "6004") {
            return prefix + "6005|" + deptSalestrNo + ">"
        } else if (strSiteNo == "6009") {
            return "$prefix$deptSalestrNo>"
        } else if (strSiteNo == "6001" || strSiteNo == "6002" || strSiteNo == "6003") {
            return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + "Y" + "|" + trSalestrNo + "N" + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + bojungSalestrNo + "Y" + "|" + gimpoSalestrNo + "Y" + "|" + trGusungSalestrNo + "Y" + "|" + trGusungSalestrNo + "N" + ">"
        } else if (strSiteNo == "6100") {
            return prefix + hwSalestrNo + "N" + ">"
        } else if (strSiteNo == "6200" || strSiteNo == "6300") {
            return prefix + "6005" + ">"
        }
        return ""
    }

    private fun getSrchInspStatCdPrefix(parameter: Parameter): String {
        return if (parameter.srchInspStatCd.isNullOrEmpty()) {
            ""
        } else "<SRCH_INSP_STAT_CD:contains:" + parameter.srchInspStatCd + ">"
    }

    private fun checkDate(date: String): Boolean {
        val dt = date.replace("-".toRegex(), "")
        val sdf = SimpleDateFormat("yyyyMMdd")
        sdf.setLenient(false)

        try {
            sdf.parse(dt)
        } catch (e: Exception) {
            return false
        }

        return true
    }


    private fun getModIdFilter(parameter: Parameter): String {
        if (parameter.srchModId.isNullOrEmpty()) {
            return ""
        }
        val fsb = StringBuilder()
        return fsb.append("<FILTER:contains:").append("DK").append(parameter.srchModId).append(">").toString()
    }
}
