package ssg.search.result;

import com.google.gson.annotations.SerializedName;

public class Banr{
	@SerializedName("SRCH_CRITN_ID")
	private String srchCritnId;
	@SerializedName("SITE_NO")
	private String siteNo;
	@SerializedName("SHRTC_DIV_CD")
	private String shrtcDivCd;
	@SerializedName("SHRTC_TGT_TYPE_CD")
	private String shrtcTgtTypeCd;
	@SerializedName("IMG_FILE_NM")
	private String imgFileNm;
	@SerializedName("LINK_URL")
	private String linkUrl;
	private String dispStrtDts;
	private String dispEndDts;
	@SerializedName("BANR_RPLC_TEXT_NM")
	private String banrRplcTextNm;
	@SerializedName("LIQUOR_YN")
	private String liquorYn;
	@SerializedName("POP_YN")
	private String popYn;
	private String exclSrchwdNm;
	
	//광고관련추가
	private String advertAcctId;
	private String advertBidId;
	private String advertBilngTypeCd;
	private String advertKindCd;
	private String advertExtensTeryDivCd;
	
	public String getSrchCritnId() {
		return srchCritnId;
	}
	public void setSrchCritnId(String srchCritnId) {
		this.srchCritnId = srchCritnId;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	public String getShrtcDivCd() {
		return shrtcDivCd;
	}
	public void setShrtcDivCd(String shrtcDivCd) {
		this.shrtcDivCd = shrtcDivCd;
	}
	public String getShrtcTgtTypeCd() {
		return shrtcTgtTypeCd;
	}
	public void setShrtcTgtTypeCd(String shrtcTgtTypeCd) {
		this.shrtcTgtTypeCd = shrtcTgtTypeCd;
	}
	public String getImgFileNm() {
		return imgFileNm;
	}
	public void setImgFileNm(String imgFileNm) {
		this.imgFileNm = imgFileNm;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getDispStrtDts() {
		return dispStrtDts;
	}
	public void setDispStrtDts(String dispStrtDts) {
		this.dispStrtDts = dispStrtDts;
	}
	public String getDispEndDts() {
		return dispEndDts;
	}
	public void setDispEndDts(String dispEndDts) {
		this.dispEndDts = dispEndDts;
	}
    public String getBanrRplcTextNm() {
        return banrRplcTextNm;
    }
    public void setBanrRplcTextNm(String banrRplcTextNm) {
        this.banrRplcTextNm = banrRplcTextNm;
    }
    public String getLiquorYn() {
        return liquorYn;
    }
    public void setLiquorYn(String liquorYn) {
        this.liquorYn = liquorYn;
    }
	public String getPopYn() {
		return popYn;
	}
	public void setPopYn(String popYn) {
		this.popYn = popYn;
	}
	public String getExclSrchwdNm() {
		return exclSrchwdNm;
	}
	public void setExclSrchwdNm(String exclSrchwdNm) {
		this.exclSrchwdNm = exclSrchwdNm;
	}
	public String getAdvertAcctId() {
		return advertAcctId;
	}
	public void setAdvertAcctId(String advertAcctId) {
		this.advertAcctId = advertAcctId;
	}
	public String getAdvertBidId() {
		return advertBidId;
	}
	public void setAdvertBidId(String advertBidId) {
		this.advertBidId = advertBidId;
	}
	public String getAdvertBilngTypeCd() {
		return advertBilngTypeCd;
	}
	public void setAdvertBilngTypeCd(String advertBilngTypeCd) {
		this.advertBilngTypeCd = advertBilngTypeCd;
	}
	public String getAdvertKindCd() {
		return advertKindCd;
	}
	public void setAdvertKindCd(String advertKindCd) {
		this.advertKindCd = advertKindCd;
	}
	public String getAdvertExtensTeryDivCd() {
		return advertExtensTeryDivCd;
	}
	public void setAdvertExtensTeryDivCd(String advertExtensTeryDivCd) {
		this.advertExtensTeryDivCd = advertExtensTeryDivCd;
	}
}
