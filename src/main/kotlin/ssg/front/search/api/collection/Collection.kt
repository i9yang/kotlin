package ssg.front.search.api.collection

import QueryAPI510.Search
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

interface Collection {
    fun getName(parameter: Parameter): String?
    fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result
}