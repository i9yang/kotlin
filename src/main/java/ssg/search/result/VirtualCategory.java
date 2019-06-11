package ssg.search.result;

import com.google.gson.annotations.SerializedName;

public class VirtualCategory{
	@SerializedName("VIRTUAL_CTG_ID")
	private String virtualCtgId;
	@SerializedName("VIRTUAL_CTG_NM")
	private String virtualCtgNm;
	
	public String getVirtualCtgId() {
		return virtualCtgId;
	}
	public void setVirtualCtgId(String virtualCtgId) {
		this.virtualCtgId = virtualCtgId;
	}
	public String getVirtualCtgNm() {
		return virtualCtgNm;
	}
	public void setVirtualCtgNm(String virtualCtgNm) {
		this.virtualCtgNm = virtualCtgNm;
	}
}
