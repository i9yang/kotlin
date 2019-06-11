package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.MorphVo

interface Morphable {
    fun morphVo(parameter: Parameter): MorphVo
}