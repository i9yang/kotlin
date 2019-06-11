package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import org.apache.commons.lang3.StringUtils
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.BenefitFilters
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.*
import ssg.front.search.api.function.*
import ssg.front.search.api.util.CollectionUtils
import ssg.front.search.api.util.ResultUtils

open class WnItem : WnCollection(), Prefixable, Sortable,  Pageable, Morphable, Rankable, Boostable
//Filterable,
{

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        // SITE
        val strSiteNo = StringUtils.defaultIfEmpty(parameter.siteNo, "")

        // DEVICE
        val strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.aplTgtMediaCd, "10")
        if (strAplTgtMediaCd == "10") {
            return "item$strSiteNo"
        } else {
            //채팅,선물하기
            val strTarget = StringUtils.defaultIfEmpty(parameter.target, "")
            if (strTarget.equals("chat_search_all", ignoreCase = true) || strTarget.equals("chat_search_item", ignoreCase = true)) {
                return "mobileChat$strSiteNo"
            } else if (strTarget.equals("chat_gift_all", ignoreCase = true) || strTarget.equals("chat_gift_item", ignoreCase = true)) {
                return "mobileGift$strSiteNo"
            }

            return "mobile$strSiteNo"
        }
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

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX, Prefixes.SRCH_PREFIX_MALL
//                Prefixes.SRCH_PREFIX, Prefixes.FILTER_SITE_NO, Prefixes.SRCH_CTG_ITEM_PREFIX, Prefixes.SALESTR_LST, Prefixes.DEVICE_CD, Prefixes.MBR_CO_TYPE, Prefixes.SPL_VEN_ID, Prefixes.LRNK_SPL_VEN_ID, Prefixes.SIZE, Prefixes.COLOR, Prefixes.SRCH_PSBL_YN, Prefixes.BRAND_ID, Prefixes.SCOM_EXPSR_YN, Prefixes.BENEFIT_FILTER, Prefixes.CLASSIFICATION_FILTER
        ).forEach {
            sb += it.getPrefix(parameter)
        }

        val strCls = parameter.cls?: ""
        val strBenefit = parameter.benefit?: ""
        val strFilter = parameter.filter?: ""
        val strShpp = parameter.shpp?: ""
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

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts: Sorts? = Sorts.BEST
        var strSort: String = parameter.sort!!
        var sortList: ArrayList<Sort?> = ArrayList()
        try{
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch(e: IllegalArgumentException){}

        if(sorts == Sorts.BEST){
            sortList.add(Sorts.WEIGHT.getSort(parameter))
            sortList.add(Sorts.RANK.getSort(parameter))
            sortList.add(Sorts.THRD.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        }else{
            sortList.add(sorts!!.getSort(parameter))
        }
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var page = parameter.page
        var count = parameter.count
        if(count.equals("") || count.equals("0")){
            count = "40"
        }

        if (parameter.isAdSearch == true && parameter.adSearchItemCount > 0) {
            CollectionUtils.getAdPageInfo(page, count, parameter.adSearchItemCount)
        }
        return CollectionUtils.getPageInfo(page, count)
    }

    override fun morphVo(parameter: Parameter): MorphVo {
        return MorphVo("ITEM_NM")
    }

//    override fun filterVo(parameter: Parameter): FilterVo {
//        return Filters.PRC_FILTER.getFilter()
//    }

    override fun rankVo(parameter: Parameter): RankVo {
        return CollectionUtils.getCollectionRanking(parameter)
    }

    override fun boostVo(parameter: Parameter): BoostVo {
        return CollectionUtils.getCategoryBoosting(parameter)
    }

    fun getQuery(parameter: Parameter): List<String> {
        return listOf("")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // 검색광고관련추가
        return if (parameter.isAdSearch!! && parameter.adSearchItemCount > 0) {
            ResultUtils.getAdItemResult(name, parameter, result, search)
        } else ResultUtils.getItemResult(name, parameter, result, search)
    }
}