package ssg.front.search.api.collection.rsearch

import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.index.query.QueryBuilder
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

abstract class RecomCollection {
    open var indexName = ""

    abstract fun getQuery(parameter: Parameter): QueryBuilder
    abstract fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result)

    open fun getClassName(parameter: Parameter): String {
        return this::class.simpleName!!
    }
}