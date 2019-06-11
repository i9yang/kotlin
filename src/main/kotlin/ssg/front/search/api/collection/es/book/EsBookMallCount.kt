package ssg.front.search.api.collection.es.book

import org.elasticsearch.action.search.SearchResponse
import ssg.front.search.api.collection.es.common.EsMallCount
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

class EsBookMallCount : EsMallCount() {
    override var indexName = "book"

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        result.bookMallCountMap = super.getMallCountMap(searchResponse)
    }
}