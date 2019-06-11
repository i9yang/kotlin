package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.SortVo

interface Sortable {
    fun sortVo(parameter: Parameter): SortVo
}