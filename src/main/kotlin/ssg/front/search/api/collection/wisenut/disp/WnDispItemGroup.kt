package ssg.front.search.api.collection.wisenut.disp

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.base.WnDispGroup
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.GroupVo
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.util.ResultUtils

class WnDispItemGroup : WnDispGroup(), Groupable {

    override fun groupVo(parameter: Parameter): GroupVo {
        return GroupVo("SIZE_LST")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        // DISP_GROUP은 전시카테고리 전체 사이즈 그룹핑 결과를 사용
        val strSizeInfo = search.w3GetMultiGroupByResult(name, "SIZE_LST") ?: ""
        if (strSizeInfo != "") {
            val sizeList = ResultUtils.getSizeGroup(strSizeInfo)
            result.sizeList = sizeList
        }
        return result
    }
}