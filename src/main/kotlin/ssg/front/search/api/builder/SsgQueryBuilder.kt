package ssg.front.search.api.builder

import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

interface SsgQueryBuilder {
    fun execute(parameter: Parameter, result: Result)
}