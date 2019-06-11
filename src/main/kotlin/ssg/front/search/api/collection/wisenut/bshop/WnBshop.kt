package ssg.front.search.api.collection.wisenut.bshop

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

class WnBshop : WnCollection() {

    fun getQuery(parameter: Parameter): List<String> {
        return listOf("")
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {

//        val list = searchResult.getHits(Item::class.java)
//        val searchItemList = Lists.newArrayList()
//        for (h in list) {
//            searchItemList.add(h.source as Item)
//        }
//
//        val srchItemIds = StringBuilder()
//        processItemResult(parameter, searchItemList, srchItemIds)
//
//        result.itemList = searchItemList
//        result.itemCount = searchResult.getTotal()!!
//        result.srchItemIds = srchItemIds.toString()

        return Result()
    }
}