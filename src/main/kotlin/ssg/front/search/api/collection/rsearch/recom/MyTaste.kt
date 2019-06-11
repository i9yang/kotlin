package ssg.front.search.api.collection.rsearch.recom

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.collect.Lists
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.MatchQueryBuilder
import org.elasticsearch.index.query.QueryBuilder
import ssg.front.search.api.base.Sort
import ssg.front.search.api.collection.rsearch.RecomCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.dto.vo.SortVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.function.Sortable
import ssg.front.search.api.util.CamelCaseToUppperCaseWithUnderscores
import ssg.search.result.Taste


class MyTaste : RecomCollection(), Pageable, Sortable {
    override var indexName: String = "my"

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0, 100)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sortList: ArrayList<Sort> = ArrayList()
        sortList.add(Sort("ORD_CONT", 1))
        sortList.add(Sort("ORD_QTY", 1))
        sortList.add(Sort("LAST_ORD_DTS", 1))

        return SortVo(sortList)
    }

    override fun getQuery(parameter: Parameter): QueryBuilder {
        return BoolQueryBuilder()
                .filter(
                        MatchQueryBuilder("MBR_ID", parameter.userInfo.mbrId!!)
                ).must(
                        MatchQueryBuilder("QUERY", parameter.query!!)
                )
    }

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        var list = mutableListOf<Taste>()

        searchResponse.hits.hits.forEach { hit ->
            var mapper = jacksonObjectMapper()
                    .setPropertyNamingStrategy(CamelCaseToUppperCaseWithUnderscores())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            val item = mapper.readValue<Taste>(hit.sourceAsString)
            list.add(item)
        }

        val tasteList = Lists.newArrayList<Taste>()
        val srchItemIds = StringBuilder()
        // Rule Set
        for (h in list) {
            val itemId = h.itemId
            val tasteType = h.tasteType
            val ordCont = h.ordCont ?: "0"


            val t = Taste()
            t.itemId = itemId
            t.taste = "my"
            t.tasteType = tasteType
            srchItemIds.append(itemId).append(":").append("my:").append(tasteType).append(":").append(ordCont).append(",")
            tasteList.add(t)
        }
        result.myTasteIds = srchItemIds.toString()
        result.myTasteList = tasteList
    }
}
