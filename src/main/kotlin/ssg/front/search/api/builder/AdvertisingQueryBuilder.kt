package ssg.front.search.api.builder

import org.apache.http.client.utils.URIBuilder
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import ssg.front.search.api.collection.ad.AdCollection
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.util.CommonUtil
import java.net.URI

@Component
class AdvertisingQueryBuilder : SsgQueryBuilder {
    private val logger = LoggerFactory.getLogger(AdvertisingQueryBuilder::class.java)

    @Autowired
    lateinit var restTemplate: RestTemplate

    override fun execute(parameter: Parameter, result: Result) {

        var advertisingDomain = when (CommonUtil.profile) {
            "qa", "qa2" -> "http://qa-addp.ssg.com"
            "stg" -> "http://stg-addp.ssg.com"
            "prod" -> "http://addp.ssg.com"
            else -> "http://dev-addp.ssg.com"
        }

        Targets.valueOf(parameter.target.toUpperCase())
                .getCollectionSet(parameter)
                .filterIsInstance<AdCollection>()
                .forEach { collection ->
                    var urlPath = collection.getUrlPath(parameter)
                    var advertisingUrl = advertisingDomain + urlPath
                    var params = collection.getQuery(parameter)
                    var uri = URIBuilder(URI(advertisingUrl)).addParameters(params).build()

                    val response = restTemplate.getForObject(uri, String::class.java)!!

                    collection.getResult(response, collection.getClassName(parameter), parameter, result)

                    logger.info("======================================================================")
                    logger.info("{ ${parameter.target} , ${collection.getClassName(parameter)} }")
                    logger.info(uri.toString())
                    logger.info("======================================================================")
                }
    }
}