package ssg.front.search.api.dto.result.test

import com.fasterxml.jackson.annotation.JsonIgnore

data class ShoppingMagazine (
    var shpgMgzId: String,
    var shpgMgzNm: String,
    @get:JsonIgnore
    var shpgMgzTypeCd: String,
    var srchwd: String,
    var dispStrtDts: String,
    @get:JsonIgnore
    var dispEndDts: String,
    var imgFileNm: String,
    var maiTitleNm1: String,
    var maiTitleNm2: String,
    var banrDesc: String,
    @get:JsonIgnore
    var aplSiteNoLst: String,
    @get:JsonIgnore
    var aplMediaCd: String,
    var lnkdUrl: String,
    var shpgContentsCd: String,
    var shpgContentsNm: String,
    var shpgCategoryCd: String,
    var shpgCategoryNm: String
)