package ssg.front.search.api.util

import com.google.common.collect.Lists
import ssg.front.search.api.base.Info
import ssg.front.search.api.collection.Collection
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.BoostVo
import ssg.front.search.api.dto.vo.CategoryGroupVo
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.RankVo
import java.util.*

// TODO object class 제거 -> fun 자체 import 사용
object CollectionUtils {

    fun getDivPipe(): String {
        return "=================================================================================================================================================================================="
    }

    fun getTargets(parameter: Parameter): Targets {

        val strTarget = (parameter.target?: "all").toUpperCase()
//        var targets = Targets.ALL
        var targets = Targets.ES_BRAND_DISP
        try {
            targets = Targets.valueOf(strTarget)
        } catch (e: IllegalArgumentException) {
        }

        return targets
    }

    fun asList(vararg collections: Collection): List<Collection> {
        val returnList = Lists.newArrayList<Collection>()
        for (collection in collections) {
            returnList.add(collection)
        }
        return returnList
    }

    fun containsTarget(current: Targets, vararg expects: Targets): Boolean {
        for (expect in expects) {
            if (expect == current) {
                return true
            }
        }
        return false
    }

    fun replaceForced(strQuery: String): String {
        if (strQuery == "크린랲") {
            return "크린랩"
        } else if (strQuery == "케챱") {
            return "케찹"
        } else if (strQuery == "케잌") {
            return "케이크"
        } else if (strQuery == "랲") {
            return "랩"
        } else if (strQuery == "웻에이징") {
            return "wet에이징"
        } else if (strQuery == "똠양꿍" || strQuery == "똠양쿵" || strQuery == "똠얌쿵" || strQuery == "똠얌꿍") {
            return "톰얌쿵"
        }
        return strQuery
    }

    fun getCommonQuery(parameter: Parameter): String {
        var strQuery = parameter.query?: ""
        val strInclude = parameter.include?: ""
        val strExclude = parameter.exclude?: ""
        val strReplace = parameter.replaceQuery?: ""

        // 검색어 조합

        // 1. 치환키워드가 있을시 검색어는 치환키워드로
        if (strReplace != "") {
            strQuery = strReplace
        }
        // 2. 결과내 검색어 생성
        if (strInclude != "") {
            val sb = StringBuilder()
            sb.append(strQuery)
            val st = StringTokenizer(strInclude, "|")
            while (st.hasMoreTokens()) {
                val t = st.nextToken()
                sb.append(" ").append(t)
            }
            strQuery = sb.toString()
        }
        // 3. 제외 키워드 생성
        if (strExclude != "") {
            val sb = StringBuilder()
            sb.append(strQuery)
            val st = StringTokenizer(strExclude, "|")
            while (st.hasMoreTokens()) {
                val t = st.nextToken()
                sb.append(" !").append(t)
            }
            strQuery = sb.toString()
        }

        // 4. 태그를 사용한 검색어에서 #이 들어가면 오동작이 난다. # 제거
        if (strQuery.startsWith("#")) {
            strQuery = strQuery.replaceFirst("#".toRegex(), "")
        }

        return strQuery
    }

    fun getOriQuery(parameter: Parameter): String {
        val strReplace = parameter.replaceQuery?: ""
        var strOriQuery = parameter.query?: ""
        if (strReplace != "") {
            strOriQuery = strReplace
        }
        return strOriQuery.replace("\\p{Space}".toRegex(), "")
    }

    fun getCollectionRanking(parameter: Parameter): RankVo {
        val strSiteNo = parameter.siteNo?: "6005"
        return if (strSiteNo == "6005") {
            RankVo("6005" + "/" + getOriQuery(parameter))
        } else if (strSiteNo == "6004" || strSiteNo == "6009") {
            RankVo("6004" + "/" + CollectionUtils.getOriQuery(parameter))
        } else if (strSiteNo == "6001" || strSiteNo == "6002") {  //@ BOOT개발시확인
            RankVo("6001" + "/" + CollectionUtils.getOriQuery(parameter))
        } else {
            RankVo()
        }
    }

    fun getCategoryBoosting(parameter: Parameter): BoostVo {
        val strSiteNo = parameter.siteNo?: "6005"
        if (strSiteNo == "6005") {
            return BoostVo("SSGBOOST")
        } else if (strSiteNo == "6004" || strSiteNo == "6009") {
            return BoostVo("SHINBOOST")
        } else if (strSiteNo == "6001" || strSiteNo == "6002") {   //@ BOOT개발시확인
            return BoostVo("EMARTBOOST")
        }
        return BoostVo()
    }

    fun getPageInfo(strPage: String?, strCount: String?): PageVo {
        var start = 0
        var count = 0
        try {
            start = Integer.parseInt(strPage)
            count = Integer.parseInt(strCount)
            start = count * (start - 1)
        } catch (ne: NumberFormatException) {
        }

        return PageVo(start, count)
    }

