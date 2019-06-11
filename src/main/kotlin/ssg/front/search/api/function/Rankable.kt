package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.RankVo

interface Rankable {
    fun rankVo(parameter: Parameter): RankVo
}
