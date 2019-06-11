package ssg.search.result;

import java.util.List;
import java.util.StringTokenizer;

public class Category{
	private String  siteNo;		// 사이트 NO
	private String  ctgId;		// 카테고리 ID
	private String  ctgNm;		// 카테고리 명

	private int	 ctgItemCount;	// 카테고리에 포함된 상품 수
	private int	 ctgLevel;		// 카테고리 레벨

	private boolean hasChild;		// 자식 존재 여부

	private String ctgLclsId;
	private String ctgMclsId;
	private String ctgSclsId;
	private String ctgDclsId;
	
	private String themeYn;		 // 테마카테고리 여부
	private String priorCtgId;		// 부모카테고리ID
	private String selectedArea;	// 선택된 영역 여부 ( 브랜드SHOP에서 사용 )

	private List<Category> childCategoryList;
	private String recomYn = "N";

	public String getCtgId() {
		return ctgId;
	}
	public void setCtgId(String ctgId) {
		this.ctgId = ctgId;
	}
	public String getCtgNm() {
		return ctgNm;
	}
	public void setCtgNm(String ctgNm) {
		this.ctgNm = ctgNm;
	}
	public int getCtgItemCount() {
		return ctgItemCount;
	}
	public void setCtgItemCount(int ctgItemCount) {
		this.ctgItemCount = ctgItemCount;
	}
	public int getCtgLevel() {
		return ctgLevel;
	}
	public void setCtgLevel(int ctgLevel) {
		this.ctgLevel = ctgLevel;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public String getCtgLclsId() {
		return ctgLclsId;
	}
	public void setCtgLclsId(String ctgLclsId) {
		this.ctgLclsId = ctgLclsId;
	}
	public String getCtgMclsId() {
		return ctgMclsId;
	}
	public void setCtgMclsId(String ctgMclsId) {
		this.ctgMclsId = ctgMclsId;
	}
	public String getCtgSclsId() {
		return ctgSclsId;
	}
	public void setCtgSclsId(String ctgSclsId) {
		this.ctgSclsId = ctgSclsId;
	}
	public String getCtgDclsId() {
		return ctgDclsId;
	}
	public void setCtgDclsId(String ctgDclsId) {
		this.ctgDclsId = ctgDclsId;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
	}
	public String getThemeYn() {
		return themeYn;
	}
	public void setThemeYn(String themeYn) {
		this.themeYn = themeYn;
	}
	public String getPriorCtgId() {
		return priorCtgId;
	}
	public void setPriorCtgId(String priorCtgId) {
		this.priorCtgId = priorCtgId;
	}
	public String getSelectedArea() {
		return selectedArea;
	}
	public void setSelectedArea(String selectedArea) {
		this.selectedArea = selectedArea;
	}
	public void tokenizeCtg(String ctg){
		int lv = 1;
		for(StringTokenizer st = new StringTokenizer(ctg,":");st.hasMoreTokens();){
			String tk = st.nextToken();
			String[] sp = tk.split("\\^");

			this.siteNo = sp[0];
			this.ctgId  = sp[1];
			this.ctgNm  = sp[2];

			if(lv==1){
				this.ctgLclsId = sp[1];
			}else if(lv==2){
				this.ctgMclsId = sp[1];
			}else if(lv==3){
				this.ctgSclsId = sp[1];
			}else if(lv==4){
				this.ctgDclsId = sp[1];
			}
			lv++;
		}
	}
	public String getLvlCtgId(int lv){
		if(lv==1){
			return this.ctgLclsId;
		}else if(lv==2){
			return this.ctgMclsId;
		}else if(lv==3){
			return this.ctgSclsId;
		}else if(lv==4){
			return this.ctgDclsId;
		}
		return null;
	}

	public List<Category> getChildCategoryList() {
		return childCategoryList;
	}

	public void setChildCategoryList(List<Category> childCategoryList) {
		this.childCategoryList = childCategoryList;
	}

	public String getRecomYn() {
		return recomYn;
	}

	public void setRecomYn(String recomYn) {
		this.recomYn = recomYn;
	}
}
