package ssg.search.result;

public class Postng implements ItemData{
    private String siteNo;
    private String itemId;
    private String itemNm;
    private String dispCtgId;
    private String postngId;
    private String postngTitleNm;
    private String postngEvalScr;
    
    private String obanjangYn;
    private String newsYn;
    private String spPriceYn;
    private String sellprc;
    private String postngWrtpeIdnfId;
    private String salestrNo;
    
    public String getSiteNo() {
        return siteNo;
    }
    public void setSiteNo(String siteNo) {
        this.siteNo = siteNo;
    }
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getItemNm() {
        return itemNm;
    }
    public void setItemNm(String itemNm) {
        this.itemNm = itemNm;
    }
    public String getDispCtgId() {
        return dispCtgId;
    }
    public void setDispCtgId(String dispCtgId) {
        this.dispCtgId = dispCtgId;
    }
    public String getPostngId() {
        return postngId;
    }
    public void setPostngId(String postngId) {
        this.postngId = postngId;
    }
    public String getPostngTitleNm() {
        return postngTitleNm;
    }
    public void setPostngTitleNm(String postngTitleNm) {
        this.postngTitleNm = postngTitleNm;
    }
    public String getPostngEvalScr() {
        return postngEvalScr;
    }
    public void setPostngEvalScr(String postngEvalScr) {
        this.postngEvalScr = postngEvalScr;
    }
    public String getObanjangYn() {
        return obanjangYn;
    }
    public void setObanjangYn(String obanjangYn) {
        this.obanjangYn = obanjangYn;
    }
    public String getNewsYn() {
        return newsYn;
    }
    public void setNewsYn(String newsYn) {
        this.newsYn = newsYn;
    }
    public String getSpPriceYn() {
        return spPriceYn;
    }
    public void setSpPriceYn(String spPriceYn) {
        this.spPriceYn = spPriceYn;
    }
    public String getSellprc() {
        return sellprc;
    }
    public void setSellprc(String sellprc) {
        this.sellprc = sellprc;
    }
    public String getPostngWrtpeIdnfId() {
        return postngWrtpeIdnfId;
    }
    public void setPostngWrtpeIdnfId(String postngWrtpeIdnfId) {
        this.postngWrtpeIdnfId = postngWrtpeIdnfId;
    }
	public String getSalestrNo(){
		return salestrNo;
	}
	public void setSalestrNo(String salestrNo){
		this.salestrNo = salestrNo;
	}
	@Override
	public String getItemRegDivCd() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getExusItemDivCd() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getExusItemDtlCd() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getShppMainCd() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getShppMthdCd() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getShppTypeCd() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getShppTypeDtlCd() {
		// TODO Auto-generated method stub
		return null;
	}
}
