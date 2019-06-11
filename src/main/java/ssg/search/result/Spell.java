package ssg.search.result;

import com.google.gson.annotations.SerializedName;

public class Spell{
	@SerializedName("CRITN_SRCHWD_NM")
	private String critnSrchwdNm;
	@SerializedName("RPLC_KEYWD_NM")
	private String rplcKeywdNm;
	
	public String getCritnSrchwdNm() {
		return critnSrchwdNm;
	}
	public void setCritnSrchwdNm(String critnSrchwdNm) {
		this.critnSrchwdNm = critnSrchwdNm;
	}
	public String getRplcKeywdNm() {
		return rplcKeywdNm;
	}
	public void setRplcKeywdNm(String rplcKeywdNm) {
		this.rplcKeywdNm = rplcKeywdNm;
	}
}
