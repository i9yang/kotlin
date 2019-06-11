package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Trecipe

class WnTrecipe: WnCollection(), Prefixable, Sortable, Pageable {

    override fun getName(parameter: Parameter): String {
        return "trecipe"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "trecipe"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("RECIPE_NO", "RECIPE_NM", "VOD_YN", "VOD_URL", "RECIPE_TAG", "TRECIPE_BEST_SCR", "TRECIPE_BRWS_CNT", "COOK_QNTY_CD", "COOK_DIFCLVL_CD", "COOK_KIND_CD", "RQRM_TIME_CD", "IMG_FILE_NM", "COOK_INGRD_NM", "COOK_DESC", "REG_DTS")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("RECIPE_NO", "RECIPE_NM", "COOK_KIND_CD", "RECIPE_TAG")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()
        val vodYn = parameter.vodYn ?: ""
        if (vodYn != "") {
            prefix.append("<VOD_YN:contains:").append(vodYn).append(">")
        }
        return PrefixVo(prefix.toString(), 1)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts = Sorts.BEST
        val strSort = parameter.sort ?: "best"
        val sortList = arrayListOf<Sort?>()
        try {
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch (e: IllegalArgumentException) {
        }

        if (sorts == Sorts.BEST) {
            sortList.add(Sorts.TRECIPE_BEST_SCR.getSort(parameter))
        } else if (sorts == Sorts.REGDT) {
            sortList.add(Sorts.REG_DTS.getSort(parameter))
        } else {
            return SortVo(arrayListOf())
        }
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = parameter.page ?: "1"
        val strTarget = (parameter.target ?: "").toLowerCase()
        val strTcount = parameter.count ?: "0"
        // 더보기 없으니, 100개까지 한번에 내려줌(검색만)
        var strCount = "100"
        //검색 결과에서는 페이징 없이 무조건 100개지만, 전시에서는 40개씩
        if (strTarget.startsWith("trecipe") && strTcount != "0") {
            strCount = strTcount
        } else if (strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val trecipeList = arrayListOf<Trecipe>()
        var trecipe: Trecipe
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            trecipe = Trecipe()
            trecipe.setRecipeNo(search.w3GetField(name, "RECIPE_NO", i))
            trecipe.setRecipeNm(search.w3GetField(name, "RECIPE_NM", i))
            trecipe.setVodYn(search.w3GetField(name, "VOD_YN", i))
            trecipe.setVodUrl(search.w3GetField(name, "VOD_URL", i))
            trecipe.setRecipeTag(search.w3GetField(name, "RECIPE_TAG", i))
            trecipe.setTrecipeBestScr(search.w3GetField(name, "TRECIPE_BEST_SCR", i))
            trecipe.setTrecipeBrwsCnt(search.w3GetField(name, "TRECIPE_BRWS_CNT", i))
            trecipe.setCookQntyCd(search.w3GetField(name, "COOK_QNTY_CD", i))
            trecipe.setCookDifclvlCd(search.w3GetField(name, "COOK_DIFCLVL_CD", i))
            trecipe.setCookKindCd(search.w3GetField(name, "COOK_KIND_CD", i))
            trecipe.setRqrmTimeCd(search.w3GetField(name, "RQRM_TIME_CD", i))
            trecipe.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i))
            trecipe.setCookIngrdNm(search.w3GetField(name, "COOK_INGRD_NM", i))
            trecipe.setCookDesc(search.w3GetField(name, "COOK_DESC", i))
            trecipeList.add(trecipe)
        }
        result.trecipeList = trecipeList
        result.trecipeCount = search.w3GetResultTotalCount(name)
        return result
    }
}
