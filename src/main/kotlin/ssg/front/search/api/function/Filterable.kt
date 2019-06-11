package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.FilterVo

interface Filterable {
    fun filterVo(parameter: Parameter): FilterVo
}