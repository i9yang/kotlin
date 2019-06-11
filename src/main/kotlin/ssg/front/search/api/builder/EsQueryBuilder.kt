package ssg.front.search.api.builder

import org.elasticsearch.action.search.MultiSearchRequest
import org.elasticsearch.action.search.MultiSearchResponse
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import ssg.front.search.api.collection.es.EsCollection
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.function.Groupable
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Sortable

@Component
class EsQueryBuilder : SsgQueryBuilder {
    private val logger = LoggerFactory.getLogger(EsQueryBuilder::class.java)

    @Autowired
    @Qualifier("esDispRestClient")
    lateinit var client: RestHighLevelClient

    override fun execute(parameter: Parameter, result: Result) {
        var requests = MultiSearchRequest()

        Targets.valueOf(parameter.target.toUpperCase())
                .getCollectionSet(parameter)
                .filterIsInstance<EsCollection>()
                .forEach { collection ->
                    var searchRequest = SearchRequest(collection.indexName)
                    var searchSourceBuilder = SearchSourceBuilder()
                    val queryList = collection.getQuery(parameter)

                    if (collection is Pageable) {
                        val pageVo = collection.pageVo(parameter)
                        searchSourceBuilder.from(pageVo.start)
                        searchSourceBuilder.size(pageVo.count)
                    }

                    if (collection is Sortable) {
                        collection.sortVo(parameter).sortBuilderList(parameter).forEach { searchSourceBuilder.sort(it) }
                    }

                    if (collection is Groupable) {
                        searchSourceBuilder.size(0)
                        collection.groupVo(parameter).aggregationBuilderList(parameter).forEach { searchSourceBuilder.aggregation(it) }
                    }
                    searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryList.joinToString(" AND ")))
                    searchRequest.source(searchSourceBuilder)

                    requests.add(searchRequest)

                    logger.info("======================================================================")
                    logger.info("{ ${parameter.target} , ${collection.indexName}, ${collection.getClassName(parameter)} }")
                    logger.info(searchRequest.source().toString())
                    logger.info("======================================================================")
                }

        if (requests.requests().size == 0) return
        var response = client.msearch(requests, RequestOptions.DEFAULT)
        result(parameter, response, result)

    }

    fun result(parameter: Parameter, response: MultiSearchResponse, result: Result) {
        response.responses.forEachIndexed { idx, res ->
            if (res.failure != null) {
                result.libErrMsg = res.failure.toString()
                throw res.failure
            }

            Targets.valueOf(parameter.target.toUpperCase())
                    .getCollectionSet(parameter)
                    .filterIsInstance<EsCollection>()[idx]
                    .getResult(res.response, "esBuilder", parameter, result)
        }

        var itemTotalCount = result.itemCount
        var bookTotalCount = result.bookCount

        if (bookTotalCount > 0 && itemTotalCount < bookTotalCount) {
            result.itemCount = if (bookTotalCount >= 500000) 500000 else bookTotalCount
            result.itemList = result.bookItemList
            result.srchItemIds = result.bookItemIds
            result.mallCountMap = result.bookMallCountMap
        }

        if (listOf(Targets.ES_BRAND_DISP, Targets.ES_BUNDLE).contains(Targets.valueOf(parameter.target.toUpperCase()))) {
            if (bookTotalCount > 0 && itemTotalCount < bookTotalCount) {
                result.categoryList = result.bookCategoryList
            }
        }

        result.resultYn = true
    }
}