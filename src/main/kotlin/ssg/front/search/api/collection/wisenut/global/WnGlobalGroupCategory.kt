package ssg.front.search.api.collection.wisenut.global

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.base.WnDispGroup
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.PropertyGroupVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.function.PropertyGroupable
import ssg.front.search.api.util.ResultUtils
import ssg.search.result.Prc

class WnGlobalGroupCategory: WnDispGroup(), Prefixable, Groupable, PropertyGroupable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "groupVo"
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
                Prefixes.SRCH_PREFIX_GLOBAL,
                Prefixes.TEM_DISP_CTG_ID,
                Prefixes.SALESTR_LST_GROUP,
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
        return GroupVo("BRAND_ID")
    }

    override fun propertyGroupVo(parameter: Parameter): PropertyGroupVo {
        return PropertyGroupVo("SELLPRC")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // 가격 그루핑 결과
        val propct = search.w3GetCountPropertyGroup(name)
        val min = search.w3GetMinValuePropertyGroup(name)
        val max = search.w3GetMaxValuePropertyGroup(name)
        val prcGroupList = arrayListOf<Prc>()
        var prc: Prc
        for (i in 0 until propct) {
            prc = Prc()
            prc.setMinPrc(search.w3GetMinValueInPropertyGroup(name, i))
            prc.setMaxPrc(search.w3GetMaxValueInPropertyGroup(name, i))
            prcGroupList.add(prc)
        }
        result.minPrc = min
        result.maxPrc = max
        result.prcGroupList = prcGroupList

        // 브랜드 그룹핑
        val strBrandInfo = search.w3GetMultiGroupByResult(name, "BRAND_ID") ?: ""
        if (strBrandInfo != "") {
            val brandList = ResultUtils.getBrandGroup(strBrandInfo)
            result.brandList = brandList
        }
        return result
    }

}
