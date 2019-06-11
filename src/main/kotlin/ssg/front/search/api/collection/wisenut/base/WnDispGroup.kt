package ssg.front.search.api.collection.wisenut.base

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Prefixable

open class WnDispGroup: WnCollection(), Prefixable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "disp_group"
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
                Prefixes.SRCH_PREFIX_DISP, Prefixes.DISP_CTG_LST, Prefixes.SALESTR_LST_GROUP, Prefixes.DEVICE_CD, Prefixes.MBR_CO_TYPE, Prefixes.SPL_VEN_ID, Prefixes.LRNK_SPL_VEN_ID, Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb.toString(), 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        return result
    }
}