package ssg.search.constant;

@Deprecated
public enum SORT {
	SALE("SALE_QTY",1),
	SCR("RECOM_EVAL_SCR",1),
	CNT("RECOM_EVAL_CNT",1),
	COMMENT("COMMENT_CNT",1),
	REGDT("ITEM_REG_DT",1),
	RATE("RATE",1),
	PRCDSC("SELLPRC",1),
	PRCASC("SELLPRC",0),
	BEST("BEST",1)
	;
	
	String sortName;
	int order;
	
	SORT(String sortName,int order){
		this.sortName = sortName;
		this.order = order;
	}
	
	public String getSortName(){
		return this.sortName; 
	}
	public int getOrder(){
		return this.order;
	}
	
}
