package ssg.search.result;

import java.util.List;
import java.util.Map;

public class Store{
	private String storeNm;
	private String imgFileNm;
	private String linkUrl;
	private String bgVal;
	private List<Map<String,String>>  categoryList;
	public String getStoreNm() {
		return storeNm;
	}
	public void setStoreNm(String storeNm) {
		this.storeNm = storeNm;
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
	public String getBgVal() {
		return bgVal;
	}
	public void setBgVal(String bgVal) {
		this.bgVal = bgVal;
	}
	public List<Map<String, String>> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<Map<String, String>> categoryList) {
		this.categoryList = categoryList;
	}

	
}
