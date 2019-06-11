package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import org.slf4j.LoggerFactory
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.CategoryGroupVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.CategoryGroupable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Category

class WnBookGroupCategory : WnBookCategory(), CategoryGroupable, Prefixable {
    private var lctgId = ""
    private var lctgNm = ""

    private var mctgId = ""
    private var mctgNm = ""

    private var sctgId = ""
    private var sctgNm = ""

    private var dctgId = ""
    private var dctgNm = ""

    private val logger = LoggerFactory.getLogger(WnBookGroupCategory::class.java!!)
    private val itemCountComparator = Comparator<Category> { c1, c2 ->
        if (c1.recomYn == "Y") {
            -1
        } else if (c2.recomYn == "Y") {
            1
        } else if (c1.ctgItemCount > c2.ctgItemCount) {
            -1
        } else if (c1.ctgItemCount < c2.ctgItemCount) {
            1
        } else {
            0
        }
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX,
                Prefixes.SALESTR_LST_GROUP,
                Prefixes.MBR_CO_TYPE,
                Prefixes.DEVICE_CD,
                Prefixes.SCOM_EXPSR_YN,
                Prefixes.SRCH_CTG_PREFIX
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun categoryGroupVo(parameter: Parameter): CategoryGroupVo {
        return CollectionUtils.getCategoryGroupByLevel(parameter)
    }

    /**
     * 카테고리 정보 SET
     * @param level
     * @param next
     * @param count
     * @param name
     * @param strCtgIdxNm
     * @param search
     * @return
     */
    private fun getCategory(level: Int, next: Int, count: Int, name: String, strCtgIdxNm: String, search: Search): MutableList<Category> {
        val childCtgSet = Sets.newHashSet<String>()
        val ctgList = arrayListOf<Category>()
        if (next > 0) {
            val nextCount = search.w3GetCategoryCount(name, strCtgIdxNm, next)
            for (i in 0 until nextCount) {
                val tgtCtgInfo = search.w3GetCategoryName(name, strCtgIdxNm, next, i)
                childCtgSet.add(tgtCtgInfo.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[level - 1])
            }
        }
        for (i in 0 until count) {
            val ctgInfo = search.w3GetCategoryName(name, strCtgIdxNm, level, i)
            val ctgToken = ctgInfo.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            val category = Category()
            if (level > 0) {
                val ctgs = ctgToken[level - 1].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                category.setSiteNo(ctgs[0])
                category.setCtgId(ctgs[1])
                // 부모가 존재하는 경우 부모 ctgId를 set
                if (level > 1) {
                    category.setPriorCtgId(ctgToken[level - 2].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()[1])
                }
                category.setCtgNm(ctgs[2])
                category.setCtgLevel(level)
                category.setCtgItemCount(search.w3GetDocumentCountInCategory(name, strCtgIdxNm, level, i))
                if (childCtgSet != null && childCtgSet.contains(ctgToken[level - 1])) {
                    category.setHasChild(true)
                }
                ctgList.add(category)
            }
        }
        return ctgList
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // result 에서 추천 카테고리 리스트를 가져온다.
        val recomList = result.recomDispCategoryList
        val recomLctgIdSet = Sets.newHashSet<String>()
        val recomMctgIdSet = Sets.newHashSet<String>()
        if (recomList != null && recomList!!.size > 0) {
            for (rMap in recomList!!) {
                recomLctgIdSet.add(rMap.get("DctgId"))
                recomMctgIdSet.add(rMap.get("MctgId"))
            }
        }
        val strSiteNo = parameter.siteNo ?: "6005"
        val strCtgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "DISP_CTG_IDX"
        // 통합검색의 경우 2개 뎁스로 작업을 진행한다. ( 타겟이 ALL 이고 CTG_ID, CTG_LV 값이 없는 경우 )
        if ((parameter.target.equals("all", ignoreCase = true) || parameter.target.equals("book", ignoreCase = true) || parameter.target.equals("category", ignoreCase = true)) && parameter.ctgId.isNullOrEmpty() && parameter.ctgLv.isNullOrEmpty()) {
            val ctgList = arrayListOf<Category>()
            // LEVEL 1 을 먼저 선행하고,
            var level = 1
            var next = 2
            var count = search.w3GetCategoryCount(name, strCtgIdxNm, level)
            val ctgLv1List = this.getCategory(level, next, count, name, strCtgIdxNm, search)
            // LEVEL 2 를 실행
            level = 2
            next = 3
            count = search.w3GetCategoryCount(name, strCtgIdxNm, level)
            val ctgLv2List = this.getCategory(level, next, count, name, strCtgIdxNm, search)
            // 결과가 정상이라면
            if (ctgLv1List != null && ctgLv1List.size > 0) {
                for (i in ctgLv1List.indices) {
                    val childCtgList = arrayListOf<Category>()
                    val c1 = ctgLv1List[i]
                    val strCtgId = c1.getCtgId()
                    for (j in ctgLv2List.indices) {
                        val c2 = ctgLv2List[j]
                        if (strCtgId == c2.getPriorCtgId()) {
                            if (recomMctgIdSet != null && recomMctgIdSet.contains(c2.getCtgId())) {
                                c2.setRecomYn("Y")
                            }
                            childCtgList.add(c2)
                        }
                    }
                    // childCtgList Sort
                    if (childCtgList != null && childCtgList.size > 0) childCtgList.sortWith(itemCountComparator)
                    c1.setChildCategoryList(childCtgList)
                    if (recomLctgIdSet != null && recomLctgIdSet.contains(c1.getCtgId())) {
                        c1.setRecomYn("Y")
                    }
                    ctgList.add(c1)
                }
                // CtgList Sort
                if (ctgList != null && ctgList.size > 0) ctgList.sortWith(itemCountComparator)
                result.categoryList = ctgList
            }
        } else {
            val level = CollectionUtils.getCategoryCurrentLevel(parameter)
            val next = CollectionUtils.getCategoryNextLevel(parameter)
            val count = search.w3GetCategoryCount(name, strCtgIdxNm, level)
            // 다다음 뎁스의 카테고리 정보를 이용해서 자식여부를 판별하기 위한 다음 뎁스 카테고리 아이디를 미리 매핑해둔다 ( 1, 2, 3 (자식여부Y) 레벨 클릭시에만 사용 )
            val ctgResultList = this.getCategory(level, next, count, name, strCtgIdxNm, search)
            var ctgList: MutableList<Category>? = Lists.newArrayList()
            if (ctgList != null) {
                // 중카의 경우 추천 카테고리 여부 매핑
                if (level == 2) {
                    for (c in ctgResultList) {
                        if (recomMctgIdSet != null && recomMctgIdSet.contains(c.getCtgId())) {
                            c.setRecomYn("Y")
                        }
                        ctgList.add(c)
                    }
                } else {
                    ctgList = ctgResultList
                }
                // CtgList Sort
                if (ctgList != null && ctgList.size > 0) ctgList.sortWith(itemCountComparator)
                result.categoryList = ctgList
                // 현재 뎁스가져오기
                val ctgLv = parameter.ctgLv
                val ctgId = parameter.ctgId
                for (i in 0 until search.w3GetCategoryCount(name, strCtgIdxNm, level)) {
                    val ctgInfo = search.w3GetCategoryName(name, strCtgIdxNm, level, i)
                    val ctgInfos = ctgInfo.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                    when (ctgInfos.size) {
                        4 -> {
                            var cTokens = ctgInfos[3].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            if (ctgLv == "4") {
                                if (ctgId == cTokens[1]) {
                                    dctgId = cTokens[1]
                                    dctgNm = cTokens[2]
                                }
                            }
                            cTokens = ctgInfos[2].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            if (ctgLv == "3") {
                                if (ctgId == cTokens[1]) {
                                    sctgId = cTokens[1]
                                    sctgNm = cTokens[2]
                                }
                            } else {
                                sctgId = cTokens[1]
                                sctgNm = cTokens[2]
                            }
                            cTokens = ctgInfos[1].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            mctgId = cTokens[1]
                            mctgNm = cTokens[2]
                            cTokens = ctgInfos[0].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            lctgId = cTokens[1]
                            lctgNm = cTokens[2]
                        }
                        3 -> {
                            var cTokens = ctgInfos[2].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            if (ctgLv == "3") {
                                if (ctgId == cTokens[1]) {
                                    sctgId = cTokens[1]
                                    sctgNm = cTokens[2]
                                }
                            } else {
                                sctgId = cTokens[1]
                                sctgNm = cTokens[2]
                            }
                            cTokens = ctgInfos[1].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            mctgId = cTokens[1]
                            mctgNm = cTokens[2]
                            cTokens = ctgInfos[0].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            lctgId = cTokens[1]
                            lctgNm = cTokens[2]
                        }
                        2 -> {
                            var cTokens = ctgInfos[1].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            mctgId = cTokens[1]
                            mctgNm = cTokens[2]
                            cTokens = ctgInfos[0].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            lctgId = cTokens[1]
                            lctgNm = cTokens[2]
                        }
                        1 -> {
                            var cTokens = ctgInfos[0].split("\\^".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            lctgId = cTokens[1]
                            lctgNm = cTokens[2]
                        }
                    }
                }
                if (ctgLv == "1") {
                    result.lctgId = lctgId
                    result.lctgNm = lctgNm
                } else if (ctgLv == "2") {
                    result.lctgId = lctgId
                    result.lctgNm = lctgNm
                    result.mctgId = mctgId
                    result.mctgNm = mctgNm
                } else if (ctgLv == "3") {
                    result.lctgId = lctgId
                    result.lctgNm = lctgNm
                    result.mctgId = mctgId
                    result.mctgNm = mctgNm
                    result.sctgId = sctgId
                    result.sctgNm = sctgNm
                } else if (ctgLv == "4") {
                    result.lctgId = lctgId
                    result.lctgNm = lctgNm
                    result.lctgId = lctgId
                    result.lctgNm = lctgNm
                    result.mctgId = mctgId
                    result.mctgNm = mctgNm
                    result.sctgId = sctgId
                    result.sctgNm = sctgNm
                    result.dctgId = dctgId
                    result.dctgNm = dctgNm
                }
            }
        }// 카테고리 타겟으로 실행하는 경우
        return result
    }
}