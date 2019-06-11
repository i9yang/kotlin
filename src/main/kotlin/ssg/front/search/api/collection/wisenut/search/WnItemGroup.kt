package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.ResultUtils
import ssg.search.result.Prc

class WnItemGroup : WnCollection(), Groupable, Prefixable {

    internal var logger = LoggerFactory.getLogger(WnItemGroup::class.java)

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "groupVo"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf(
                "ITEM_ID",
                "SITE_NO"
        )
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf(
                "ITEM_ID",
                "ITEM_NM",
                "ITEM_SRCHWD_NM",
                "MDL_NM",
                "BRAND_NM",
                "TAG_NM",
                "GNRL_STD_DISP_CTG"
        )
    }
    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("BRAND_ID,SIZE_LST,SELLPRC_LST")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val sb = ""
        val iter = setOf(
                Prefixes.SRCH_PREFIX,
                Prefixes.FILTER_SITE_NO,
                Prefixes.SRCH_CTG_ITEM_PREFIX,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SRCH_PSBL_YN,
                Prefixes.SCOM_EXPSR_YN
        ).iterator()
        while (iter.hasNext()) {
            val next = iter.next().getPrefix(parameter)
            if (StringUtils.isNotBlank(next)) {
                sb.plus(next)
            }
        }
        return PrefixVo(sb, 1)
    }

    fun getQuery(parameter: Parameter): List<String> {
        return listOf("")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // 가격 그루핑 결과
        val min = 0
        var max = 0
        var prcGroupList: List<Prc> = arrayListOf()

        val sellprcLst = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SELLPRC_LST"), "")

        logger.info("################### {}", sellprcLst)

        if (sellprcLst != "") {
            prcGroupList = ResultUtils.getSellprcGroupping(sellprcLst)

            if (prcGroupList.size > 0) {
                max = prcGroupList[prcGroupList.size - 1].getMaxPrc()
            }
        }

        result.minPrc = min
        result.maxPrc = max
        result.prcGroupList = prcGroupList

        // 브랜드 그룹핑
        val strBrandInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "BRAND_ID"), "")
        if (strBrandInfo != "") {
            val brandList = ResultUtils.getBrandGroup(strBrandInfo)
            result.brandList = brandList
        }
        // 전체 사이즈 그룹핑
        val strSizeInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SIZE_LST"), "")
        if (strSizeInfo != "") {
            val sizeList = ResultUtils.getSizeGroup(strSizeInfo)
            result.sizeList = sizeList
        }
        return result
    }
}