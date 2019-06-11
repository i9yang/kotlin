package ssg.search.result;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by 131544 on 2016-04-05.
 */
public class MobileCategory {
	private String dispCtgLvl;
	private String dispCtgId;
	private String dispCtgNm;
	private String priorDispCtgId;
	private String siteNo;
	private String itemCount;
	private String hasChild;
	private List<MobileCategory> children = Lists.newArrayList();

	public String getDispCtgLvl() {
		return dispCtgLvl;
	}

	public void setDispCtgLvl(String dispCtgLvl) {
		this.dispCtgLvl = dispCtgLvl;
	}

	public List<MobileCategory> getChildren() {
		return children;
	}

	public void setChildren(List<MobileCategory> children) {
		this.children = children;
	}

	public String getDispCtgId() {
		return dispCtgId;
	}

	public void setDispCtgId(String dispCtgId) {
		this.dispCtgId = dispCtgId;
	}

	public String getDispCtgNm() {
		return dispCtgNm;
	}

	public void setDispCtgNm(String dispCtgNm) {
		this.dispCtgNm = dispCtgNm;
	}

	public String getPriorDispCtgId() {
		return priorDispCtgId;
	}

	public void setPriorDispCtgId(String priorDispCtgId) {
		this.priorDispCtgId = priorDispCtgId;
	}

	public String getSiteNo() {
		return siteNo;
	}

	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}

	public void add(MobileCategory c){
		this.children.add(c);
	}

	public String getItemCount() {
		return itemCount;
	}

	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getHasChild() {
		return hasChild;
	}

	public void setHasChild(String hasChild) {
		this.hasChild = hasChild;
	}
	
}
