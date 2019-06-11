package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.PrefixVo

interface Prefixable {
    fun prefixVo(parameter: Parameter): PrefixVo
}
