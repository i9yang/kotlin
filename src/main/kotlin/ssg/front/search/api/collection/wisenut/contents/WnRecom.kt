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
import ssg.search.result.Recom

class WnRecom: WnCollection(), Pageable, Prefixable {

    override fun getName(parameter: Parameter): String {
        return "rsearch"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "rsearch"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ITEM_ID", "ITEM_NM", "DISP_ORDR")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("CRITN_SRCHWD_NM")
    }

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0, 12)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val srchItemIds = StringBuilder()
        val recomList = arrayListOf<Recom>()
        val count = search.w3GetResultCount(name)
        var recom: Recom
        for (i in 0 until count) {
            recom = Recom()
            val strItemId = search.w3GetField(name, "ITEM_ID", i)
            val strItemNm = search.w3GetField(name, "ITEM_NM", i)
            val strSiteNo = search.w3GetField(name, "SITE_NO", i)

            recom.setItemId(strItemId)
            recom.setItemNm(strItemNm)
            recom.setSiteNo(strSiteNo)

            val ids = StringBuilder().append(strItemId).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                srchItemIds.append(ids)
            }

            recomList.add(recom)
        }
        result.recomList = recomList
        result.recomCount = search.w3GetResultTotalCount(name)
        result.recomItemIds = srchItemIds.toString() //itemId만 가지고 select 하기 위함
        return result
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SITE_NO_ONLY
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }
}
