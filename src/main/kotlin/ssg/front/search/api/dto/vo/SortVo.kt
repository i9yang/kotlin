package ssg.front.search.api.dto.vo

import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.NestedSortBuilder
import org.elasticsearch.search.sort.SortBuilders
import org.elasticsearch.search.sort.SortOrder
import ssg.front.search.api.base.Sort
import ssg.front.search.api.dto.Parameter

data class SortVo(var sortList: ArrayList<Sort>) {
    fun sortBuilderList(parameter: Parameter): List<FieldSortBuilder> {
        var sortBuilderList = mutableListOf<FieldSortBuilder>()

        for (sort in sortList) {
            var sortBuilder = SortBuilders.fieldSort(sort.sortName).order(if (sort.operator == 1) SortOrder.DESC else SortOrder.ASC)

            if (sort.sortName == "DISP_CTG_ORDR.DISP_ORDR") {
                var dispCtgId = parameter.dispCtgId ?: parameter.ctgId ?: ""

                sortBuilder
                        .nestedSort = NestedSortBuilder("DISP_CTG_ORDR")
                        .setFilter(QueryBuilders.termsQuery("DISP_CTG_ORDR.DISP_CTG_ID", dispCtgId))
            }

            sortBuilderList.add(sortBuilder)
        }

        return sortBuilderList
    }
}