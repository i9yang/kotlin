package ssg.search.result;

public class BrandMaster {
	private String brandId;
	private String brandNm;
	private String brandSrchwdNm;
	private String brandDesc;
	private String brandCncepCntt;
	private String useYn;
	private String brandSyncYn;
	private String brandRegMainDivCd;
	
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
	public String getBrandSrchwdNm() {
		return brandSrchwdNm;
	}
	public void setBrandSrchwdNm(String brandSrchwdNm) {
		this.brandSrchwdNm = brandSrchwdNm;
	}
	public String getBrandDesc() {
		return brandDesc;
	}
	public void setBrandDesc(String brandDesc) {
		this.brandDesc = brandDesc;
	}
	public String getBrandCncepCntt() {
		return brandCncepCntt;
	}
	public void setBrandCncepCntt(String brandCncepCntt) {
		this.brandCncepCntt = brandCncepCntt;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getBrandSyncYn() {
		return brandSyncYn;
	}
	public void setBrandSyncYn(String brandSyncYn) {
		this.brandSyncYn = brandSyncYn;
	}
	public String getBrandRegMainDivCd() {
		return brandRegMainDivCd;
	}
	public void setBrandRegMainDivCd(String brandRegMainDivCd) {
		this.brandRegMainDivCd = brandRegMainDivCd;
	}
	@Override
	public String toString() {
		return "BrandMaster [brandId=" + brandId + ", brandNm=" + brandNm + ", brandSrchwdNm=" + brandSrchwdNm
				+ ", brandDesc=" + brandDesc + ", brandCncepCntt=" + brandCncepCntt + ", useYn=" + useYn
				+ ", brandSyncYn=" + brandSyncYn + ", brandRegMainDivCd=" + brandRegMainDivCd + "]";
	}
}
