package ssg.front.search.api.collection.wisenut.global

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.base.WnDispGroup
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.ResultUtils

class WnGlobalDispGroupBrand: WnDispGroup(), Prefixable, Groupable {

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_GLOBAL,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.BRAND_ID, Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("DISP_CTG_LST")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val ctgLst = search.w3GetMultiGroupByResult(name, "DISP_CTG_LST") ?: ""
        if (ctgLst != "") {
            result.categoryList = ResultUtils.getGlobalCategoryGroup(parameter.siteNo, parameter.dispCtgId, ctgLst, parameter.target)
        }
        return result
    }
}
