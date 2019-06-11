package ssg.front.search.api.function

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.vo.PropertyGroupVo

interface PropertyGroupable {
    fun propertyGroupVo(parameter: Parameter): PropertyGroupVo
}
