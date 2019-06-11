package ssg.front.search.api.collection.wisenut.disp

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.base.WnDispGroup
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.PropertyGroupVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.PropertyGroupable
import ssg.front.search.api.util.ResultUtils
import ssg.search.result.Prc

class WnDispCommBrandGroup : WnDispGroup(), Groupable, PropertyGroupable {

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("BRAND_ID,SIZE_LST")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_DISP,
                Prefixes.DISP_CTG_LST,
                Prefixes.BRAND_ID,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb,1)
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
        // DISP_GROUP은 전시카테고리 전체 사이즈 그룹핑 결과를 사용
        val strSizeInfo = search.w3GetMultiGroupByResult(name, "SIZE_LST") ?: ""
        if (strSizeInfo != "") {
            val sizeList = ResultUtils.getSizeGroup(strSizeInfo)
            result.sizeList = sizeList
        }
        return result
    }
}
