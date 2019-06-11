package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.GroupVo

interface Groupable {
    fun groupVo(parameter: Parameter): GroupVo
}
