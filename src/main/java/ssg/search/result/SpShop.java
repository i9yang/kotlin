package ssg.search.result;

public class SpShop implements ItemData{
	private String itemId;
	private String siteNo;
	private String itemNm;
	private String sellprc;
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
	public String getSellprc(){
		return sellprc;
	}
	public void setSellprc(String sellprc){
		this.sellprc = sellprc;
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
}
