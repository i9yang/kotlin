package ssg.search.result;

public class Recom implements ItemData{
    private String siteNo;
	private String itemId;
	private String itemNm;
	
	private String obanjangYn;
    private String newsYn;
    private String spPriceYn;
    
    private String dispOrdr;
	
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
	public String getDispOrdr() {
		return dispOrdr;
	}
	public void setDispOrdr(String dispOrdr) {
		this.dispOrdr = dispOrdr;
	}
	@Override
	public String getSalestrNo() {
		// TODO Auto-generated method stub
		return null;
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
	@Override
	public String getSellprc() {
		// TODO Auto-generated method stub
		return null;
	}
}
