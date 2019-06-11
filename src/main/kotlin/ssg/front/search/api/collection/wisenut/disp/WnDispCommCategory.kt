package ssg.front.search.api.collection.wisenut.disp

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.CategoryGroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.CategoryGroupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.front.search.api.util.ResultUtils

class WnDispCommCategory: WnCollection(), Prefixable, CategoryGroupable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "category"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "SITE_NO")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "BRAND_NM", "TAG_NM", "GNRL_STD_DISP_CTG")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX,
                Prefixes.FILTER_SITE_NO,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun categoryGroupVo(parameter: Parameter): CategoryGroupVo {
        return CollectionUtils.getMobileCategoryGroupBy(parameter, 4)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        var idx = "DISP_CTG_IDX"
        if ((parameter.siteNo ?: "") == "6005") idx = "SCOM_DISP_CTG_IDX"
        result.commCategoryList = ResultUtils.getMobileCategoryGroup(search, name, idx, 4)
        return result
    }
}
