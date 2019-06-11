package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Prefixable
import ssg.search.result.SrchGuide

class WnSrchGuide : WnCollection(), Prefixable {

    override fun getName(parameter: Parameter): String {
        return "srchguide"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "srchguide"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("CRITN_SRCHWD_NM", "SRCH_TYPE_CD", "PNSHOP", "ISSUE_ITEM", "SOCIAL", "RECIPE", "TGT_ITEM", "KEYWD_NM_LST", "ITEM_ID_LST", "SHRTC_TGT_TYPE_CD", "SHRTC_TGT_ID", "LINK_URL", "KEYWD_TAG", "PNSHOP_TAG", "RECIPE_TAG")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("CRITN_SRCHWD_NM", "TGT_SRCHWD_NM")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SITE_NO_USE_FILTER
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val srchGuideList = arrayListOf<SrchGuide>()
        var srchGuide: SrchGuide
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            srchGuide = SrchGuide()
            srchGuide.setSrchCritnId(search.w3GetField(name, "SRCH_CRITN_ID", i))
            srchGuide.setCritnSrchwdNm(search.w3GetField(name, "CRITN_SRCHWD_NM", i))
            srchGuide.setSrchTypeCd(search.w3GetField(name, "SRCH_TYPE_CD", i))
            srchGuide.setPnshop(search.w3GetField(name, "PNSHOP", i))
            srchGuide.setIssueItem(search.w3GetField(name, "ISSUE_ITEM", i))
            srchGuide.setSocial(search.w3GetField(name, "SOCIAL", i))
            srchGuide.setRecipe(search.w3GetField(name, "RECIPE", i))
            srchGuide.setTgtItem(search.w3GetField(name, "TGT_ITEM", i))
            srchGuide.setKeywdNmLst(search.w3GetField(name, "KEYWD_NM_LST", i))
            srchGuide.setItemIdLst(search.w3GetField(name, "ITEM_ID_LST", i))
            srchGuide.setShrtcTgtId(search.w3GetField(name, "SHRTC_TGT_TYPE_CD", i))
            srchGuide.setShrtcTgtTypeCd(search.w3GetField(name, "SHRTC_TGT_ID", i))
            srchGuide.setLinkUrl(search.w3GetField(name, "LINK_URL", i))
            srchGuide.setKeywdTag(search.w3GetField(name, "KEYWD_TAG", i))
            srchGuide.setPnshopTag(search.w3GetField(name, "PNSHOP_TAG", i))
            srchGuide.setRecipeTag(search.w3GetField(name, "RECIPE_TAG", i))
            srchGuideList.add(srchGuide)
        }
        result.srchGuideList = srchGuideList
        result.srchGuideCount = search.w3GetResultTotalCount(name)
        return result
    }

}
