package ssg.front.search.api.dto.result.test

data class Store (
    var storeNm: String,
    var imgFileNm: String,
    var linkUrl: String,
    var bgVal: String,
    var categoryList: List<Map<String, String>>
)