    fun getCategoryNextLevel(parameter: Parameter): Int {
        if ((parameter.target.equals("all", ignoreCase = true) || parameter.target.equals("book", ignoreCase = true)) && parameter.ctgId == null && parameter.ctgLv == null) {
            return 0
        } else {
            val ctgLv = parameter.ctgLv?: "1"
            val ctgLast = parameter.ctgLast?: "N"
            if (ctgLv == "1") {
                return 3
            } else if (ctgLv == "2") {
                return if (ctgLast == "Y") {
                    0
                } else 4
            } else if (ctgLv == "3") {
                return if (ctgLast == "Y") {
                    4
                } else 0
            } else if (ctgLv == "4") {
                return 0
            }
        }
        return 0
    }

    fun getCategoryCurrentLevel(parameter: Parameter): Int {
        if ((parameter.target.equals("all", ignoreCase = true) || parameter.target.equals("book", ignoreCase = true)) && parameter.ctgId == null && parameter.ctgLv == null) {
            return 0
        } else {
            val ctgLv = parameter.ctgLv?: "1"
            val ctgLast = parameter.ctgLast?: "N"
            if (ctgLv == "1") {
                return 2
            } else if (ctgLv == "2") {
                return if (ctgLast == "Y") {
                    2
                } else 3
            } else if (ctgLv == "3") {
                return if (ctgLast == "Y") {
                    3
                } else 4
            } else if (ctgLv == "4") {
                return 4
            }
        }
        return 0
    }

    fun getCategoryGroupByLevel(parameter: Parameter): CategoryGroupVo {
        val strTarget = parameter.target
        val strSiteNo = parameter.siteNo
        var strCtgIdxNm = "DISP_CTG_IDX"
        if (strSiteNo == "6005") {
            strCtgIdxNm = "SCOM_DISP_CTG_IDX"
        } else if ((strSiteNo == "6001" || strSiteNo == "6002") && (strTarget.equals("all", ignoreCase = true) || strTarget.equals("category", ignoreCase = true))) {  //@ BOOT개발시확인
            if (parameter.ctgId == null) {
                strCtgIdxNm = "DISP_CTG_IDX,TEM_DISP_CTG_IDX"
            } else {
                if (parameter.themeYn == null || parameter.themeYn == "N") {
                    strCtgIdxNm = "DISP_CTG_IDX"
                } else {
                    strCtgIdxNm = "TEM_DISP_CTG_IDX"
                }
            }
        }
        if (
                (strTarget.equals("all", ignoreCase = true) || strTarget.equals("book", ignoreCase = true) || strTarget.equals("category", ignoreCase = true))
                && parameter.ctgId == null
                && parameter.ctgLv == null
        )
        {
            return CategoryGroupVo(strCtgIdxNm, "1,2,3")
        } else {
            val ctgLv = parameter.ctgLv?: "1"
            val ctgLast = parameter.ctgLast?: "N"
            if (ctgLv == "1") {
                return CategoryGroupVo(strCtgIdxNm, "2,3")
            } else if (ctgLv == "2") {
                return if (ctgLast == "Y") {
                    CategoryGroupVo(strCtgIdxNm, "2,3")
                } else CategoryGroupVo(strCtgIdxNm, "3,4")
            } else if (ctgLv == "3") {
                return if (ctgLast == "Y") {
                    CategoryGroupVo(strCtgIdxNm, "3,4")
                } else CategoryGroupVo(strCtgIdxNm, "4")
            } else if (ctgLv == "4") {
                return CategoryGroupVo(strCtgIdxNm, "4")
            }
        }
        return CategoryGroupVo(strCtgIdxNm, "1,2")
    }

    fun getCategoryGroupBy(parameter: Parameter): CategoryGroupVo {
        val strSiteNo = parameter.siteNo?: "6005"
        val ctgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "DISP_CTG_IDX"
        return CategoryGroupVo(ctgIdxNm, "1")
    }

    fun getMobileCategoryGroupBy(parameter: Parameter, maxlevel: Int): CategoryGroupVo {
        val strSiteNo = parameter.siteNo?: "6005"
        val ctgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "DISP_CTG_IDX"
        if (maxlevel == 4) {
            return CategoryGroupVo(ctgIdxNm, "1,2,3,4")
        } else if (maxlevel == 3) {
            return CategoryGroupVo(ctgIdxNm, "1,2,3")

        } else if (maxlevel == 2) {
            return CategoryGroupVo(ctgIdxNm, "1,2")

        } else if (maxlevel == 1) {
            return CategoryGroupVo(ctgIdxNm, "1")
        }
        return CategoryGroupVo(ctgIdxNm, "1,2,3,4")
    }

    fun getRecomCategoryGroupBy(parameter: Parameter): CategoryGroupVo {
        val strSiteNo = parameter.siteNo?: "6005"
        val ctgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "DISP_CTG_IDX"
        return CategoryGroupVo(ctgIdxNm, "3")
    }

