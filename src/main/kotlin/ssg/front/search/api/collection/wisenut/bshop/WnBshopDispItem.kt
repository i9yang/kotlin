package ssg.front.search.api.collection.wisenut.bshop

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.base.WnDispItem
import ssg.front.search.api.core.constants.BenefitFilters
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.front.search.api.util.ResultUtils

class WnBshopDispItem : WnDispItem(), Prefixable {

    override fun getAliasName(parameter: Parameter): String {
        return "bshop"
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_BSHOP,
                Prefixes.FILTER_SITE_NO,
                Prefixes.DISP_CTG_LST,
                Prefixes.SALESTR_LST, Prefixes.DEVICE_CD, Prefixes.MBR_CO_TYPE, Prefixes.BSHOP_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        val strCls = parameter.cls ?: ""
        val strBenefit = parameter.benefit ?: ""
        val strFilter = parameter.filter ?: ""
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

        return PrefixVo(sb.toString(), 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        return ResultUtils.getItemResult(name, parameter, result, search)
    }
}