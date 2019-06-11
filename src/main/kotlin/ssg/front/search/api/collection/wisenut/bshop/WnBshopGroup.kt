package ssg.front.search.api.collection.wisenut.bshop

import QueryAPI510.Search
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.util.ResultUtils

class WnBshopGroup : WnBshopItem(), Groupable {

    override fun getAliasName(parameter: Parameter): String {
        return "group"
    }

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("BSHOPID_LST")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val strBshopInfo = search.w3GetMultiGroupByResult(name, "BSHOPID_LST") ?: ""
        if (strBshopInfo != "") {
            val bshopRstList = ResultUtils.getBshopGroup(strBshopInfo)
            result.bshopRstList = bshopRstList
        }
        return result
    }
}
