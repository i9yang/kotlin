package ssg.search.constant;

@Deprecated
public enum TARGET {
	ALL("all"),
	ITEM("item"),
	SHOP("shop"),
	CATEGORY("category"),
	POSTNG("postng"),
	FAQ("faq"),
	RECIPE("recipe"),
	BOOK("book"),
    RECOM("rsearch"),
	MOBILE("mobile"),
	MOBILE_BOOK("mobi" +
			"le_book"),
	PNSHOP("pnshop"),
	SRCHGUIDE("srchguide"),
	SPSHOP("spshop"),
    DISP("book"),
	BRAND_DISP("brand_disp"),
	VIRTUAL("virtual"),
	BRAND_DTL("brand_dtl"),
	PERSON("person"),
	EVENT("event"),
	NOTICE("notice"),
	MOBILE_ITEM("mobile_item"),
	PARTNER("partner"),
	GLOBAL("global"),
	GOLBAL_CATEGORY("golbal_category"),
	BSHOP("bshop"),
	BSHOP_BRAND("bshop_brand"),
	BSHOP_ITEM("bshop_item"),
	BSHOP_DISP("bshop_disp"),
	BSHOP_DISP_ITEM("bshop_disp_item"),
	LIFEMAGAZINE("lifemagazine"),
	MOBILE_DTL("mobile_dtl"),
	ES_DISP("es_disp"),
    ES_DISP_ITEM("es_disp_item"),
	ES_BRAND_DISP("es_brand_disp"),
	ES_PSNZ_BRAND_ITEM("es_psnz_brand_item"),
	ES_GLOBAL_CATEGORY("es_global_category"),
	ES_GLOBAL_BRAND("es_global_brand"),
	ES_BSHOP_DISP("es_bshop_disp"),
	ES_BSHOP_DISP_ITEM("es_bshop_disp_item"),
	ES_BUNDLE("es_bundle"),
	ES_SPCSHOP("es_spcshop"),
	SPPRICE("spprice"),
	ISSUETHEME("issuetheme"),
	CHAT_VEN_ITEMS("chat_ven_items"),
	MOBILE_TASTE("mobile_taste"),
	AD_CPC("ad_cpc"),
	AD_CPC_EXT("ad_cpc_ext"),
	STARFIELD("starfield")
	;

	private String value;

	TARGET(String value) {
		this.value = value;
	}

	@Override public String toString() {
		return this.value;
	}
}
