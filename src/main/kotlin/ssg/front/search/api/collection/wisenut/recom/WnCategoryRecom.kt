package ssg.front.search.api.collection.wisenut.recom

import QueryAPI510.Search
import org.slf4j.LoggerFactory
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.core.constants.Prefixes
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.PrefixVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Prefixable

class WnCategoryRecom: WnCollection(), Prefixable, Pageable {
    private val logger = LoggerFactory.getLogger(WnCategoryRecom::class.java)

    override fun getName(parameter: Parameter): String {
        return "categoryrecom"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "categoryrecom"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("DISP_CTG_LCLS_IDS", "DISP_CTG_LCLS_NMS", "DISP_CTG_MCLS_IDS", "DISP_CTG_MCLS_NMS", "DISP_CTG_SCLS_IDS", "DISP_CTG_SCLS_NMS")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("QUERY")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val recomDispCtgCategoryList = ArrayList<Map<String, String>>()
        if (search.w3GetResultTotalCount(name) > 0) {
            var ctgMap: MutableMap<String, String> = HashMap()
            val dispCtgLclsIds = search.w3GetField(name, "DISP_CTG_LCLS_IDS", 0)
            val dispCtgLclsNms = search.w3GetField(name, "DISP_CTG_LCLS_NMS", 0)
            val dispCtgMclsIds = search.w3GetField(name, "DISP_CTG_MCLS_IDS", 0)
            val dispCtgMclsNms = search.w3GetField(name, "DISP_CTG_MCLS_NMS", 0)
            val dispCtgSclsIds = search.w3GetField(name, "DISP_CTG_SCLS_IDS", 0)
            val dispCtgSclsNms = search.w3GetField(name, "DISP_CTG_SCLS_NMS", 0)
            // 여러개 존재 ( 2개까지 Set )
            if (dispCtgLclsIds.indexOf(",") > -1) {
                val dispCtgLclsId = dispCtgLclsIds.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val dispCtgLclsNm = dispCtgLclsNms.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val dispCtgMclsId = dispCtgMclsIds.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val dispCtgMclsNm = dispCtgMclsNms.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val dispCtgSclsId = dispCtgSclsIds.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val dispCtgSclsNm = dispCtgSclsNms.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                for (i in 0..1) {
                    ctgMap = HashMap()
                    ctgMap["DctgId"] = dispCtgLclsId[i]
                    ctgMap["DctgNm"] = dispCtgLclsNm[i]
                    ctgMap["MctgId"] = dispCtgMclsId[i]
                    ctgMap["MctgNm"] = dispCtgMclsNm[i]
                    ctgMap["SctgId"] = dispCtgSclsId[i]
                    ctgMap["SctgNm"] = dispCtgSclsNm[i]
                    recomDispCtgCategoryList.add(ctgMap)
                }
            } else {
                ctgMap["DctgId"] = dispCtgLclsIds
                ctgMap["DctgNm"] = dispCtgLclsNms
                ctgMap["MctgId"] = dispCtgMclsIds
                ctgMap["MctgNm"] = dispCtgMclsNms
                ctgMap["SctgId"] = dispCtgSclsIds
                ctgMap["SctgNm"] = dispCtgSclsNms
                recomDispCtgCategoryList.add(ctgMap)
            }// 한개만 있는 경우
        }
        if (recomDispCtgCategoryList != null && recomDispCtgCategoryList.size > 0) {
            result.recomDispCategoryList = recomDispCtgCategoryList
        }
        return result
    }

    override fun prefixVo(parameter: Parameter): PrefixVo {
        return PrefixVo(Prefixes.SITE_NO_ONLY.getPrefix(parameter), 1)
    }

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0, 1)
    }
}
