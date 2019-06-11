package ssg.search.result;

public class Book implements ItemData{
	private String itemId;
	private String siteNo;
	private String itemNm;
	private String dispCtgId;
	private String dispCtgNm;
	private String isbn;
	private String bookEngNm;
	private String ortitlNm;
	private String subtitlNm;
	private String authorNm;
	private String trltpeNm;
	private String pubscoNm;
	private String sellprc;
	private String fxprc;
	private String shppTypeDtlCd;
	private String dispCtgLclsId;
    private String dispCtgMclsId;
    private String dispCtgSclsId;
    private String dispCtgDclsId;
    private String dispCtgLclsNm;
    private String dispCtgMclsNm;
    private String dispCtgSclsNm;
    private String dispCtgDclsNm;
    private String itemRegDivCd;
    private String salestrNo;

	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(String siteNo) {
		this.siteNo = siteNo;
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
	public String getDispCtgNm() {
		return dispCtgNm;
	}
	public void setDispCtgNm(String dispCtgNm) {
		this.dispCtgNm = dispCtgNm;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getBookEngNm() {
		return bookEngNm;
	}
	public void setBookEngNm(String bookEngNm) {
		this.bookEngNm = bookEngNm;
	}
	public String getOrtitlNm() {
		return ortitlNm;
	}
	public void setOrtitlNm(String ortitlNm) {
		this.ortitlNm = ortitlNm;
	}
	public String getSubtitlNm() {
		return subtitlNm;
	}
	public void setSubtitlNm(String subtitlNm) {
		this.subtitlNm = subtitlNm;
	}
	public String getAuthorNm() {
		return authorNm;
	}
	public void setAuthorNm(String authorNm) {
		this.authorNm = authorNm;
	}
	public String getTrltpeNm() {
		return trltpeNm;
	}
	public void setTrltpeNm(String trltpeNm) {
		this.trltpeNm = trltpeNm;
	}
	public String getPubscoNm() {
		return pubscoNm;
	}
	public void setPubscoNm(String pubscoNm) {
		this.pubscoNm = pubscoNm;
	}
    public String getDispCtgLclsId() {
        return dispCtgLclsId;
    }
    public void setDispCtgLclsId(String dispCtgLclsId) {
        this.dispCtgLclsId = dispCtgLclsId;
    }
    public String getDispCtgMclsId() {
        return dispCtgMclsId;
    }
    public void setDispCtgMclsId(String dispCtgMclsId) {
        this.dispCtgMclsId = dispCtgMclsId;
    }
    public String getDispCtgSclsId() {
        return dispCtgSclsId;
    }
    public void setDispCtgSclsId(String dispCtgSclsId) {
        this.dispCtgSclsId = dispCtgSclsId;
    }
    public String getDispCtgDclsId() {
        return dispCtgDclsId;
    }
    public void setDispCtgDclsId(String dispCtgDclsId) {
        this.dispCtgDclsId = dispCtgDclsId;
    }
    public String getDispCtgLclsNm() {
        return dispCtgLclsNm;
    }
    public void setDispCtgLclsNm(String dispCtgLclsNm) {
        this.dispCtgLclsNm = dispCtgLclsNm;
    }
    public String getDispCtgMclsNm() {
        return dispCtgMclsNm;
    }
    public void setDispCtgMclsNm(String dispCtgMclsNm) {
        this.dispCtgMclsNm = dispCtgMclsNm;
    }
    public String getDispCtgSclsNm() {
        return dispCtgSclsNm;
    }
    public void setDispCtgSclsNm(String dispCtgSclsNm) {
        this.dispCtgSclsNm = dispCtgSclsNm;
    }
    public String getDispCtgDclsNm() {
        return dispCtgDclsNm;
    }
    public void setDispCtgDclsNm(String dispCtgDclsNm) {
        this.dispCtgDclsNm = dispCtgDclsNm;
    }
    public String getSellprc() {
        return sellprc;
    }
    public void setSellprc(String sellprc) {
        this.sellprc = sellprc;
    }
    public String getShppTypeDtlCd() {
        return shppTypeDtlCd;
    }
    public void setShppTypeDtlCd(String shppTypeDtlCd) {
        this.shppTypeDtlCd = shppTypeDtlCd;
    }
    public String getFxprc() {
        return fxprc;
    }
    public void setFxprc(String fxprc) {
        this.fxprc = fxprc;
    }
	public String getItemRegDivCd(){
		return itemRegDivCd;
	}
	public void setItemRegDivCd(String itemRegDivCd){
		this.itemRegDivCd = itemRegDivCd;
	}
	public String getSalestrNo(){
		return salestrNo;
	}
	public void setSalestrNo(String salestrNo){
		this.salestrNo = salestrNo;
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
    
}
