package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.BoostVo

interface Boostable {
    fun boostVo(parameter: Parameter): BoostVo
}