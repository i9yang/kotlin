package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import com.google.common.base.Splitter
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
import ssg.search.result.Magazine

class WnMagazine : WnCollection(), Pageable, Prefixable, Highlightable, Sortable {

    override fun getName(parameter: Parameter): String {
        return "magazine"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "magazine"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("DISP_CMPT_ID", "DISP_CMPT_NM", "HTML_CNTT", "IMG_FILE_NM", "SRCHWD_NM", "APL_TGT_MEDIA_CD", "MOBILE_DISPLAY_YN", "PNSHOP_STRT_DTS", "PNSHOP_END_DTS", "WRT_DATE")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("DISP_CMPT_ID", "DISP_CMPT_NM", "SRCHWD_NM", "HTML_CNTT")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()
        val userInfo = parameter.userInfo
        // B2C, B2E
        val type = userInfo.mbrType
        val coId = userInfo.mbrCoId
        if (type != null && coId != null) {
            if (type == "B2C") {
                prefix.append("<APL_B2C_LST:contains:").append(coId).append(">")
            } else if (type == "B2E") {
                prefix.append("<APL_B2E_LST:contains:B2E|").append(coId).append(">")
            }
        }
        //모바일,pc
        val aplTgtMediaCd = parameter.aplTgtMediaCd
        if (aplTgtMediaCd != null) {
            prefix.append("<APL_TGT_MEDIA_CD:contains:").append(aplTgtMediaCd).append(">")
        }
        return PrefixVo(prefix.toString(), 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        val strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0") {
            strCount = "20"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        val sortList = arrayListOf<Sort?>()
        sortList.add(Sorts.WRT_DATE.getSort(parameter))
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val magazineList = arrayListOf<Magazine>()
        var magazine: Magazine
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            magazine = Magazine()
            magazine.setDispCmptId(search.w3GetField(name, "DISP_CMPT_ID", i))
            magazine.setDispCmptNm(search.w3GetField(name, "DISP_CMPT_NM", i))

            val srchwdNm = search.w3GetField(name, "SRCHWD_NM", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>") ?: ""
            magazine.setSrchwdNm(srchwdNm)
            //srchwdNm여러개일 경우 tagList 로 내려줌
            if (srchwdNm != "" && srchwdNm.indexOf(",") > -1) {
                val tagList = ArrayList<String>()
                val tagIter = Splitter.on(",").trimResults().split(srchwdNm).iterator()
                while (tagIter.hasNext()) {
                    val srchwdNmTag = tagIter.next()
                    tagList.add(srchwdNmTag)
                }
                magazine.setMagazineTag(tagList)
            }

            var imgFileNm = search.w3GetField(name, "IMG_FILE_NM", i) ?: ""
            if (imgFileNm != "") imgFileNm = "http://static.ssgcdn.com$imgFileNm"
            magazine.setImgFileNm(imgFileNm)
            magazine.setHtmlCntt(search.w3GetField(name, "HTML_CNTT", i).replace("<!HS>".toRegex(), "<strong>").replace("<!HE>".toRegex(), "</strong>"))
            magazine.setAplTgtMediaCd(search.w3GetField(name, "APL_TGT_MEDIA_CD", i))
            magazine.setAplTgtMediaLst(search.w3GetField(name, "APL_TGT_MEDIA_LST", i))
            magazine.setMobileDisplayYn(search.w3GetField(name, "MOBILE_DISPLAY_YN", i))
            magazine.setPnshopStrtDts(search.w3GetField(name, "PNSHOP_STRT_DTS", i))
            magazine.setPnshopEndDts(search.w3GetField(name, "PNSHOP_END_DTS", i))
            magazine.setWrtDate(search.w3GetField(name, "WRT_DATE", i))
            magazineList.add(magazine)
        }
        result.magazineCount = search.w3GetResultTotalCount(name)
        result.MagazineList = magazineList
        return result
    }

}
