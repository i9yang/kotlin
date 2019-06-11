package ssg.front.search.api.collection.wisenut.brand

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.ResultUtils

class WnBookBrandDisp : WnCollection(), Groupable, Prefixable {

    override fun getName(parameter: Parameter): String {
        return "book"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "book_brand_disp"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ITEM_ID")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID")
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("DISP_CTG_LST")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_DISP,
                Prefixes.BRAND_ID,
                Prefixes.SALESTR_LST,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SCOM_EXPSR_YN_ALL
        ).forEach{
            sb.plus(it.getPrefix(parameter))
        }
        return PrefixVo(sb, 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val ctgLst = search.w3GetMultiGroupByResult(name, "DISP_CTG_LST") ?: ""
        if (ctgLst != "") {
            result.bookCategoryList = ResultUtils.getBrandCategoryGroup(parameter.siteNo, parameter.dispCtgId, ctgLst)
        }
        return result
    }
}