package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.*
import ssg.front.search.api.function.*
import ssg.front.search.api.util.CollectionUtils
import ssg.front.search.api.util.ResultUtils

class WnItemMall : WnCollection(), Pageable, Prefixable, Groupable, Sortable, Rankable, Boostable {
    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "mall"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        // SITE
        val strSiteNo = parameter.siteNo ?: ""
        return if (strSiteNo != "6005") {
            arrayOf(
                    // NORMAL META DATA
                    "SITE_NO", "ITEM_ID", "ITEM_NM", "SELLPRC", "ITEM_REG_DIV_CD", "SHPP_TYPE_CD", "SHPP_TYPE_DTL_CD", "SALESTR_LST", "EXUS_ITEM_DIV_CD", "EXUS_ITEM_DTL_CD", "SHPP_MAIN_CD", "SHPP_MTHD_CD")
        } else arrayOf("ITEM_ID", "SITE_NO")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "BRAND_NM", "TAG_NM", "GNRL_STD_DISP_CTG")
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = "1"
        var strCount = "1"
        // 일반적인 유입
        val strSiteNo = parameter.siteNo ?: "6005"
        // S.COM 을 제외하고는 실패검색어 SSG 상품 추천 기능을 사용
        if (strSiteNo == "6005") {
            strCount = "1"
        } else {
            strPage = parameter.page ?: "1"
            strCount = parameter.count ?: ""
            if (strCount == "" || strCount == "0") {
                strCount = "40"
            }
        }// 검색결과 없음 SSG 상품 추천
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        // Item Mall Collection 은 SSG 기준으로만 호출한다.
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX_MALL,
                Prefixes.SALESTR_LST_MALL,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SRCH_PSBL_YN,
                Prefixes.SCOM_EXPSR_YN_SSG
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sortVo(parameter: Parameter): SortVo {
        val strSiteNo = parameter.siteNo ?: "6005"
        val sortList = arrayListOf<Sort?>()
        if (strSiteNo == "6005") {
            sortList.add(Sorts.RANK.getSort(parameter))
        } else {
            sortList.add(Sorts.WEIGHT.getSort(parameter))
            sortList.add(Sorts.RANK.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
        }

        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun rankVo(parameter: Parameter): RankVo {
        if (parameter.siteNo != "6005") {
            return RankVo("6005" + "/" + CollectionUtils.getOriQuery(parameter))
        }
        return RankVo()
    }

    override fun boostVo(parameter: Parameter): BoostVo {
        if (parameter.siteNo != "6005"){
            return BoostVo("SSGBOOST")
        }
        return BoostVo()
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        var result = result
        val strSiteInfo = search.w3GetMultiGroupByResult(name, "SITE_NO") ?: ""
        val strScomInfo = search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN") ?: ""
        if (strSiteInfo != "" && strScomInfo != "") {
            result.mallCountMap = ResultUtils.getSiteGroup(strSiteInfo, strScomInfo)
        }
        // S.COM 이 아닌 다른 몰의 경우 실패검색어 SSG 상품 추천 기능을 사용
        if (parameter.siteNo != "6005") {
            result = ResultUtils.getItemResult(name, parameter, result, search)
        }
        return result
    }
}
