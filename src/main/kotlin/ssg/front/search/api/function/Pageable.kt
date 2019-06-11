package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.PageVo

interface Pageable {
    fun pageVo(parameter: Parameter): PageVo
}
