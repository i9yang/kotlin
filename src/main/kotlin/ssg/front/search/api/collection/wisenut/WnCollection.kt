package ssg.front.search.api.collection.wisenut

import QueryAPI510.Search
import ssg.front.search.api.collection.Collection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result

abstract class WnCollection : Collection {
    private var name = "item"
    private var aliasName = ""

    override fun getName(parameter: Parameter): String {
        return name
    }

    open fun getAliasName(parameter: Parameter): String {
        return aliasName
    }

    open fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf(
                "BSHOP_ID",
                "REP_BRAND_ID",
                "BSHOP_TITLE_NM",
                "BSHOP_ENG_TITLE_NM1",
                "BSHOP_ENG_TITLE_NM2",
                "IMG_FILE_NM"
        )
    }

    open fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf(
                "BSHOP_TITLE_NM",
                "BSHOP_ENG_TITLE_NM1",
                "BSHOP_ENG_TITLE_NM2"
        )
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        return Result()
    }
}