    fun getGlobalCategoryGroupBy(parameter: Parameter): CategoryGroupVo {
        val strSiteNo = parameter.siteNo?: "6005"
        val ctgIdxNm = if (strSiteNo == "6005") "SCOM_DISP_CTG_IDX" else "TEM_DISP_CTG_IDX"
        val ctgLv = parameter.ctgLv?: "0"
        val ctgLast = parameter.ctgLast?: "N"
        var lev = "1"

        if (ctgLv == "1") lev = "1,2,3"
        if (ctgLv == "2") {
            lev = "1,2,3"
        }
        if (ctgLv == "3") {
            if (ctgLast == "Y") {
                lev = "3"
            } else
                lev = "3,4"
        }
        if (ctgLv == "4") {
            lev = "4"
        } else
            lev = "1,2,3"

        return CategoryGroupVo(ctgIdxNm, lev)
    }

    fun getShppPrefix(parameter: Parameter): String {
        var sb = StringBuilder()
        val strShpp = parameter.shpp?: ""
        val strPickuSalestr = parameter.pickuSalestr?: ""
        if (strShpp != "") {
            // 여러개 토큰 처리 ( 매직픽업 퀵배송 제외 )
            if (strShpp.indexOf("|") > -1) {
                sb.append("<SHPP:contains:")
                val s = strShpp.split("\\|".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in s.indices) {
                    val s1 = s[i].toUpperCase()
                    if (s1 != "QSHPP" && s1 != "PICKU") {
                        sb.append(s1).append("|")
                    }
                }
                // 아무것도 매핑안된 경우 를 제외 ( 파라메터에 매직픽업, 퀵배송 만 들어가 있는 경우 회피 )
                if (sb.toString() != "<SHPP:contains:") {
                    sb.deleteCharAt(sb.lastIndexOf("|")).append(">")
                } else {
                    sb = StringBuilder()
                }
            } else if (strShpp.toUpperCase().indexOf("QSHPP") < 0 && strShpp.toUpperCase().indexOf("PICKU") < 0) {
                sb.append("<SHPP:contains:").append(strShpp.toUpperCase()).append(">")
            }// 한개 토큰 중 매직픽업이나 퀵배송이 없을 시
            // 무조건 매직픽업 퀵배송은 따로 AND 로 처리한다.
            if (strShpp.toUpperCase().indexOf("QSHPP") > -1) {
                sb.append("<SHPP:contains:QSHPP>")
            }
            if (strShpp.toUpperCase().indexOf("PICKU") > -1) {
                // 매직픽업 점포가 있을 시
                if (strPickuSalestr != "") {
                    sb.append("<SHPP:contains:PICKU$strPickuSalestr>")
                } else {
                    sb.append("<SHPP:contains:PICKU>")
                }
            }
        }
        return sb.toString()
    }

    fun getAdPageInfo(strPage: String, strCount: String, adCount: Int): Info {
        var start = 0
        var count = 0
        try {
            start = Integer.parseInt(strPage)
            count = Integer.parseInt(strCount)
            start = count * (start - 1)

            if (start > adCount && count > adCount && adCount > 0) {
                start = start - adCount
                count = count + adCount
            }

        } catch (ne: NumberFormatException) {
        }

        return Info(start, count)
    }

    fun isAdSearch(parameter: Parameter): Boolean {
        val strSiteNo = parameter.siteNo?: "6005"
        val strFilterSiteNo = parameter.filterSiteNo?: ""
        val loadLevel = parameter.loadLevel?: "10"
        var isAdSearch = false

        if ("Y" == parameter.adYn
                && (strSiteNo == "6004" || strSiteNo == "6009"
                        || strSiteNo == "6001" && ("" == strFilterSiteNo || "6001" == strFilterSiteNo)
                        || strSiteNo == "6002" && ("" == strFilterSiteNo || "6001" == strFilterSiteNo)
                        || strSiteNo == "6005" && ("" == strFilterSiteNo || "6001" == strFilterSiteNo || "6004" == strFilterSiteNo))
                && (parameter.brand == null && parameter.benefit == null && (parameter.cls == null
                        && parameter.clsFilter == null && parameter.ctgId == null && parameter.size == null
                        && parameter.minPrc == null && parameter.maxPrc == null && parameter.shpp == null
                        && parameter.sort == null || parameter.sort.equals("best", true)))
                && (parameter.LOAD_LEVEL_GENERAL == loadLevel
                        || parameter.LOAD_LEVEL_WARNING == loadLevel
                        || parameter.LOAD_LEVEL_LIMIT == loadLevel
                    )
        ) {
            isAdSearch = true
        }

        return isAdSearch
    }

    fun isRecommendSearch(parameter: Parameter): Boolean {
        val strSiteNo = parameter.siteNo?: "6005"
        val loadLevel = parameter.loadLevel?: "10"
        var isRecommendSearch = false

        if ("Y" == parameter.recommendYn
                && (strSiteNo == "6004" || strSiteNo == "6001" || strSiteNo == "6004" || strSiteNo == "6009" || strSiteNo == "6005")
                && (parameter.LOAD_LEVEL_GENERAL == loadLevel
                        || parameter.LOAD_LEVEL_WARNING == loadLevel
                        || parameter.LOAD_LEVEL_LIMIT == loadLevel)
        ) {
            isRecommendSearch = true
        }

        return isRecommendSearch
    }
}
