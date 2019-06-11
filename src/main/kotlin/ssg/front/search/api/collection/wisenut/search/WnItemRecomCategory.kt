package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import org.slf4j.LoggerFactory
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.CategoryGroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.CategoryGroupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils

class WnItemRecomCategory: WnCollection(), Prefixable, CategoryGroupable {
    private val logger = LoggerFactory.getLogger(WnItemRecomCategory::class.java)

    override fun getName(parameter: Parameter): String {
        return "item"
    }
    override fun getAliasName(parameter: Parameter): String {
        return "category"
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
                Prefixes.SRCH_PREFIX,
                Prefixes.FILTER_SITE_NO,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.SRCH_PSBL_YN,
                Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun categoryGroupVo(parameter: Parameter): CategoryGroupVo {
        return CollectionUtils.getRecomCategoryGroupBy(parameter)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val strSiteNo = parameter.siteNo
        if (strSiteNo == "6002" || strSiteNo == "6003") {
            return result
        }
        val ctgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "DISP_CTG_IDX"
        // 상품수가 가장 많은 순으로 정렬을 위해 리스트에 담는다.
        val ctgSortList = ArrayList<Map<String, String>>()
        for (i in 0 until search.w3GetCategoryCount(name, ctgIdxNm, 3)) {
            // 필요한 카테고리 정보만 추출하고 Sorting 한다.
            // 6005^5500002768^등산/캠핑/낚시:6005^5500002804^낚시용품:6005^5500002805^낚시장비(대/릴/찌/바늘)
            val ctgSortMap = HashMap<String, String>()
            ctgSortMap["CTG_INFO"] = search.w3GetCategoryName(name, ctgIdxNm, 3, i)
            ctgSortMap["CTG_COUNT"] = search.w3GetDocumentCountInCategory(name, ctgIdxNm, 3, i).toString()
            ctgSortList.add(ctgSortMap)
        }
        // 정렬 실행
        ctgSortList.sortWith(Comparator<Map<String, String>> { o1, o2 ->
            if (Integer.parseInt(o1["CTG_COUNT"]) > Integer.parseInt(o2["CTG_COUNT"]))
                return@Comparator -1
            else if (Integer.parseInt(o1["CTG_COUNT"]) < Integer.parseInt(o2["CTG_COUNT"])) return@Comparator 1
            0
        })

        // 정렬된 리스트에서 5개만 담아서 리턴한다.
        val recomCtgList = ArrayList<Map<String, String>>()
        for (i in ctgSortList.indices) {
            val ctgMap = HashMap<String, String>()
            val token = ctgSortList[i]["CTG_INFO"].toString()
            val count = ctgSortList[i]["CTG_COUNT"].toString()
            val ctgs = token!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (ctgs != null && ctgs.size > 2) {

                val dctg = ctgs[0].split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val mctg = ctgs[1].split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val sctg = ctgs[2].split("\\^".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                ctgMap["SctgId"] = sctg[1]
                ctgMap["SctgNm"] = sctg[2]
                ctgMap["SctgCount"] = count
                ctgMap["MctgId"] = mctg[1]
                ctgMap["MctgNm"] = mctg[2]
                ctgMap["DctgId"] = dctg[1]
                ctgMap["DctgNm"] = dctg[2]

                recomCtgList.add(ctgMap)

                if (i == 4) break
            }
        }
        result.recomCategoryList = recomCtgList
        return result
    }
}
