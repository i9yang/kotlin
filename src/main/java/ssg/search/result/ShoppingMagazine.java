package ssg.search.result;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShoppingMagazine{
	private String shpgMgzId;
	private String shpgMgzNm;
	private String shpgMgzTypeCd;
	private String srchwd;
	private String dispStrtDts;
	private String dispEndDts;
	private String imgFileNm;
	private String maiTitleNm1;
	private String maiTitleNm2;
	private String banrDesc;
	private String aplSiteNoLst;
	private String aplMediaCd;
	private String lnkdUrl;
	private String shpgContentsCd;
	private String shpgContentsNm;
	private String shpgCategoryCd;
	private String shpgCategoryNm;
	
	public String getShpgMgzId() {
		return shpgMgzId;
	}
	public void setShpgMgzId(String shpgMgzId) {
		this.shpgMgzId = shpgMgzId;
	}
	public String getShpgMgzNm() {
		return shpgMgzNm;
	}
	public void setShpgMgzNm(String shpgMgzNm) {
		this.shpgMgzNm = shpgMgzNm;
	}
	
	@JsonIgnore
	public String getShpgMgzTypeCd() {
		return shpgMgzTypeCd;
	}
	public void setShpgMgzTypeCd(String shpgMgzTypeCd) {
		this.shpgMgzTypeCd = shpgMgzTypeCd;
	}
	public String getSrchwd() {
		return srchwd;
	}
	public void setSrchwd(String srchwd) {
		this.srchwd = srchwd;
	}
	public String getDispStrtDts() {
		return dispStrtDts;
	}
	public void setDispStrtDts(String dispStrtDts) {
		this.dispStrtDts = dispStrtDts;
	}
	
	@JsonIgnore
	public String getDispEndDts() {
		return dispEndDts;
	}
	public void setDispEndDts(String dispEndDts) {
		this.dispEndDts = dispEndDts;
	}
	public String getImgFileNm() {
		return imgFileNm;
	}
	public void setImgFileNm(String imgFileNm) {
		this.imgFileNm = imgFileNm;
	}
	public String getMaiTitleNm1() {
		return maiTitleNm1;
	}
	public void setMaiTitleNm1(String maiTitleNm1) {
		this.maiTitleNm1 = maiTitleNm1;
	}
	public String getMaiTitleNm2() {
		return maiTitleNm2;
	}
	public void setMaiTitleNm2(String maiTitleNm2) {
		this.maiTitleNm2 = maiTitleNm2;
	}
	public String getBanrDesc() {
		return banrDesc;
	}
	public void setBanrDesc(String banrDesc) {
		this.banrDesc = banrDesc;
	}
	
	@JsonIgnore
	public String getAplSiteNoLst() {
		return aplSiteNoLst;
	}
	public void setAplSiteNoLst(String aplSiteNoLst) {
		this.aplSiteNoLst = aplSiteNoLst;
	}
	
	@JsonIgnore
	public String getAplMediaCd() {
		return aplMediaCd;
	}
	public void setAplMediaCd(String aplMediaCd) {
		this.aplMediaCd = aplMediaCd;
	}
	public String getLnkdUrl() {
		return lnkdUrl;
	}
	public void setLnkdUrl(String lnkdUrl) {
		this.lnkdUrl = lnkdUrl;
	}
	public String getShpgContentsCd() {
		return shpgContentsCd;
	}
	public void setShpgContentsCd(String shpgContentsCd) {
		this.shpgContentsCd = shpgContentsCd;
	}
	public String getShpgContentsNm() {
		return shpgContentsNm;
	}
	public void setShpgContentsNm(String shpgContentsNm) {
		this.shpgContentsNm = shpgContentsNm;
	}
	public String getShpgCategoryCd() {
		return shpgCategoryCd;
	}
	public void setShpgCategoryCd(String shpgCategoryCd) {
		this.shpgCategoryCd = shpgCategoryCd;
	}
	public String getShpgCategoryNm() {
		return shpgCategoryNm;
	}
	public void setShpgCategoryNm(String shpgCategoryNm) {
		this.shpgCategoryNm = shpgCategoryNm;
	}
}
