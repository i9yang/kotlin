package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.ResultUtils

class WnMall : WnCollection(), Prefixable, Groupable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "mall"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "SITE_NO")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "BRAND_NM", "TAG_NM", "GNRL_STD_DISP_CTG")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        // Mall Collection 은 SSG 기준으로만 호출한다.
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_MALL,
                Prefixes.SALESTR_LST_MALL,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SRCH_PSBL_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb,1)
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("SITE_NO,SCOM_EXPSR_YN")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val strSiteInfo = search.w3GetMultiGroupByResult(name, "SITE_NO") ?: ""
        val strScomInfo = search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN") ?: ""
        if (strSiteInfo != "" && strScomInfo != "") {
            result.mallCountMap = ResultUtils.getSiteGroup(strSiteInfo, strScomInfo)
            try {
                result.noResultItemCount = Integer.parseInt(result.mallCountMap.get("6005"))
            } catch (e: NumberFormatException) {
                result.noResultItemCount = 0
            }
        }
        return result
    }
}
