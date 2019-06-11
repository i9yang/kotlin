package ssg.front.search.api.collection.es.book

import org.elasticsearch.action.search.SearchResponse
import ssg.front.search.api.collection.es.common.EsItem
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

class EsBookItem : EsItem() {
    override var indexName = "book"

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        var (itemList, srchItemIds) = super.getItemResultPair(searchResponse, parameter)

        result.bookItemList = itemList
        result.bookCount = searchResponse.hits.totalHits.toInt()
        result.bookItemIds = srchItemIds
    }
}