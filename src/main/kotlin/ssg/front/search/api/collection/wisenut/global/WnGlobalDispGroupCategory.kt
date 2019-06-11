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

class WnGlobalDispGroupCategory : WnDispGroup(), Prefixable, Groupable {
    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_GLOBAL,
                Prefixes.DISP_CTG_LST,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("BRAND_ID")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val strBrandInfo = search.w3GetMultiGroupByResult(name, "BRAND_ID") ?: ""
        println(strBrandInfo)
        if (strBrandInfo != "") {
            val brandList = ResultUtils.getBrandGroup(strBrandInfo)
            result.brandList = brandList
        }
        return result
    }

}
