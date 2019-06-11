package ssg.front.search.api.collection.wisenut.search

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.*
import ssg.front.search.api.function.*
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Postng
import java.util.*

class WnPostng: WnCollection(), Prefixable, Pageable, Rankable, Boostable, Sortable {

    override fun getName(parameter: Parameter): String {
        return "postng"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "postng"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ITEM_ID", "ITEM_NM", "POSTNG_ID", "POSTNG_TITLE_NM", "POSTNG_EVAL_SCR", "POSTNG_WRTPE_IDNF_ID", "SELLPRC", "ITEM_REG_DIV_CD", "SALESTR_LST")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "BRAND_NM", "TAG_NM", "GNRL_STD_DISP_CTG")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.SRCH_PREFIX,
                Prefixes.FILTER_SITE_NO,
                Prefixes.SALESTR_LST,
                Prefixes.DEVICE_CD,
                Prefixes.SPL_VEN_ID,
                Prefixes.LRNK_SPL_VEN_ID,
                Prefixes.MBR_CO_TYPE
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb,1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = "1"
        var strCount = parameter.count ?: ""
        val targets = CollectionUtils.getTargets(parameter)
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (targets == Targets.ALL) {
            strCount = "9"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun rankVo(parameter: Parameter): RankVo {
        return CollectionUtils.getCollectionRanking(parameter)
    }

    override fun boostVo(parameter: Parameter): BoostVo {
        return CollectionUtils.getCategoryBoosting(parameter)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sorts = Sorts.BEST
        val strSort = parameter.sort ?: "best"
        val sortList = arrayListOf<Sort?>()
        try {
            sorts = Sorts.valueOf(strSort.toUpperCase())
        } catch (e: IllegalArgumentException) {
        }

        if (sorts == Sorts.BEST) {
            sortList.add(Sorts.WEIGHT.getSort(parameter))
            sortList.add(Sorts.RANK.getSort(parameter))
            sortList.add(Sorts.THRD.getSort(parameter))
            sortList.add(Sorts.REGDT.getSort(parameter))
            sortList.add(Sorts.PRCASC.getSort(parameter))
        } else {
            sortList.add(sorts.getSort(parameter))
        }
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val count = search.w3GetResultCount(name)
        val srchItemIds = StringBuilder(result.srchItemIds)
        val postngList = ArrayList<Postng>()
        var postng: Postng
        for (i in 0 until count) {
            postng = Postng()
            val strItemId = search.w3GetField(name, "ITEM_ID", i)
            val strSiteNo = search.w3GetField(name, "SITE_NO", i)
            val strItemNm = search.w3GetField(name, "ITEM_NM", i)
            val strPostngId = search.w3GetField(name, "POSTNG_ID", i)
            val strPostngNm = search.w3GetField(name, "POSTNG_TITLE_NM", i)
            val strPostngEvalScr = search.w3GetField(name, "POSTNG_EVAL_SCR", i)
            val strPostngWrtpeIdnfId = search.w3GetField(name, "POSTNG_WRTPE_IDNF_ID", i)
            var strSalestrLst = search.w3GetField(name, "SALESTR_LST", i)
            var strSalestrNo = ""
            val itemRegDivCd = search.w3GetField(name, "ITEM_REG_DIV_CD", i)
            val sellprc = search.w3GetField(name, "SELLPRC", i)

            postng.setSiteNo(strSiteNo)
            postng.setItemId(strItemId)
            postng.setItemNm(strItemNm)
            postng.setPostngId(strPostngId)
            postng.setPostngTitleNm(strPostngNm)
            postng.setPostngEvalScr(strPostngEvalScr)
            postng.setPostngWrtpeIdnfId(strPostngWrtpeIdnfId)

            postng.setSellprc(sellprc)

            // 상품의 이빨이 빠졌을 때 점포값이 필수인 것들이 있기 때문에 강제로  점포 매핑작업을 한다.
            val userInfo = parameter.userInfo
            if (strSiteNo == "6009" && itemRegDivCd == "30") {
                if (strSalestrLst.indexOf("0001") > -1) {
                    var idx = 0
                    strSalestrLst = strSalestrLst.replace("0001,", "").trim({ it <= ' ' })
                    strSalestrLst = strSalestrLst.replace("0001", "").trim({ it <= ' ' })
                    val st = StringTokenizer(strSalestrLst, " ")
                    while (st.hasMoreTokens()) {
                        if (idx > 1) break
                        strSalestrNo = st.nextToken().replace("D", "")
                        idx++
                    }
                } else {
                    strSalestrNo = strSalestrLst.replace("\\p{Space}".toRegex(), "").replace("D", "")
                    strSalestrNo = strSalestrLst.replace("\\p{Space}".toRegex(), "").replace("Y", "")    //백화점쓱배송점포처리
                }
            } else if (strSiteNo == "6001" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.emSaleStrNo
            } else if (strSiteNo == "6002" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.trSaleStrNo
            } else if (strSiteNo == "6003" && itemRegDivCd == "20") {
                strSalestrNo = userInfo.bnSaleStrNo
            } else {
                strSalestrNo = "6005"
            }// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
            postng.setSalestrNo(strSalestrNo)

            val ids = StringBuilder().append(strSiteNo).append(":").append(strItemId).append(":").append(strSalestrNo).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                srchItemIds.append(ids)
            }
            postngList.add(postng)
        }
        // POSTNG LIST
        result.postngList = postngList
        result.postngCount = search.w3GetResultTotalCount(name)
        result.srchItemIds = srchItemIds.toString()
        return result
    }
}
