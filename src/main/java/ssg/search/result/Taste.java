package ssg.search.result;

import com.google.gson.annotations.SerializedName;

public class Taste {
	@SerializedName("ITEM_ID")
	private String itemId;
	@SerializedName("TASTE")
	private String taste;
	@SerializedName("TASTE_TYPE")
	private String tasteType;
	@SerializedName("ORD_CONT")
	private String ordCont;
	

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getTasteType() {
		return tasteType;
	}

	public void setTasteType(String tasteType) {
		this.tasteType = tasteType;
	}
	
	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}
	
	public String getOrdCont() {
		return ordCont;
	}

	public void setOrdCont(String ordCont) {
		this.ordCont = ordCont;
	}

	@Override
	public String toString() {
		return this.itemId + ":" + this.taste + ":" + this.tasteType + ":" + this.ordCont;
	}

}
