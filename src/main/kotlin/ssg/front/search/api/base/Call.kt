package ssg.front.search.api.base

import ssg.front.search.api.dto.Parameter

interface Call<T> {
    fun apply(parameter: Parameter): Info
}
