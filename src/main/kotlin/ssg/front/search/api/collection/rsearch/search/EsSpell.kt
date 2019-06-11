package ssg.front.search.api.collection.rsearch.search

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
import ssg.search.result.Spell

class EsSpell : RecomCollection(), Pageable, Sortable {
    override var indexName: String = "spell"

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0, 1)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sortList: ArrayList<Sort> = ArrayList()
        sortList.add(Sort("_id", 0))

        return SortVo(sortList)
    }

    override fun getQuery(parameter: Parameter): QueryBuilder {
        return BoolQueryBuilder()
                .must(MatchQueryBuilder("CRITN_SRCHWD_NM", parameter.query))
    }

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        var list = mutableListOf<Spell>()
        val spellList = mutableListOf<Spell>()

        searchResponse.hits.hits.forEach { hit ->
            var mapper = jacksonObjectMapper()
                    .setPropertyNamingStrategy(CamelCaseToUppperCaseWithUnderscores())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            val item = mapper.readValue<Spell>(hit.sourceAsString)
            list.add(item)
        }

        // Rule Set
        for (h in list) {
            var critnSrchwdNm = h.critnSrchwdNm
            var rplcKeywdNm = h.rplcKeywdNm

            var s = Spell()
            s.critnSrchwdNm = critnSrchwdNm
            s.rplcKeywdNm = rplcKeywdNm

            spellList.add(s)
        }

        result.spellList = spellList
        result.spellCount = spellList.size
    }
}
