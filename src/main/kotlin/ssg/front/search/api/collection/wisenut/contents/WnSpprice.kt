package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.core.constants.Sorts
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.BoostVo
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Boostable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Spprice

class WnSpprice: WnCollection(), Pageable, Prefixable, Boostable, Sortable {

    override fun getName(parameter: Parameter): String {
        return "spprice"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "spprice"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ITEM_ID", "ITEM_NM", "BRAND_NM", "SP_TYPE", "SSG_DISP_YN")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "BRAND_NM")
    }

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0, 40)
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()
        val strSiteNo = parameter.siteNo ?: ""
        //SpType :10-오반장, 20-해바
        //SSG에서는 모두 노출
        if (strSiteNo == "6005") {
            prefix.append("<SITE_NO:contains:6001|6002|6003|6004|6009>")
            prefix.append("<SSG_DISP_YN:contains:Y>")
        } else if (strSiteNo == "6004") {
            prefix.append("<SITE_NO:contains:6004|6009>")
            prefix.append("<SP_TYPE:contains:20|30>")
        } else if (strSiteNo == "6001") {
            prefix.append("<SITE_NO:contains:6001|6002>")
            prefix.append("<SP_TYPE:contains:10|30>")
        } else if (strSiteNo == "6002") {      //@ BOOT개발시확인
            prefix.append("<SITE_NO:contains:").append(strSiteNo).append(">")
            prefix.append("<SP_TYPE:contains:10|30>")
        } else if (strSiteNo == "6009") {
            prefix.append("<SITE_NO:contains:").append(strSiteNo).append(">")
            prefix.append("<SP_TYPE:contains:20|30>")
        }
        prefix
                .append(Prefixes.DEVICE_CD.getPrefix(parameter))
                .append(Prefixes.MBR_CO_TYPE.getPrefix(parameter))

        return PrefixVo(prefix.toString(), 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val spList = arrayListOf<Spprice>()
        var spprice: Spprice
        val count = search.w3GetResultCount(name)
        val srchItemIds = StringBuilder()

        for (i in 0 until count) {
            spprice = Spprice()
            spprice.setSiteNo(search.w3GetField(name, "SITE_NO", i))
            spprice.setItemId(search.w3GetField(name, "ITEM_ID", i))
            spprice.setItemNm(search.w3GetField(name, "ITEM_NM", i))
            spprice.setSpType(search.w3GetField(name, "SP_TYPE", i))
            val ids = StringBuilder().append(spprice.getSiteNo()).append(":").append(spprice.getItemId()).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                if (spprice.getSpType() != "30") {
                    srchItemIds.append(ids)
                    spList.add(spprice)
                }
            }
        }

        //딜상품 추가하기 위해 한번 더 돌림(해바 +딜상품, 오반장 +딜상품 일 경우 해바, 오반장만 우선으로 보여줌)
        for (i in 0 until count) {
            spprice = Spprice()
            spprice.setSiteNo(search.w3GetField(name, "SITE_NO", i))
            spprice.setItemId(search.w3GetField(name, "ITEM_ID", i))
            spprice.setItemNm(search.w3GetField(name, "ITEM_NM", i))
            spprice.setSpType(search.w3GetField(name, "SP_TYPE", i))
            val ids = StringBuilder().append(spprice.getSiteNo()).append(":").append(spprice.getItemId()).append(",")
            if (srchItemIds.indexOf(ids.toString()) < 0) {
                if (spprice.getSpType() == "30") {
                    srchItemIds.append(ids)
                    spList.add(spprice)
                }
            }
        }

        result.sppriceList = spList
        result.sppriceItemIds = srchItemIds.toString() //가이드와 동일한 형식(itemId만 가지고 select)
        result.sppriceCount = spList.size
        return result
    }


    override fun boostVo(parameter: Parameter): BoostVo {
        return CollectionUtils.getCategoryBoosting(parameter)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sortList = arrayListOf<Sort?>()
        sortList.add(Sorts.WEIGHT.getSort(parameter))
        sortList.add(Sorts.RANK.getSort(parameter))
        var notNullList = sortList.filter { it != null } as ArrayList<Sort>
        return SortVo(notNullList)
    }
}