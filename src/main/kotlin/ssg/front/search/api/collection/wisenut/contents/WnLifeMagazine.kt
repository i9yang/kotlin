package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Highlightable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.ShoppingMagazine

class WnLifeMagazine: WnCollection(), Pageable, Prefixable, Highlightable, Sortable {

    override fun getName(parameter: Parameter): String {
        return "shoppingmagazine"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "lifemagazine"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SHPG_MGZ_ID", "SHPG_MGZ_NM", "SRCHWD", "IMG_FILE_NM", "MAI_TITLE_NM_1", "MAI_TITLE_NM_2", "BANR_DESC", "DISP_STRT_DTS", "LNKD_URL", "SHPG_CONTENTS_CD", "SHPG_CONTENTS_NM", "SHPG_CATEGORY_CD", "SHPG_CATEGORY_NM", "SHPG_MGZ_TYPE_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("SHPG_MGZ_ID", "MAI_TITLE_NM_1", "MAI_TITLE_NM_2", "BANR_DESC")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()

        //접속 사이트
        val siteNo = parameter.siteNo
        if (siteNo.isNullOrBlank()) {
            if (siteNo != "6100") {
                prefix.append("<APL_SITE_NO_LST:contains:").append(siteNo).append("|6100").append(">")
            } else {
                prefix.append("<APL_SITE_NO_LST:contains:").append(siteNo).append(">")
            }
        }

        //접속 미디어 체크 10 : PC, 20 : 모바일 - 웹과 앱을 따로 구분하지 않는다.
        val aplTgtMediaCd = parameter.aplTgtMediaCd
        if (aplTgtMediaCd.isNullOrBlank()) {
            prefix.append("<APL_MEDIA_CD:contains:").append(aplTgtMediaCd).append(">")
        }

        //쇼핑매거진 유형 코드 D324 10:매거진, 20:하우디 저널
        //pc만 (매거진+하우디) 보여줌, 하우디 검색에서는 하우디만
        val shpgMgzTypeCd = parameter.shpgMgzTypeCd ?: "10"
        if (aplTgtMediaCd != "10" || siteNo == "6100") {
            prefix.append("<SHPG_MGZ_TYPE_CD:contains:").append(shpgMgzTypeCd).append(">")
        }

        return PrefixVo(prefix.toString(), 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        val strTarget = (parameter.target ?: "").toLowerCase()
        val shpgMgzTypeCd = parameter.shpgMgzTypeCd ?: "10"
        var strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""

        if (shpgMgzTypeCd == "20") {
            //하우디는 무조건 전체 내려줌
            strCount = "100"
            strPage = "1"
        } else if (strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "100"
        } else {
            // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
            if (strCount == "" || strCount == "0") {
                strCount = "20"
            }
        }

        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        val sortList = arrayListOf<Sort?>()
        sortList.add(Sorts.DISP_STRT_DTS.getSort(parameter))
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val shoppingMagazineList = ArrayList<ShoppingMagazine>()
        var shoppingMagazine: ShoppingMagazine
        val count = search.w3GetResultCount(name)

        for (i in 0 until count) {
            shoppingMagazine = ShoppingMagazine()

            shoppingMagazine.setShpgMgzId(search.w3GetField(name, "SHPG_MGZ_ID", i))
            shoppingMagazine.setShpgMgzNm(search.w3GetField(name, "SHPG_MGZ_NM", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            shoppingMagazine.setShpgMgzTypeCd(search.w3GetField(name, "SHPG_MGZ_TYPE_CD", i))
            shoppingMagazine.setSrchwd(search.w3GetField(name, "SRCHWD", i))
            shoppingMagazine.setDispStrtDts(search.w3GetField(name, "DISP_STRT_DTS", i))
            shoppingMagazine.setDispEndDts(search.w3GetField(name, "DISP_END_DTS", i))
            shoppingMagazine.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i))
            shoppingMagazine.setMaiTitleNm1(search.w3GetField(name, "MAI_TITLE_NM_1", i))
            shoppingMagazine.setMaiTitleNm2(search.w3GetField(name, "MAI_TITLE_NM_2", i))
            shoppingMagazine.setLnkdUrl(search.w3GetField(name, "LNKD_URL", i))
            shoppingMagazine.setBanrDesc(search.w3GetField(name, "BANR_DESC", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            shoppingMagazine.setAplSiteNoLst(search.w3GetField(name, "APL_SITE_NO_LST", i))
            shoppingMagazine.setShpgContentsCd(search.w3GetField(name, "SHPG_CONTENTS_CD", i))
            shoppingMagazine.setShpgContentsNm(search.w3GetField(name, "SHPG_CONTENTS_NM", i))
            shoppingMagazine.setShpgCategoryCd(search.w3GetField(name, "SHPG_CATEGORY_CD", i))
            shoppingMagazine.setShpgCategoryNm(search.w3GetField(name, "SHPG_CATEGORY_NM", i))

            shoppingMagazineList.add(shoppingMagazine)


        }
        result.lifeMagazineCount = search.w3GetResultTotalCount(name)
        result.lifeMagazineList = shoppingMagazineList
        return result
    }

}
