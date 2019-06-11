package ssg.front.search.api.collection.es

import org.elasticsearch.action.search.SearchResponse
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

abstract class EsCollection {
    open var indexName = "item"

    abstract fun getQuery(parameter: Parameter): List<String>
    abstract fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result)

    open fun getClassName(parameter: Parameter): String {
        return this::class.simpleName!!
    }
}