package ssg.search.result;

public class Brand{
	private String brandId;
	private String brandNm;
	private String itemCount;
	private String recomYn = "N";

	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandNm() {
		return brandNm;
	}
	public void setBrandNm(String brandNm) {
		this.brandNm = brandNm;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}

	public String getRecomYn() {
		return recomYn;
	}

	public void setRecomYn(String recomYn) {
		this.recomYn = recomYn;
	}
}
