package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import com.google.common.base.Splitter
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable
import ssg.search.result.Store

class WnStore: WnCollection(), Pageable, Prefixable {

    override fun getName(parameter: Parameter): String {
        return "store"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "store"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("APL_TGT_MEDIA_CD", "STORE_NM", "IMG_FILE_NM", "LINK_URL", "BG_VAL", "CATEGORY_LST", "SITE_LST", "RPLC_SRCHWD_NM")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("STORE_NM", "RPLC_SRCHWD_NM")
    }

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0 ,40)
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        val prefix = StringBuilder()
        val strSiteNo = parameter.siteNo ?: ""
        val aplTgtMediaCd = parameter.aplTgtMediaCd ?: ""

        prefix.append("<SITE_LST:contains:").append(strSiteNo).append(">")
        prefix.append("<APL_TGT_MEDIA_CD:contains:00|").append(aplTgtMediaCd).append(">")

        return PrefixVo(prefix.toString(), 1)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val storeList = arrayListOf<Store>()
        var store: Store
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            store = Store()
            store.setStoreNm(search.w3GetField(name, "STORE_NM", i))
            store.setLinkUrl(search.w3GetField(name, "LINK_URL", i))
            store.setBgVal(search.w3GetField(name, "BG_VAL", i))
            store.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i))
            val cateList = ArrayList<Map<String, String>>()
            val ctgLst = search.w3GetField(name, "CATEGORY_LST", i)
            val iter = Splitter.on(",").trimResults().split(ctgLst).iterator()
            while (iter.hasNext()) {
                val ctgElement = iter.next()
                if (ctgElement != null && ctgElement != "") {
                    var j = 0
                    val cateMap = HashMap<String, String>()
                    val ctgToken = Splitter.on("^").trimResults().split(ctgElement!!).iterator()
                    while (ctgToken.hasNext()) {
                        val ctg = ctgToken.next()
                        if (ctg != null && ctg != "") {
                            if (j == 1) {
                                cateMap["cateNm"] = ctg
                            } else if (j == 2) {
                                cateMap["cateUrl"] = ctg
                            }
                        }
                        j++
                    }
                    cateList.add(cateMap)
                }
            }
            store.setCategoryList(cateList)
            storeList.add(store)
        }
        result.storeList = storeList
        result.storeCount = search.w3GetResultTotalCount(name)
        return result
    }
}
