package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Starfield

class WnStarfield: WnCollection(), Pageable, Prefixable {

    override fun getName(parameter: Parameter): String {
        return "starfield"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "starfield"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("OFFLINE_STR_ID", "OFFLINE_STR_NM", "OFFLINE_BLDG_ID", "OFFLINE_FLO_ID", "OFFLINE_FLO_NM", "OFFLINE_SHOP_ID", "OFFLINE_SHOP_NM", "OFFLINE_SHOP_DESC", "OFFLINE_SHOP_STAT_CD", "OFFLINE_SHOP_TELNO1", "OFFLINE_SHOP_TELNO2", "OFFLINE_SHOP_TAG_CNTT", "PC_IMG_URL", "MOBIL_IMG_URL", "BRNLG_IMG_URL", "BRNLG_IMG_URL1", "BRNLG_IMG_URL2", "BRNLG_APP_IMG_URL", "SF_SPCL_SHOP_URL", "WDAY_STRT_HM", "WDAY_END_HM", "HOLI_STRT_HM", "HOLI_END_HM", "SF_STR_LCLS_CTG_ID", "SF_STR_LCLS_CTG_NM", "SF_STR_MCLS_CTG_ID", "SF_STR_MCLS_CTG_NM", "SF_PST_XCOR_VAL", "SF_PST_YCOR_VAL", "SF_PST_POI_LVL", "PST_DISP_YN", "PST_USE_YN", "BRAND_ID", "SPCSHOP_URL")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("OFFLINE_SHOP_ID", "OFFLINE_STR_NM", "OFFLINE_SHOP_NM", "OFFLINE_SHOP_DESC", "OFFLINE_SHOP_TAG_CNTT", "SF_STR_LCLS_CTG_NM", "SF_STR_MCLS_CTG_NM")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val starfieldList = ArrayList<Starfield>()
        var starfield: Starfield
        val count = search.w3GetResultCount(name)

        for (i in 0 until count) {
            starfield = Starfield()

            starfield.setOfflineStrId(search.w3GetField(name, "OFFLINE_STR_ID", i))
            starfield.setOfflineStrNm(search.w3GetField(name, "OFFLINE_STR_NM", i))
            starfield.setOfflineBldgId(search.w3GetField(name, "OFFLINE_BLDG_ID", i))
            starfield.setOfflineFloId(search.w3GetField(name, "OFFLINE_FLO_ID", i))
            starfield.setOfflineFloNm(search.w3GetField(name, "OFFLINE_FLO_NM", i))
            starfield.setOfflineShopId(search.w3GetField(name, "OFFLINE_SHOP_ID", i))
            starfield.setOfflineShopNm(search.w3GetField(name, "OFFLINE_SHOP_NM", i))
            starfield.setOfflineShopDesc(search.w3GetField(name, "OFFLINE_SHOP_DESC", i))
            starfield.setOfflineShopStatCd(search.w3GetField(name, "OFFLINE_SHOP_STAT_CD", i))
            starfield.setOfflineShopTelNo1(search.w3GetField(name, "OFFLINE_SHOP_TELNO1", i))
            starfield.setOfflineShopTelNo2(search.w3GetField(name, "OFFLINE_SHOP_TELNO2", i))
            starfield.setOfflineShopTagCntt(search.w3GetField(name, "OFFLINE_SHOP_TAG_CNTT", i))
            starfield.setPcImgUrl(search.w3GetField(name, "PC_IMG_URL", i))
            starfield.setMobilImgUrl(search.w3GetField(name, "MOBIL_IMG_URL", i))
            starfield.setBrnlgImgUrl(search.w3GetField(name, "BRNLG_IMG_URL", i))
            starfield.setBrnlgImgUrl1(search.w3GetField(name, "BRNLG_IMG_URL1", i))
            starfield.setBrnlgImgUrl2(search.w3GetField(name, "BRNLG_IMG_URL2", i))
            starfield.setBrnlgAppImgUrl(search.w3GetField(name, "BRNLG_APP_IMG_URL", i))
            starfield.setSfSpclShopUrl(search.w3GetField(name, "SF_SPCL_SHOP_URL", i))
            starfield.setWdayStrtHm(search.w3GetField(name, "WDAY_STRT_HM", i))
            starfield.setWdayEndHm(search.w3GetField(name, "WDAY_END_HM", i))
            starfield.setHoliStrtHm(search.w3GetField(name, "HOLI_STRT_HM", i))
            starfield.setHoliEndHm(search.w3GetField(name, "HOLI_END_HM", i))
            starfield.setSfStrLclsCtgId(search.w3GetField(name, "SF_STR_LCLS_CTG_ID", i))
            starfield.setSfStrLclsCtgNm(search.w3GetField(name, "SF_STR_LCLS_CTG_NM", i))
            starfield.setSfStrMclsCtgId(search.w3GetField(name, "SF_STR_MCLS_CTG_ID", i))
            starfield.setSfStrMclsCtgNm(search.w3GetField(name, "SF_STR_MCLS_CTG_NM", i))
            starfield.setSfPstXcorVal(search.w3GetField(name, "SF_PST_XCOR_VAL", i))
            starfield.setSfPstYcorVal(search.w3GetField(name, "SF_PST_YCOR_VAL", i))
            starfield.setSfPstPoiLvl(search.w3GetField(name, "SF_PST_POI_LVL", i))
            starfield.setPstDispYn(search.w3GetField(name, "PST_DISP_YN", i))
            starfield.setPstUseYn(search.w3GetField(name, "PST_USE_YN", i))
            starfield.setBrandId(search.w3GetField(name, "BRAND_ID", i))
            starfield.setSpcshopUrl(search.w3GetField(name, "SPCSHOP_URL", i))
            starfieldList.add(starfield)
        }
        result.starfieldCount = search.w3GetResultTotalCount(name)
        result.starfieldList = starfieldList
        return result
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: "80"
        val strTarget = (parameter.target ?: "").toLowerCase()
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0" || strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "80"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()

        val offlineStrId = parameter.offlineStrId
        val offlineBldgId = parameter.offlineBldgId
        val offlineFloId = parameter.offlineFloId
        val sfStrLclsCtgId = parameter.sfStrLclsCtgId
        val sfStrMclsCtgId = parameter.sfStrMclsCtgId

        if (offlineStrId.isNullOrBlank()) prefix.append("<OFFLINE_STR_ID:contains:").append(offlineStrId).append(">")
        if (offlineBldgId.isNullOrBlank()) prefix.append("<OFFLINE_BLDG_ID:contains:").append(offlineBldgId).append(">")
        if (offlineFloId.isNullOrBlank()) prefix.append("<OFFLINE_FLO_ID:contains:").append(offlineFloId).append(">")
        if (sfStrLclsCtgId.isNullOrBlank()) prefix.append("<SF_STR_LCLS_CTG_ID:contains:").append(sfStrLclsCtgId).append(">")
        if (sfStrMclsCtgId.isNullOrBlank()) prefix.append("<SF_STR_MCLS_CTG_ID:contains:").append(sfStrMclsCtgId).append(">")

        return PrefixVo(prefix.toString(), 1)
    }
}
