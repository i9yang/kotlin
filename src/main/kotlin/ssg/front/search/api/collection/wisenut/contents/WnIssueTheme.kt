package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.IssueTheme

class WnIssueTheme : WnCollection(), Prefixable, Pageable {

    override fun getName(parameter: Parameter): String {
        return "issuetheme"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "issuetheme"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("CURA_ID", "CURA_SRCH_TYPE_CD", "SITE_NO", "TGT_SRCHWD_NM", "CRITN_SRCHWD_NM", "RPLC_KEYWD_NM", "ITEM_ID_LST", "DISP_ORDR", "DISP_STRT_DTS", "DISP_END_DTS", "APL_TGT_MEDIA_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("CRITN_SRCHWD_NM")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val sb = StringBuilder()
        val iter = listOf(
                Prefixes.SITE_NO_USE_FILTER
        ).iterator()
        while (iter.hasNext()) {
            sb.append(iter.next().getPrefix(parameter))
        }
        val aplTgtMediaCd = parameter.aplTgtMediaCd
        if (aplTgtMediaCd != null) {
            sb.append("<APL_TGT_MEDIA_CD:contains:00|").append(aplTgtMediaCd).append(">")
        }
        return PrefixVo(sb.toString(), 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        val strTarget = (parameter.target ?: "").toLowerCase()
        if (strCount == "" || strCount == "0") {
            strCount = "40"
        }
        if (strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "40"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val issueThemeList = arrayListOf<IssueTheme>()
        var issueTheme: IssueTheme
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            issueTheme = IssueTheme()
            issueTheme.setCuraId(search.w3GetField(name, "CURA_ID", i))
            issueTheme.setCuraSrchTypeCd(search.w3GetField(name, "CURA_SRCH_TYPE_CD", i))
            issueTheme.setSiteNo(search.w3GetField(name, "SITE_NO", i))
            issueTheme.setTgtSrchwdNm(search.w3GetField(name, "TGT_SRCHWD_NM", i))
            issueTheme.setCritnSrchwdNm(search.w3GetField(name, "CRITN_SRCHWD_NM", i))
            issueTheme.setRplcKeywdNm(search.w3GetField(name, "RPLC_KEYWD_NM", i))
            issueTheme.setItemIdLst(search.w3GetField(name, "ITEM_ID_LST", i))
            issueTheme.setDispOrdr(search.w3GetField(name, "DISP_ORDR", i))
            issueTheme.setDispStrtDts(search.w3GetField(name, "DISP_STRT_DTS", i))
            issueTheme.setDispEndDts(search.w3GetField(name, "DISP_END_DTS", i))
            issueThemeList.add(issueTheme)
        }
        result.issueThemeList = issueThemeList
        result.issueThemeCount = search.w3GetResultTotalCount(name)
        return result
    }

}
