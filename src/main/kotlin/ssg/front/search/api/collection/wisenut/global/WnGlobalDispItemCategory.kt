package ssg.front.search.api.collection.wisenut.global

import ssg.front.search.api.collection.wisenut.base.WnDispItem
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Prefixable

class WnGlobalDispItemCategory: WnDispItem(), Prefixable {
    override fun getAliasName(parameter: Parameter): String {
        return "global"
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_GLOBAL,
                Prefixes.FILTER_SITE_NO,
                Prefixes.DISP_CTG_LST,
                Prefixes.SALESTR_LST,
                Prefixes.BRAND_ID,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }
}
