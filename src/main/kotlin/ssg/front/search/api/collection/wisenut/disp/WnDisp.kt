package ssg.front.search.api.collection.wisenut.disp

import ssg.front.search.api.collection.wisenut.base.WnDispItem
import ssg.front.search.api.core.constants.Filters
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.FilterVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Filterable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.constant.Cls
import ssg.search.constant.Shpp

class WnDisp: WnDispItem(), Prefixable, Filterable {

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_DISP,
                Prefixes.FILTER_SITE_NO,
                Prefixes.DISP_CTG_LST,
                Prefixes.BRAND_ID,
                Prefixes.SALESTR_LST,
                Prefixes.SIZE,
                Prefixes.COLOR,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        // 혜택 필터 ( 이마트 점포상품만 보기, 이마트 온라인상품 )
        val strCls = parameter.cls ?: ""
        if (strCls.equals("emart", ignoreCase = true)) {
            sb.plus("<CLS:contains:").plus(Cls.EMART.getPrefix()).plus(">")
        }
        if (strCls.equals("emartonline", ignoreCase = true)) {
            sb.plus("<CLS:contains:").plus(Cls.EMARTONLINE.getPrefix()).plus(">")
        }
        // 무료배송, 특가상품
        val strFilter = parameter.filter ?: ""
        if (strFilter == "free") {
            sb.plus("<SHPP:contains:").plus(Shpp.FREE.getPrefix()).plus(">")
        }
        if (strFilter.indexOf("spprice") > -1) {
            sb.plus("<CLS:contains:").plus(Cls.SP_PRICE.getPrefix()).plus("|").plus(Cls.OBANJANG.getPrefix()).plus(">")
        }
        // 상품 관련 필터 ( 매직 픽업, 퀵배송 )
        val shppPrefix = CollectionUtils.getShppPrefix(parameter)
        if (shppPrefix != null && shppPrefix != "") {
            sb.plus(shppPrefix)
        }
        return PrefixVo(sb, 1)
    }

    override fun filterVo(parameter: Parameter): FilterVo {
        return FilterVo(Filters.PRC_FILTER.getFilter(parameter))
    }
}