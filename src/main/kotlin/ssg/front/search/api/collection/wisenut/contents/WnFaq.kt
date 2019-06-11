package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Highlightable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Faq

class WnFaq : WnCollection(), Prefixable, Highlightable, Pageable {

    override fun getName(parameter: Parameter): String {
        return "faq"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "faq"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("POSTNG_ID", "POSTNG_TITLE_NM", "POSTNG_CNTT", "POSTNG_BRWS_CNT")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        //고객센터에서 쓰기 때문에 분기 처리
        val strTarget = parameter.target
        return if (strTarget.isNullOrEmpty() && strTarget == "faq") {
            arrayOf("POSTNG_TITLE_NM", "POSTNG_CNTT")
        } else {
            arrayOf("POSTNG_TITLE_NM")
        }
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var userInfo = parameter.userInfo
        var strPrefix = Prefixes.DVIC_DIV_CD.getPrefix(parameter)
        if (userInfo != null && "B2E" == userInfo!!.mbrType) {
            // SEC
            if ("0000000040" == userInfo!!.mbrCoId || "0000000041" == userInfo!!.mbrCoId) {
                strPrefix += "<SITE_TYPE_CD:contains:SEC>"
            } else if ("0000000107" == userInfo!!.mbrCoId || "0000000109" == userInfo!!.mbrCoId) {
                strPrefix += "<SITE_TYPE_CD:contains:SFC>"
            } else {
                strPrefix += "<SITE_TYPE_CD:contains:SSG>"
            }// SFC
        } else {
            strPrefix += "<SITE_TYPE_CD:contains:SSG>"
        }
        return PrefixVo(strPrefix, 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val faqList = arrayListOf<Faq>()
        var faq: Faq
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            faq = Faq()
            faq.setPostngId(search.w3GetField(name, "POSTNG_ID", i))
            faq.setPostngTitleNm(search.w3GetField(name, "POSTNG_TITLE_NM", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            faq.setPostngCntt(search.w3GetField(name, "POSTNG_CNTT", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            faq.setPostngBrwsCnt(search.w3GetField(name, "POSTNG_BRWS_CNT", i))
            faqList.add(faq)
        }
        result.faqList = faqList
        result.faqCount = search.w3GetResultTotalCount(name)
        return result
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        val strTarget = (parameter.target ?: "").toLowerCase()
        if (strCount == "" || strCount == "0") {
            strCount = "5"
        }
        if (strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "5"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }
}
