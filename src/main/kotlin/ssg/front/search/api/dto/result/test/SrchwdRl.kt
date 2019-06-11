package ssg.front.search.api.dto.result.test

import com.google.gson.annotations.SerializedName

data class SrchwdRl (
    @SerializedName("SRCHWD_NM")
    var srchwdNm: String,
    @SerializedName("RL_KEYWD_NM")
    var rlKeywdNm: String
)
