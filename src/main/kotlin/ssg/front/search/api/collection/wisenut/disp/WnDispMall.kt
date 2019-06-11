package ssg.front.search.api.collection.wisenut.disp

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Filters
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.FilterVo
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Filterable
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.ResultUtils

class WnDispMall: WnCollection(), Prefixable, Groupable, Filterable {
    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "disp_mall"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "SITE_NO")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("DOCID")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_MALL,
                Prefixes.DISP_CTG_LST,
                Prefixes.BRAND_ID,
                Prefixes.SALESTR_LST_MALL,
                Prefixes.SIZE,
                Prefixes.COLOR,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb,1)
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("SITE_NO,SCOM_EXPSR_YN")
    }

    override fun filterVo(parameter: Parameter): FilterVo {
        return FilterVo(Filters.PRC_FILTER.getFilter(parameter))
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // DISP_MALL 은 전시카테고리 사이트 그룹핑 결과를 사용
        val strSiteInfo = search.w3GetMultiGroupByResult(name, "SITE_NO") ?: ""
        val strScomInfo = search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN") ?: ""
        if (strSiteInfo != "" && strScomInfo != "") {
            result.mallCountMap = ResultUtils.getSiteGroup(strSiteInfo, strScomInfo)
        }
        return result
    }
}