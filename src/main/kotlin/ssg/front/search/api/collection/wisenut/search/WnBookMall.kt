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

class WnBookMall: WnCollection(), Prefixable, Groupable {

    override fun getName(parameter: Parameter): String {
        return "book"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "mall"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "SITE_NO")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "ISBN", "BOOK_ENG_NM", "ORTITL_NM", "SUBTITL_NM", "AUTHOR_NM", "TRLTPE_NM", "PUBSCO_NM")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.MBR_CO_TYPE,
                Prefixes.DEVICE_CD,
                Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb.toString(), 1)
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("SITE_NO,SCOM_EXPSR_YN")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val strSiteInfo = search.w3GetMultiGroupByResult(name, "SITE_NO") ?: ""
        val strScomInfo = search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN") ?: ""
        if (strSiteInfo != "" && strScomInfo != "") {
            result.mallCountMap = ResultUtils.getSiteGroup(strSiteInfo, strScomInfo)
        }
        return result
    }

}
