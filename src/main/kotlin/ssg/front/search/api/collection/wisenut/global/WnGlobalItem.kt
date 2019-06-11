package ssg.front.search.api.collection.wisenut.global

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.search.WnItem
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.ResultUtils

class WnGlobalItem : WnItem(), Prefixable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "global_item"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf(
                // NORMAL META DATA
                "SITE_NO", "ITEM_ID", "ITEM_NM", "SELLPRC", "ITEM_REG_DIV_CD", "SHPP_TYPE_CD", "SHPP_TYPE_DTL_CD", "SALESTR_LST", "EXUS_ITEM_DIV_CD", "EXUS_ITEM_DTL_CD", "SHPP_MAIN_CD", "SHPP_MTHD_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "BRAND_NM", "TAG_NM", "GNRL_STD_DISP_CTG")
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts = Sorts.BEST
        val strSort = parameter.sort!!
        val sortList = arrayListOf<Sort?>()
        try {
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch (e: IllegalArgumentException) {
        }

        if (sorts == Sorts.BEST) {
            sortList.add(Sorts.WEIGHT.getSort(parameter))
            sortList.add(Sorts.RANK.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
        } else {
            sortList.add(sorts.getSort(parameter))
        }
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_GLOBAL,
                Prefixes.TEM_DISP_ITEM_CTG_ID,
                Prefixes.SALESTR_LST,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SRCH_PSBL_YN,
                Prefixes.BRAND_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        return ResultUtils.getItemResult(name, parameter, result, search)
    }
}
