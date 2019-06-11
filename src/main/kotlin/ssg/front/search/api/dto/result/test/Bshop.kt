package ssg.front.search.api.dto.result.test

data class Bshop (
    var bshopId: String,
    var bshopTitleNm: String,
    var bshopEngTitleNm1: String,
    var bshopEngTitleNm2: String,
    var imgFileNm: String,
    var repBrandId: String,
    var bshopItemCount: Int = 0
)