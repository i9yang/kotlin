package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Pnshop

class WnPnshopSd : WnCollection(), Pageable, Prefixable {
    override fun getName(parameter: Parameter): String {
        return "pnshop"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "pnshop_sd"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ORI_SITE_NO", "DISP_CMPT_ID", "DISP_CMPT_NM", "IMG_FILE_NM1", "IMG_FILE_NM2", "IMG_FILE_NM3", "IMG_FILE_NM4", "MOD_DTS", "DISP_CMPT_TYPE_DTL_LST", "MOBILE_DISPLAY_YN", "MAI_TITLE_NM_1", "MAI_TITLE_NM_2", "SUBTITL_NM_1", "SUBTITL_NM_2", "OSMU_YN", "PNSHOP_TYPE_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("DISP_CMPT_NM", "SRCHWD_NM", "BRAND_LST")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()
        val targets = CollectionUtils.getTargets(parameter)

        prefix.append("<ORI_SITE_NO:contains:6009>")

        val strSiteNo = parameter.siteNo ?: ""
        // SHIN
        if (strSiteNo == "6004" || strSiteNo == "6009") {
            prefix.append("<SITE_NO:contains:6009>")
        } else if (strSiteNo == "6005") {
            prefix.append("<SITE_NO:contains:6005>")
        }// SSG

        // SET B2C, B2E
        prefix.append(Prefixes.MBR_CO_TYPE_CONTENTS.getPrefix(parameter))
        // SET DISP_CTG_LST
        prefix.append(Prefixes.DISP_CTG_LST.getPrefix(parameter))

        // 도서인 경우에는 BOOK_YN 사용한다.
        if (targets == Targets.BOOK || targets == Targets.MOBILE_BOOK) {
            prefix.append("<BOOK_YN:contains:Y>")
        }

        return PrefixVo(prefix.toString(), 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val pnshopSdList = arrayListOf<Pnshop>()
        var pnshop: Pnshop
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            pnshop = Pnshop()
            pnshop.setSiteNo(search.w3GetField(name, "SITE_NO", i))
            pnshop.setDispCmptId(search.w3GetField(name, "DISP_CMPT_ID", i))
            pnshop.setDispCmptNm(search.w3GetField(name, "DISP_CMPT_NM", i))
            pnshop.setDispCmptTypeDtlLst(search.w3GetField(name, "DISP_CMPT_TYPE_DTL_LST", i))

            if (parameter.target == "mobile_all" || parameter.target == "mobile_pnshop") {
                var imgFileNm = search.w3GetField(name, "IMG_FILE_NM1", i) ?: ""
                if (imgFileNm != "") imgFileNm = "http://static.ssgcdn.com$imgFileNm"
                pnshop.setImgFileNm1(imgFileNm)
            } else {
                pnshop.setImgFileNm1(search.w3GetField(name, "IMG_FILE_NM1", i))
            }
            pnshop.setImgFileNm2(search.w3GetField(name, "IMG_FILE_NM2", i))
            pnshop.setImgFileNm3(search.w3GetField(name, "IMG_FILE_NM3", i))
            pnshop.setImgFileNm4(search.w3GetField(name, "IMG_FILE_NM4", i))
            pnshop.setModDts(search.w3GetField(name, "MOD_DTS", i))
            pnshop.setMobileDisplayYn(search.w3GetField(name, "MOBILE_DISPLAY_YN", i))
            pnshop.setOriSiteNo(search.w3GetField(name, "ORI_SITE_NO", i))
            pnshop.setMaiTitleNm1(search.w3GetField(name, "MAI_TITLE_NM_1", i))
            pnshop.setMaiTitleNm2(search.w3GetField(name, "MAI_TITLE_NM_2", i))
            pnshop.setSubtitlNm1(search.w3GetField(name, "SUBTITL_NM_1", i))
            pnshop.setSubtitlNm2(search.w3GetField(name, "SUBTITL_NM_2", i))
            pnshop.setOsmuYn(search.w3GetField(name, "OSMU_YN", i))
            pnshop.setPnshopTypeCd(search.w3GetField(name, "PNSHOP_TYPE_CD", i))
            pnshopSdList.add(pnshop)
        }
        result.pnshopSdCount = search.w3GetResultTotalCount(name)
        result.pnshopSdList = pnshopSdList
        return result
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = "1"
        var strCount = "10"
        val strTarget = (parameter.target ?: "").toLowerCase()
        // PNSHOP TARGET 일때만 값을 세팅한다.
        if ((strTarget.startsWith("pnshop") || strTarget.startsWith("mobile_pnshop")) && strCount != "") {
            strCount = parameter.count ?: ""
            strPage = parameter.page ?: "1"
        } else if (strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "100"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }
}
