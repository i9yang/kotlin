package ssg.front.search.api.collection.wisenut.global

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.base.WnDispItem
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.CategoryGroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.CategoryGroupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Category
import java.util.*

class WnGlobalItemCategory: WnDispItem(), Prefixable, CategoryGroupable {

    override fun getName(parameter: Parameter): String {
        return "item"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "global"
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
                Prefixes.FILTER_SITE_NO,
                Prefixes.TEM_DISP_CTG_ID,
                Prefixes.SALESTR_LST,
                Prefixes.DEVICE_CD,
                Prefixes.MBR_CO_TYPE,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun categoryGroupVo(parameter: Parameter): CategoryGroupVo {
        return CollectionUtils.getGlobalCategoryGroupBy(parameter)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val strSiteNo = parameter.siteNo
        val ctgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "TEM_DISP_CTG_IDX"
        val ctgId = parameter.ctgId ?: ""
        val ctgIds = parameter.ctgIds ?: ""
        val ctgLv = parameter.ctgLv ?: "0"
        val ctgLast = parameter.ctgLast ?: "N"

        /* category */
        var categoryList = arrayListOf<Category>()
        if (ctgIds != "") {
            var lv = 2
            var totCnt = 0
            try {
                lv = Integer.parseInt(ctgLv)
            } catch (e: NumberFormatException) {
                lv = 1
            }

            val count = search.w3GetCategoryCount(name, ctgIdxNm, lv)
            var ctgStr: String? = ""
            for (k in 0 until count) {
                var category = Category()
                ctgStr = search.w3GetCategoryName(name, ctgIdxNm, lv, k)
                val ctgs = (ctgStr ?: "").split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

                val ids = ctgIds.split("\\|".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                for (id in ids) {
                    if (ctgStr!!.indexOf(id) > -1) {
                        category = Category()
                        category.setThemeYn("Y")
                        category.setCtgLevel(lv)
                        category.tokenizeCtg(ctgStr)
                        category.setCtgItemCount(search.w3GetDocumentCountInCategory(name, ctgIdxNm, lv, k))
                        category.setHasChild(true)
                        categoryList!!.add(category)
                        totCnt += search.w3GetDocumentCountInCategory(name, ctgIdxNm, lv, k)
                    }
                }
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
            // 1 Depth에서 선택검색하지 않은 경우에는 Depth를 보여준다.
            if (ctgStr != null && ctgStr != "") {
                var k = 1
                if (ctgStr.indexOf(":") > -1) {
                    val ctgSt = StringTokenizer(ctgStr, ":")
                    while (ctgSt.hasMoreTokens()) {
                        val tk = ctgSt.nextToken()
                        val splitStr = tk.split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                        if (k < lv) {
                            result.ctgSiteNo = splitStr[0]
                            result.setCtgId(k, splitStr[1])
                            result.setCtgNm(k, splitStr[2])
                            if (k == lv - 1) {
                                result.setCtgCount(lv, totCnt)
                            }
                            k++
                        }
                    }
                }
            }
            result.categoryList = categoryList
        } else {
            var viewLv = 1
            if (ctgLv == "0")
                viewLv = 2
            else if (ctgLv == "1")
                viewLv = 2
            else if (ctgLv == "2") {
                if (ctgLast == "Y")
                    viewLv = 2
                else
                    viewLv = 3
            } else if (ctgLv == "3") {
                if (ctgLast == "Y")
                    viewLv = 3
                else
                    viewLv = 4
            } else if (ctgLv == "4") {
                if (ctgLast == "Y")
                    viewLv = 4
                else
                    viewLv = 5
            } else
                viewLv = 1

            /* 카테고리 SET */
            var count = search.w3GetCategoryCount(name, ctgIdxNm, viewLv)
            categoryList = ArrayList<Category>()

            for (k in 0 until count) {
                val c = Category()
                c.tokenizeCtg(search.w3GetCategoryName(name, ctgIdxNm, viewLv, k))
                c.setCtgItemCount(search.w3GetDocumentCountInCategory(name, ctgIdxNm, viewLv, k))
                c.setCtgLevel(viewLv)
                c.setThemeYn("Y")
                categoryList.add(c)
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
            /* 카테고리 자식찾기 ( ctgLastYn이 N인 경우에는 다음 뎁스를 무조건 가져온다고 가정하고 ) */
            if (ctgLast == "N") {
                viewLv = viewLv + 1
                count = search.w3GetCategoryCount(name, ctgIdxNm, viewLv)
                for (j in categoryList.indices) {
                    val c = categoryList[j]
                    for (k in 0 until count) {
                        val nm = search.w3GetCategoryName(name, ctgIdxNm, viewLv, k)
                        if (nm.indexOf(c.getCtgId()) > -1) {
                            c.setHasChild(true)
                            categoryList[j] = c
                            break
                        }
                    }
                }
            }
            result.ctgViewCount = categoryList.size

            /* Result에 들어갈 카테고리( 경로정보 ) set */
            if (ctgLv != "0" && ctgId != "") {
                var lv = 2
                try {
                    lv = Integer.parseInt(ctgLv)
                } catch (e: NumberFormatException) {
                    lv = 1
                }

                val count1 = search.w3GetCategoryCount(name, ctgIdxNm, lv)
                for (k in 0 until count1) {
                    val nm = search.w3GetCategoryName(name, ctgIdxNm, lv, k)
                    val itemCount = search.w3GetDocumentCountInCategory(name, ctgIdxNm, lv, k)
                    if (nm.indexOf(ctgId) > -1) {
                        if (nm.indexOf(":") > -1) {
                            var tkLv = 1
                            val resultToken = StringTokenizer(nm, "\\:")
                            while (resultToken.hasMoreTokens()) {
                                val strSplit = resultToken.nextToken().split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                                result.ctgSiteNo = strSplit[0]
                                result.setCtgId(tkLv, strSplit[1])
                                result.setCtgNm(tkLv, strSplit[2])
                                tkLv++
                            }
                        } else {
                            val strSplit = nm.split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            result.ctgSiteNo = strSplit[0]
                            result.setCtgId(lv, strSplit[1])
                            result.setCtgNm(lv, strSplit[2])
                        }
                        result.setCtgCount(lv, itemCount)
                    }
                }
            }
            result.categoryList = categoryList
        }

        return result
    }

}
