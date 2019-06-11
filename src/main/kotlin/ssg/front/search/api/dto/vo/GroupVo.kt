package ssg.front.search.api.dto.vo

import org.elasticsearch.search.aggregations.AggregationBuilder
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.BucketOrder
import ssg.front.search.api.dto.Parameter

data class GroupVo(var group: String = "") {
    fun aggregationBuilderList(parameter: Parameter): List<AggregationBuilder> {
        var aggregationBuilderList = mutableListOf<AggregationBuilder>()

        when {
            group == "SELLPRC" -> {
                var min = AggregationBuilders.min("minPrc").field(group)
                var max = AggregationBuilders.max("maxPrc").field(group)

                aggregationBuilderList.add(min)
                aggregationBuilderList.add(max)
            }
            group.contains("|") -> {
                var groups = group.split("|")

                var aggregationBuilder = AggregationBuilders.terms("${groups[0]}").field("${groups[0]}").size(9999)
                        .subAggregation(AggregationBuilders.terms("${groups[1]}").field("${groups[1]}").size(9999))
                aggregationBuilderList.add(aggregationBuilder)
            }
            else -> {
                for (group in group.split(",")) {
                    var aggregationBuilder = AggregationBuilders.terms(group).field(group).size(9999)

                    if (group == "SIZE_LIST") {
                        aggregationBuilder.order(BucketOrder.key(true))
                    }

                    aggregationBuilderList.add(aggregationBuilder)
                }
            }
        }
        return aggregationBuilderList
    }

}