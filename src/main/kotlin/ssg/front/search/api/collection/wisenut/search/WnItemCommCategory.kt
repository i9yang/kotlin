package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import com.google.common.collect.Maps
import com.google.common.collect.Sets
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.CategoryGroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.CategoryGroupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.MobileCategory


class WnItemCommCategory: WnCollection(), Prefixable, CategoryGroupable {
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
                Prefixes.SRCH_PREFIX, Prefixes.FILTER_SITE_NO, Prefixes.SRCH_CTG_ITEM_PREFIX, Prefixes.SALESTR_LST_GROUP, Prefixes.DEVICE_CD, Prefixes.MBR_CO_TYPE, Prefixes.SPL_VEN_ID, Prefixes.LRNK_SPL_VEN_ID, Prefixes.SRCH_PSBL_YN, Prefixes.SCOM_EXPSR_YN
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun categoryGroupVo(parameter: Parameter): CategoryGroupVo {
        return if (parameter.target.equals("chat_ven_items", ignoreCase = true)) {
            CollectionUtils.getMobileCategoryGroupBy(parameter, 2)
        } else CollectionUtils.getMobileCategoryGroupBy(parameter, 4)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // result 에서 추천 카테고리 리스트를 가져온다.
        val recomList = result.recomDispCategoryList
        val recomLctgIdSet = Sets.newHashSet<String>()
        val recomMctgIdSet = Sets.newHashSet<String>()
        val recomSctgIdSet = Sets.newHashSet<String>()
        if (recomList != null && recomList!!.size > 0) {
            for (rMap in recomList!!) {
                recomLctgIdSet.add(rMap.get("DctgId"))
                recomMctgIdSet.add(rMap.get("MctgId"))
                recomSctgIdSet.add(rMap.get("SctgId"))
            }
        }

        var idx = "DISP_CTG_IDX"
        if (parameter.siteNo ?: "" == "6005") idx = "SCOM_DISP_CTG_IDX"
        var maxLevel = 4
        if (parameter.target.equals("chat_ven_items", ignoreCase = true)) maxLevel = 2

        var cList: List<MobileCategory> = ArrayList<MobileCategory>()
        var categoryMap = Maps.newHashMap<String, List<MobileCategory>>()
        var existLctgIdSet = Sets.newHashSet<String>()
        var existMctgIdSet = Sets.newHashSet<String>()
        var existSctgIdSet = Sets.newHashSet<String>()
        for (level in 1..maxLevel) {
            val categoryList = arrayListOf<MobileCategory>()

            //1레벨부터 전체 카테고리 돌린다.(부모/자식 상관없이 일단 모든 카테고리 다 넣는다)
            for (i in 0 until search.w3GetCategoryCount(name, idx, level)) {

                //1레벨은 1레벨만   ( 6001^0006510000^생활용품/세제/제지 )
                //2레벨부터는 자기자신 포함 전 레벨을 다 가져옴 ( 6001^0006510000^생활용품/세제/제지:6001^0006510003^세탁/주방/생활 세제 )
                val s = search.w3GetCategoryName(name, idx, level, i)
                val t = s.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val categoryToken = t[level - 1].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()

                val category = MobileCategory()
                category.setSiteNo(categoryToken[0])
                category.setDispCtgId(categoryToken[1])
                category.setDispCtgNm(categoryToken[2])
                category.setItemCount(search.w3GetDocumentCountInCategory(name, idx, level, i).toString())
                category.setDispCtgLvl(level.toString())
                category.setHasChild("false")
                if (level > 1)
                    category.setPriorDispCtgId(t[level - 2].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1])
                else
                    category.setPriorDispCtgId("")
                categoryList.add(category)

                //추천카테고리에 존재하는 카테고리를 추려내기 위함
                if (level == 1 && recomLctgIdSet != null && recomLctgIdSet.contains(categoryToken[1])) {
                    if (!existLctgIdSet.contains(categoryToken[1])) existLctgIdSet.add(categoryToken[1])
                }
                if (level == 2 && recomMctgIdSet != null && recomMctgIdSet.contains(categoryToken[1])) {
                    if (!existMctgIdSet.contains(categoryToken[1])) existMctgIdSet.add(categoryToken[1])
                }
                if (level == 3 && recomSctgIdSet != null && recomSctgIdSet.contains(categoryToken[1])) {
                    if (!existSctgIdSet.contains(categoryToken[1])) existSctgIdSet.add(categoryToken[1])
                }
            }

            // CATEGORY SORT(상품 많은순)
            if (categoryList != null && categoryList.size > 0) {
                categoryList.sortWith(Comparator<MobileCategory> { c1, c2 ->
                    if (Integer.parseInt(c1.itemCount) > Integer.parseInt(c2.itemCount)) {
                        -1
                    } else if (Integer.parseInt(c1.itemCount) < Integer.parseInt(c2.itemCount)) {
                        1
                    } else {
                        0
                    }
                })
            }
            categoryMap.put(level.toString(), categoryList)
        }

        // Set 한 데이터의 참조를 가지고 부모/자식관계를 생성한다.
        for (level in maxLevel downTo 2) {
            categoryMap[(level - 1).toString()]!!.forEach { prior ->
                categoryMap[level.toString()]!!.forEach { target ->
                    if(target.priorDispCtgId == prior.dispCtgId) {
                        prior.hasChild = "true"
                        prior.add(target)
                    }
                }
            }
        }
        cList = categoryMap.get("1")!!
        result.commCategoryList = cList

        //추천카테고리 정리(존재하지 않는 카테고리 삭제처리)
        if (recomList != null && recomList!!.size > 0) {
            recomList.filterNot { recomMap->
                    val dispLctgId = recomMap.get("DctgId")
                    val dispMcCtgId = recomMap.get("MctgId")
                    val dispScCtgId = recomMap.get("SctgId")
                    (!existLctgIdSet.contains(dispLctgId) || !existMctgIdSet.contains(dispMcCtgId) || !existSctgIdSet.contains(dispScCtgId))
            }
            result.recomDispCategoryList = recomList
        }
        return result
    }
}
