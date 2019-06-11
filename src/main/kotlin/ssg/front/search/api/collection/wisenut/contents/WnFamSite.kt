package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.function.Highlightable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Snippet
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.FamSite

class WnFamSite : WnCollection(), Highlightable, Pageable, Snippet {

    override fun getName(parameter: Parameter): String {
        return "fam_site"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "fam_site"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NM", "PAGE_TITLE_NM", "PAGE_LINK", "PAGE_CNTT", "OUTLINK_YN")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("PAGE_TITLE_NM", "PAGE_CNTT")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val famSiteList = ArrayList<FamSite>()
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            val famSite = FamSite()
            famSite.setSiteNm(search.w3GetField(name, "SITE_NM", i))
            famSite.setPageTitleNm(search.w3GetField(name, "PAGE_TITLE_NM", i))
            famSite.setPageLink(search.w3GetField(name, "PAGE_LINK", i))
            famSite.setPageCntt(search.w3GetField(name, "PAGE_CNTT", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            famSite.setOutlinkYn(search.w3GetField(name, "OUTLINK_YN", i))
            famSiteList.add(famSite)
        }
        result.famSiteCount = search.w3GetResultTotalCount(name)
        result.famSiteList = famSiteList
        return result
    }

    override fun pageVo(parameter: Parameter): PageVo {
        return CollectionUtils.getPageInfo("0", "100")
    }
}
