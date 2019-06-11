package ssg.front.search.api.dto.result.test

import com.google.gson.annotations.SerializedName

data class Spell (
    @SerializedName("CRITN_SRCHWD_NM")
    var critnSrchwdNm: String,
    @SerializedName("RPLC_KEYWD_NM")
    var rplcKeywdNm: String
)
