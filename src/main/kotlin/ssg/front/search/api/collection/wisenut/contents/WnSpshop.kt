package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.search.result.SpShop

class WnSpshop: WnCollection() {

    override fun getName(parameter: Parameter): String {
        return "spshop"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "spshop"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("SITE_NO", "ITEM_ID", "ITEM_NM", "SELLPRC")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("ITEM_ID", "ITEM_NM", "ITEM_SRCHWD_NM", "MDL_NM", "BRAND_NM", "GNRL_STD_DISP_CTG")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val spShopllList = arrayListOf<SpShop>()
        var spShop: SpShop
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            spShop = SpShop()
            spShop.setSiteNo(search.w3GetField(name, "SITE_NO", i))
            spShop.setItemId(search.w3GetField(name, "ITEM_ID", i))
            spShop.setItemNm(search.w3GetField(name, "ITEM_NM", i))
            spShop.setSellprc(search.w3GetField(name, "SELLPRC", i))
            spShopllList.add(spShop)
        }
        result.spShopList = spShopllList
        result.spShopCount = search.w3GetResultTotalCount(name)
        return result
    }

}
