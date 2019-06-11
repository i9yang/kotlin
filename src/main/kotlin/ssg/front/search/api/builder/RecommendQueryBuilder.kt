package ssg.front.search.api.builder

import org.elasticsearch.action.search.MultiSearchRequest
import org.elasticsearch.action.search.MultiSearchResponse
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import ssg.front.search.api.collection.rsearch.RecomCollection
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Sortable
import ssg.search.collection.es.rsearch.MyTasteCollection

@Component
class RecommendQueryBuilder : SsgQueryBuilder {
    private val logger = LoggerFactory.getLogger(RecommendQueryBuilder::class.java)

    @Autowired
    @Qualifier("esRecomRestClient")
    lateinit var client: RestHighLevelClient

    override fun execute(parameter: Parameter, result: Result) {
        var requests = MultiSearchRequest()

        Targets.valueOf(parameter.target.toUpperCase())
                .getCollectionSet(parameter)
                .filterIsInstance<RecomCollection>()
                .forEach { collection ->
                    var searchRequest = SearchRequest(collection.indexName)
                    var searchSourceBuilder = SearchSourceBuilder()

                    if (collection is Pageable) {
                        val pageVo = collection.pageVo(parameter)
                        searchSourceBuilder.from(pageVo.start)
                        searchSourceBuilder.size(pageVo.count)
                    }

                    if (collection is Sortable) {
                        collection.sortVo(parameter).sortBuilderList(parameter).forEach { searchSourceBuilder.sort(it) }
                    }

                    if (collection is MyTasteCollection) {
                        searchRequest.routing(parameter.userInfo.mbrId)
                    }

                    searchSourceBuilder.query(collection.getQuery(parameter))
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
                    .filterIsInstance<RecomCollection>()[idx]

                    .getResult(res.response, "recomBuilder", parameter, result)
        }
    }
}