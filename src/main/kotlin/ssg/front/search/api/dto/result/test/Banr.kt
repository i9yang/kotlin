package ssg.front.search.api.dto.result.test

import com.google.gson.annotations.SerializedName

data class Banr (
    @SerializedName("SRCH_CRITN_ID")
    var srchCritnId: String,
    @SerializedName("SITE_NO")
    var siteNo: String,
    @SerializedName("SHRTC_DIV_CD")
    var shrtcDivCd: String,
    @SerializedName("SHRTC_TGT_TYPE_CD")
    var shrtcTgtTypeCd: String,
    @SerializedName("IMG_FILE_NM")
    var imgFileNm: String,
    @SerializedName("LINK_URL")
    var linkUrl: String,
    var dispStrtDts: String,
    var dispEndDts: String,
    @SerializedName("BANR_RPLC_TEXT_NM")
    var banrRplcTextNm: String,
    @SerializedName("LIQUOR_YN")
    var liquorYn: String,
    @SerializedName("POP_YN")
    var popYn: String,
    var exclSrchwdNm: String,

    //광고관련추가
    var advertAcctId: String,
    var advertBidId: String,
    var advertBilngTypeCd: String,
    var advertKindCd: String,
    var advertExtensTeryDivCd: String
)
