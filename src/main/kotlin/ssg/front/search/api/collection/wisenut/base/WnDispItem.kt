package ssg.front.search.api.collection.wisenut.base

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CollectionUtils
import ssg.front.search.api.util.ResultUtils

open class WnDispItem : WnCollection(), Sortable, Pageable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "book"
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts : Sorts? = Sorts.BEST
        val strSort = parameter.sort ?: "best"
        val sortList = arrayListOf<Sort?>()
        try {
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch (e: IllegalArgumentException) {
            sorts = Sorts.BEST
        }

        if (sorts == Sorts.BEST) {
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
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
        }

        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        val strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0") {
            strCount = "40"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        // DISP COLLECTION 은 상품의 META DATA를 리턴
        return arrayOf("SITE_NO", "ITEM_ID", "ITEM_NM", "SELLPRC", "ITEM_REG_DIV_CD", "SHPP_TYPE_CD", "SHPP_TYPE_DTL_CD", "SALESTR_LST", "EXUS_ITEM_DIV_CD", "EXUS_ITEM_DTL_CD", "SHPP_MAIN_CD", "SHPP_MTHD_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // 상품 Meta Set 결과
        return ResultUtils.getItemResult(name, parameter, result, search)
    }
}