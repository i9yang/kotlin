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
import ssg.search.result.Banr

class EsBanr : RecomCollection(), Pageable, Sortable {
    override var indexName: String = "banr"

    override fun pageVo(parameter: Parameter): PageVo {
        return PageVo(0, 2)
    }

    override fun sortVo(parameter: Parameter): SortVo {
        var sortList: ArrayList<Sort> = ArrayList()
        sortList.add(Sort("_id", 0))

        return SortVo(sortList)
    }

    override fun getQuery(parameter: Parameter): QueryBuilder {
        return BoolQueryBuilder()
                .must(MatchQueryBuilder("CRITN_SRCHWD_NM", parameter.query!!.replace(" ", "")))
                .must(MatchQueryBuilder("SITE_NO", parameter.siteNo))
                .must(MatchQueryBuilder("SHRTC_TGT_TYPE_CD", parameter.aplTgtMediaCd))
    }

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        var list = mutableListOf<Banr>()
        val banrList = mutableListOf<Banr>()

        searchResponse.hits.hits.forEach { hit ->
            var mapper = jacksonObjectMapper()
                    .setPropertyNamingStrategy(CamelCaseToUppperCaseWithUnderscores())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            val item = mapper.readValue<Banr>(hit.sourceAsString)
            list.add(item)
        }

        // Rule Set
        for (h in list) {
            val srchCritnId = h.srchCritnId
            val shrtcDivCd = h.shrtcDivCd
            val imgFileNm = h.imgFileNm
            val banrRplcTextNm = h.banrRplcTextNm
            var linkUrl = h.linkUrl
            val liquorYn = h.liquorYn
            val popYn = h.popYn
            val siteNo = h.siteNo
            val shrtcTgtTypeCd = h.shrtcTgtTypeCd

            //링크뒤에 자동으로 쿼리 넘김({}있을때만 처리되도록)
            if (linkUrl.indexOf("query={}") > -1) {
                linkUrl = linkUrl.substring(0, linkUrl.indexOf("{}")) + parameter.query
            }

            val b = Banr()

            b.srchCritnId = srchCritnId
            b.siteNo = siteNo
            b.shrtcTgtTypeCd = shrtcTgtTypeCd
            b.shrtcDivCd = shrtcDivCd
            b.imgFileNm = imgFileNm
            b.banrRplcTextNm = banrRplcTextNm
            b.linkUrl = linkUrl
            b.liquorYn = liquorYn
            b.popYn = popYn

            banrList.add(b)
        }

        result.banrList = banrList
        result.banrCount = banrList.size
        result.banrGcount = banrList.size
    }
}
