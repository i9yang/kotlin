package ssg.front.search.config

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EsConfig {
    @Value("\${es.disp.host}")
    lateinit var dispHosts: List<String>

    @Value("\${es.recom.host}")
    lateinit var recomHosts: List<String>

    @Qualifier("esDispRestClient")
    @Bean
    fun getEsRestHighClient(): RestHighLevelClient {
        val hostList = mutableListOf<HttpHost>()
        for (host in dispHosts) {
            hostList.add(HttpHost(host.split(":")[0], host.split(":")[1].toInt(), "http"))
        }

        val builder = RestClient.builder(*hostList.toTypedArray())
        return RestHighLevelClient(builder)
    }

    @Qualifier("esRecomRestClient")
    @Bean
    fun getRecomRestHighClient(): RestHighLevelClient {
        val hostList = mutableListOf<HttpHost>()
        for (host in recomHosts) {
            hostList.add(HttpHost(host.split(":")[0], host.split(":")[1].toInt(), "http"))
        }

        val builder = RestClient.builder(*hostList.toTypedArray())
        return RestHighLevelClient(builder)
    }
}