package ssg.front.search.api.collection.wisenut.disp

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Filters
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.FilterVo
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Filterable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CollectionUtils
import ssg.front.search.api.util.ResultUtils
import ssg.search.constant.Cls

class WnBookDisp: WnCollection(), Prefixable, Filterable, Pageable, Sortable {

    override fun getName(parameter: Parameter): String {
        return "book"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "book_disp"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ITEM_ID", "ITEM_NM", "SELLPRC", "ITEM_REG_DIV_CD", "SHPP_TYPE_CD", "SHPP_TYPE_DTL_CD", "SALESTR_LST", "EXUS_ITEM_DIV_CD", "EXUS_ITEM_DTL_CD", "SHPP_MAIN_CD", "SHPP_MTHD_CD", "AUTHOR_NM", "TRLTPE_NM", "PUBSCO_NM", "FXPRC")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX, Prefixes.FILTER_SITE_NO, Prefixes.DISP_CTG_LST, Prefixes.BRAND_ID,
                Prefixes.SALESTR_LST, Prefixes.DEVICE_CD, Prefixes.SCOM_EXPSR_YN, Prefixes.MBR_CO_TYPE
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
        // 상품 관련 필터 ( 매직 픽업, 퀵배송 )
        val shppPrefix = CollectionUtils.getShppPrefix(parameter)
        if (shppPrefix != null && shppPrefix != "") {
            sb.plus(shppPrefix)
        }
        return PrefixVo(sb,1)
    }

    override fun filterVo(parameter: Parameter): FilterVo {
        return FilterVo(Filters.PRC_FILTER.getFilter(parameter))
    }

    override fun pageVo(parameter: Parameter): PageVo {
        val strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        val strSiteNo = parameter.siteNo ?: "6005"
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0") {
            if (strSiteNo == "6005") {
                strCount = "40"
            } else if (strSiteNo == "6004") {
                strCount = "40"
            } else if (strSiteNo == "6009") {
                strCount = "40"
            } else if (strSiteNo == "6001") {
                strCount = "40"
            } else if (strSiteNo == "6002") {
                strCount = "15"
            }
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts = Sorts.BEST
        val strSort = parameter.sort ?: "best"
        val sortList = arrayListOf<Sort?>()
        try {
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch (e: IllegalArgumentException) {
        }

        // 동점이 많기때문에 1~4 차 까지 정렬한다.
        if (sorts == Sorts.BEST) {
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else if (sorts == Sorts.PRCASC || sorts == Sorts.PRCDSC) {
            sortList.add(sorts.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
        } else if (sorts == Sorts.CNT) {
            sortList.add(sorts.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else if (sorts == Sorts.REGDT) {
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.DISP_BEST.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        }
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // 상품 Meta Set 결과
        return ResultUtils.getItemResult(name, parameter, result, search)
    }
}
