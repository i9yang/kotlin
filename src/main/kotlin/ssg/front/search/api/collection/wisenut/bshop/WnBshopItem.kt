package ssg.front.search.api.collection.wisenut.bshop

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.Collection
import ssg.front.search.api.collection.wisenut.search.WnItem
import ssg.front.search.api.core.constants.BenefitFilters
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.front.search.api.util.ResultUtils

open class WnBshopItem : WnItem(), Collection, Prefixable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "bshop_item"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf(
                // NORMAL META DATA
                "SITE_NO",
                "ITEM_ID",
                "ITEM_NM",
                "SELLPRC",
                "ITEM_REG_DIV_CD",
                "SHPP_TYPE_CD",
                "SHPP_TYPE_DTL_CD",
                "SALESTR_LST",
                "EXUS_ITEM_DIV_CD",
                "EXUS_ITEM_DTL_CD",
                "SHPP_MAIN_CD",
                "SHPP_MTHD_CD"
        )
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf(
                "ITEM_ID",
                "ITEM_NM",
                "ITEM_SRCHWD_NM",
                "MDL_NM",
                "BRAND_NM",
                "TAG_NM",
                "GNRL_STD_DISP_CTG"
        )
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts: Sorts? = Sorts.BEST
        var strSort: String = parameter.sort!!
        var sortList: ArrayList<Sort> = ArrayList()
        try{
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch(e: IllegalArgumentException){}

        if (sorts == Sorts.BEST) {
            sortList.plus(Sorts.WEIGHT.getSort(parameter))
            sortList.plus(Sorts.RANK.getSort(parameter))
            sortList.plus(Sorts.REGDT.getSort(parameter))
        } else {
            sortList.plus(sorts!!.getSort(parameter))
        }
        return SortVo(sortList)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var page = parameter.page
        var count = parameter.count
        if(count.equals("") || count.equals("0")){
            count = "40"
        }
        return CollectionUtils.getPageInfo(page, count)
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                  Prefixes.SRCH_PREFIX_BSHOP
                , Prefixes.SRCH_CTG_PREFIX
                , Prefixes.SALESTR_LST
                , Prefixes.DEVICE_CD
                , Prefixes.MBR_CO_TYPE
                , Prefixes.SRCH_PSBL_YN
                , Prefixes.BSHOP_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }

        val strCls = parameter.cls?: ""
        val strBenefit = parameter.benefit?: ""
        val strFilter = parameter.filter?: ""
        if (strCls.equals("emart", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.EMART.field).plus(":contains:").plus(BenefitFilters.EMART.prefix).plus(">")
        }
        if (strCls.equals("emartonline", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.EMARTONLINE.field).plus(":contains:").plus(BenefitFilters.EMARTONLINE.prefix).plus(">")
        }
        if (strCls.equals("department", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.DEPARTMENT.field).plus(":contains:").plus(BenefitFilters.DEPARTMENT.prefix).plus(">")
        }
        if (strCls.equals("shinsegae", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.SHINSEGAE.field).plus(":contains:").plus(BenefitFilters.SHINSEGAE.prefix).plus(">")
        }
        if (strBenefit.equals("free", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.FREE.field).plus(":contains:").plus(BenefitFilters.FREE.prefix).plus(">")
        }
        if (strBenefit.equals("prize", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.PRIZE.field).plus(":contains:").plus(BenefitFilters.PRIZE.prefix).plus(">")
        }
        if (strFilter.equals("free", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.FREE.field).plus(":contains:").plus(BenefitFilters.FREE.prefix).plus(">")
        }
        if (strFilter.equals("news", ignoreCase = true)) {
            sb.plus("<").plus(BenefitFilters.NEWS.field).plus(":contains:").plus(BenefitFilters.NEWS.prefix).plus(">")
        }
        if (strFilter.equals("obanjang|spprice", ignoreCase = true)) {
            sb.plus("<")
                    .plus(BenefitFilters.OBANJANG.field)
                    .plus(":contains:")
                    .plus(BenefitFilters.OBANJANG.prefix)
                    .plus("|")
                    .plus(BenefitFilters.SP_PRICE.prefix)
                    .plus(">")
        }
        val shppPrefix = CollectionUtils.getShppPrefix(parameter)
        if (shppPrefix != null && shppPrefix != "") {
            sb.plus(shppPrefix)
        }
        return PrefixVo(sb, 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        ResultUtils.getItemResult(name, parameter, result, search);
        return super.getResult(search, name, parameter, result)
    }
}