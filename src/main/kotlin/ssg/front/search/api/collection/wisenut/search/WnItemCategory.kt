package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.CategoryGroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.CategoryGroupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Category

class WnItemCategory : WnCollection(), Prefixable, CategoryGroupable {
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
        val sb = ""
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
        )
        return PrefixVo(sb, 1)
    }

    override fun categoryGroupVo(parameter: Parameter): CategoryGroupVo {
        return CollectionUtils.getCategoryGroupBy(parameter)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val strSiteNo = parameter.siteNo ?: "6005"
        val ctgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "DISP_CTG_IDX"
        val count = search.w3GetCategoryCount(name, ctgIdxNm, 1)
        val categoryList = arrayListOf<Category>()
        // Category Result Set
        for (k in 0 until count) {
            val category = Category()
            val ctgs = (search.w3GetCategoryName(name, ctgIdxNm, 1, k) ?: "").split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            if (ctgs.size == 3) {
                category.setSiteNo(ctgs[0])
                category.setCtgId(ctgs[1])
                category.setCtgNm(ctgs[2])
            } else {
                category.setSiteNo("")
                category.setCtgId("")
                category.setCtgNm("")
            }
            category.setCtgItemCount(search.w3GetDocumentCountInCategory(name, ctgIdxNm, 1, k))
            categoryList.add(category)
        }
        // Category Result Sort ( 상품 많은 순 )
        if (categoryList != null && categoryList.size > 0) {
            categoryList.sortWith(Comparator<Category> { c1, c2 ->
                if (c1.ctgItemCount > c2.ctgItemCount) {
                    -1
                } else if (c1.ctgItemCount < c2.ctgItemCount) {
                    1
                } else {
                    0
                }
            })

        }
        result.categoryList = categoryList
        return result
    }

}
