package ssg.front.search.api.collection.ad

import org.apache.http.NameValuePair
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

abstract class AdCollection {
    abstract fun getUrlPath(parameter: Parameter): String
    abstract fun getQuery(parameter: Parameter): List<NameValuePair>
    abstract fun getResult(searchResult: String, name: String, parameter: Parameter, result: Result)

    open fun getClassName(parameter: Parameter): String {
        return this::class.simpleName!!
    }
}