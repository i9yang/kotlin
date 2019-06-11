package ssg.search.result;

import com.google.gson.annotations.SerializedName;

public class Item implements ItemData{
	@SerializedName("SITE_NO")
	private String siteNo;
	@SerializedName("ITEM_ID")
	private String itemId;
	@SerializedName("ITEM_NM")
	private String itemNm;
	@SerializedName("DISP_CTG_ID")
	private String dispCtgId;
	@SerializedName("DISP_CTG_LCLS_ID")
	private String dispCtgLclsId;
	@SerializedName("DISP_CTG_MCLS_ID")
	private String dispCtgMclsId;
	@SerializedName("DISP_CTG_SCLS_ID")
	private String dispCtgSclsId;
	@SerializedName("DISP_CTG_DCLS_ID")
	private String dispCtgDclsId;
	@SerializedName("DISP_CTG_LCLS_NM")
	private String dispCtgLclsNm;
	@SerializedName("DISP_CTG_MCLS_NM")
	private String dispCtgMclsNm;
	@SerializedName("DISP_CTG_SCLS_NM")
	private String dispCtgSclsNm;
	@SerializedName("DISP_CTG_DCLS_NM")
	private String dispCtgDclsNm;
	@SerializedName("TEM_DISP_CTG_LCLS_ID")
	private String temDispCtgLclsId;
	@SerializedName("TEM_DISP_CTG_LCLS_NM")
	private String temDispCtgLclsNm;
	@SerializedName("TEM_DISP_CTG_MCLS_ID")
	private String temDispCtgMclsId;
	@SerializedName("TEM_DISP_CTG_MCLS_NM")
	private String temDispCtgMclsNm;
	@SerializedName("TEM_DISP_CTG_SCLS_ID")
	private String temDispCtgSclsId;
	@SerializedName("TEM_DISP_CTG_SCLS_NM")
	private String temDispCtgSclsNm;
	@SerializedName("TEM_DISP_CTG_DCLS_ID")
	private String temDispCtgDclsId;
	@SerializedName("TEM_DISP_CTG_DCLS_NM")
	private String temDispCtgDclsNm;
	@SerializedName("OBANJANG_YN")
	private String obanjangYn;
	@SerializedName("NEWS_YN")
	private String newsYn;
	@SerializedName("SP_PRICE_YN")
	private String spPriceYn;
	@SerializedName("SELLPRC")
	private String sellprc;
	@SerializedName("SHPP_TYPE_CD")
	private String shppTypeCd;
	@SerializedName("SHPP_TYPE_DTL_CD")
	private String shppTypeDtlCd;
	@SerializedName("SALESTR_LST")
	private String salestrLst;
	@SerializedName("SALESTR_NO")
	private String salestrNo;
	@SerializedName("SELL_SALESTR_CNT")
	private int sellSalestrCnt;
	@SerializedName("ITEM_REG_DIV_CD")
	private String itemRegDivCd;
	@SerializedName("EXUS_ITEM_DIV_CD")
	private String exusItemDivCd;
	@SerializedName("EXUS_ITEM_DTL_CD")
	private String exusItemDtlCd;
	@SerializedName("SHPP_MTHD_CD")
	private String shppMthdCd;
	@SerializedName("SHPP_MAIN_CD")
	private String shppMainCd;
	@SerializedName("AUTHOR_NM")
	private String authorNm;
	@SerializedName("TRLTPE_NM")
	private String trltpeNm;
	@SerializedName("PUBSCO_NM")
	private String pubscoNm;
	@SerializedName("FXPRC")
	private String fxprc;
	@SerializedName("PICKU_ITEM_YN")
	private String pickuItemYn;
	@SerializedName("SRCH_INSP_STAT_CD")
	private String srchInspStatCd;
	@SerializedName("ADVERT_ACCT_ID")
	private String advertAcctId; // 광고계좌ID
	@SerializedName("ADVERT_BID_ID")
    private String advertBidId; // 광고입찰ID
	@SerializedName("ADVERT_BILNG_TYPE_CD")
	private String advertBilngTypeCd; // 10 CPT / 20 CPC
	@SerializedName("ADVERT_KIND_CD")
    private String advertKindCd; // 10 딜광고 / 20 검색광고 / 30 외부광고
	@SerializedName("ADVERT_EXTENS_TERY_DIV_CD")
	private String advertExtensTeryDivCd;
	
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
    public String getTemDispCtgLclsId() {
        return temDispCtgLclsId;
    }
    public void setTemDispCtgLclsId(String temDispCtgLclsId) {
        this.temDispCtgLclsId = temDispCtgLclsId;
    }
    public String getTemDispCtgLclsNm() {
        return temDispCtgLclsNm;
    }
    public void setTemDispCtgLclsNm(String temDispCtgLclsNm) {
        this.temDispCtgLclsNm = temDispCtgLclsNm;
    }
    public String getTemDispCtgMclsId() {
        return temDispCtgMclsId;
    }
    public void setTemDispCtgMclsId(String temDispCtgMclsId) {
        this.temDispCtgMclsId = temDispCtgMclsId;
    }
    public String getTemDispCtgMclsNm() {
        return temDispCtgMclsNm;
    }
    public void setTemDispCtgMclsNm(String temDispCtgMclsNm) {
        this.temDispCtgMclsNm = temDispCtgMclsNm;
    }
    public String getTemDispCtgSclsId() {
        return temDispCtgSclsId;
    }
    public void setTemDispCtgSclsId(String temDispCtgSclsId) {
        this.temDispCtgSclsId = temDispCtgSclsId;
    }
    public String getTemDispCtgSclsNm() {
        return temDispCtgSclsNm;
    }
    public void setTemDispCtgSclsNm(String temDispCtgSclsNm) {
        this.temDispCtgSclsNm = temDispCtgSclsNm;
    }
    public String getTemDispCtgDclsId() {
        return temDispCtgDclsId;
    }
    public void setTemDispCtgDclsId(String temDispCtgDclsId) {
        this.temDispCtgDclsId = temDispCtgDclsId;
    }
    public String getTemDispCtgDclsNm() {
        return temDispCtgDclsNm;
    }
    public void setTemDispCtgDclsNm(String temDispCtgDclsNm) {
        this.temDispCtgDclsNm = temDispCtgDclsNm;
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
    public String getShppTypeCd() {
		return shppTypeCd;
	}
	public void setShppTypeCd(String shppTypeCd) {
		this.shppTypeCd = shppTypeCd;
	}
	public String getShppTypeDtlCd() {
        return shppTypeDtlCd;
    }
    public void setShppTypeDtlCd(String shppTypeDtlCd) {
        this.shppTypeDtlCd = shppTypeDtlCd;
    }
    public String getSalestrLst() {
        return salestrLst;
    }
    public void setSalestrLst(String salestrLst) {
        this.salestrLst = salestrLst;
    }
	public String getSalestrNo() {
		return salestrNo;
	}
	public void setSalestrNo(String salestrNo) {
		this.salestrNo = salestrNo;
	}
	public int getSellSalestrCnt() {
		return sellSalestrCnt;
	}
	public void setSellSalestrCnt(int sellSalestrCnt) {
		this.sellSalestrCnt = sellSalestrCnt;
	}
	public String getItemRegDivCd() {
		return itemRegDivCd;
	}
	public void setItemRegDivCd(String itemRegDivCd) {
		this.itemRegDivCd = itemRegDivCd;
	}
	public String getExusItemDivCd() {
		return exusItemDivCd;
	}
	public void setExusItemDivCd(String exusItemDivCd) {
		this.exusItemDivCd = exusItemDivCd;
	}
	public String getExusItemDtlCd() {
		return exusItemDtlCd;
	}
	public void setExusItemDtlCd(String exusItemDtlCd) {
		this.exusItemDtlCd = exusItemDtlCd;
	}
	public String getShppMthdCd() {
		return shppMthdCd;
	}
	public void setShppMthdCd(String shppMthdCd) {
		this.shppMthdCd = shppMthdCd;
	}
	public String getShppMainCd() {
		return shppMainCd;
	}
	public void setShppMainCd(String shppMainCd) {
		this.shppMainCd = shppMainCd;
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
	public String getFxprc() {
		return fxprc;
	}
	public void setFxprc(String fxprc) {
		this.fxprc = fxprc;
	}
	public String getPickuItemYn(){
		return pickuItemYn;
	}
	public void setPickuItemYn(String pickuItemYn){
		this.pickuItemYn = pickuItemYn;
	}
	public String getSrchInspStatCd() {
		return srchInspStatCd;
	}
	public void setSrchInspStatCd(String srchInspStatCd) {
		this.srchInspStatCd = srchInspStatCd;
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
