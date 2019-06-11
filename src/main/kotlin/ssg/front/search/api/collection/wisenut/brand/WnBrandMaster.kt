package ssg.front.search.api.collection.wisenut.brand

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.BrandMaster

class WnBrandMaster: WnCollection(), Prefixable, Pageable {

    override fun getName(parameter: Parameter): String {
        return "brandmaster"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "brandmaster"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("BRAND_ID", "BRAND_NM", "BRAND_SRCHWD_NM", "BRAND_DESC", "BRAND_CNCEP_CNTT", "USE_YN", "BRAND_SYNC_YN", "BRAND_REG_MAIN_DIV_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("BRAND_ID", "BRAND_NM", "BRAND_SRCHWD_NM")
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        var sb = ""
        setOf(
                Prefixes.USE_YN_PREFIX
        ).forEach {
            sb += it.getPrefix(parameter)
        }
        return PrefixVo(sb, 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        val strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0") {
            strCount = "5"
        }
        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val brandMasterList = arrayListOf<BrandMaster>()
        var brandMaster: BrandMaster
        val count = search.w3GetResultCount(name)
        //브랜드필터 노출여부 판단을위해 추가(검색어와 같은 브랜드명 있을경우 미노출 처리)
        var brandFilterDispYn = "Y"
        for (i in 0 until count) {
            brandMaster = BrandMaster()
            brandMaster.setBrandId(search.w3GetField(name, "BRAND_ID", i))
            brandMaster.setBrandNm(search.w3GetField(name, "BRAND_NM", i))
            brandMaster.setBrandSrchwdNm(search.w3GetField(name, "BRAND_SRCHWD_NM", i))
            brandMaster.setBrandCncepCntt(search.w3GetField(name, "BRAND_CNCEP_CNTT", i))
            brandMaster.setBrandDesc(search.w3GetField(name, "BRAND_DESC", i))
            brandMaster.setUseYn(search.w3GetField(name, "USE_YN", i))
            brandMaster.setBrandSyncYn(search.w3GetField(name, "BRAND_SYNC_YN", i))
            brandMaster.setBrandRegMainDivCd(search.w3GetField(name, "BRAND_REG_MAIN_DIV_CD", i))
            brandMasterList.add(brandMaster)

            if ((parameter.query ?: "") == brandMaster.getBrandNm()) {
                brandFilterDispYn = "N"
            }
        }
        result.brandMasterList = brandMasterList
        result.brandMasterCount = search.w3GetResultTotalCount(name)
        result.brandFilterDispYn = brandFilterDispYn
        return result
    }
}
