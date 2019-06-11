package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.CategoryGroupVo

interface CategoryGroupable {
    fun categoryGroupVo(parameter: Parameter): CategoryGroupVo
}
