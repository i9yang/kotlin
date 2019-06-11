package ssg.search.result;

import com.google.gson.annotations.SerializedName;

public class SrchwdRl {
	@SerializedName("SRCHWD_NM")
    private String srchwdNm;
	@SerializedName("RL_KEYWD_NM")
    private String rlKeywdNm;
    
    public String getSrchwdNm() {
        return srchwdNm;
    }
    public void setSrchwdNm(String srchwdNm) {
        this.srchwdNm = srchwdNm;
    }
    public String getRlKeywdNm() {
        return rlKeywdNm;
    }
    public void setRlKeywdNm(String rlKeywdNm) {
        this.rlKeywdNm = rlKeywdNm;
    }
}
