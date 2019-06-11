package ssg.search.constant;

import ssg.search.base.Sort;
import ssg.search.parameter.Parameter;

public enum Sorts {
	WEIGHT{
		public Sort getSort(Parameter parameter){
			return new Sort("WEIGHT",1);
		}
	},
	RANK{
		public Sort getSort(Parameter parameter){
			return new Sort("RANK",1);
		}
	},
	SALE{
		public Sort getSort(Parameter parameter){
			return new Sort("SALE_QTY",1);
		}
	},
	SCR{
		public Sort getSort(Parameter parameter){
			return new Sort("RECOM_EVAL_SCR",1);
		}
	},
	CNT{
		public Sort getSort(Parameter parameter){
			return new Sort("RECOM_EVAL_CNT",1);
		}
	},
	REGDT{
		public Sort getSort(Parameter parameter){
			return new Sort("ITEM_REG_DT",1);
		}
	},
	RATE{
		public Sort getSort(Parameter parameter){
			return new Sort("RATE",1);
		}
	},
	PRCDSC{
		public Sort getSort(Parameter parameter){
			return new Sort("SELLPRC",1);
		}
	},
	PRCASC{
		public Sort getSort(Parameter parameter){
			return new Sort("SELLPRC",0);
		}
	},
	UPRCASC{
		public Sort getSort(Parameter parameter){
			return new Sort("UNIT_PRC",0);
		}
	},
	DISP_BEST{
		public Sort getSort(Parameter parameter){
			String strSiteNo = parameter.getSiteNo();
			if(strSiteNo!=null){
				if(strSiteNo.equals("6004") || strSiteNo.equals("6009") || strSiteNo.equals("6300") ){
					return new Sort("SHIN_DISP_BEST_SCR",1);
				}else if(strSiteNo.equals("6001") || strSiteNo.equals("6002") || strSiteNo.equals("6003") || strSiteNo.equals("6200") || strSiteNo.equals("6100")){  //@ BOOT개발시확인
					return new Sort("EMART_DISP_BEST_SCR",1);
				}else if(strSiteNo.equals("6005")){
					return new Sort("SCOM_DISP_BEST_SCR",1);
				}
			}
			return null;
		}
	},
	BEST_SCR{
		public Sort getSort(Parameter parameter){
			return new Sort("SRCH_TYPE_THRD_SCR",1);
		}
	},
	BEST{
		public Sort getSort(Parameter parameter){
			return null;
		}
	},
	WRT_DATE{
		public Sort getSort(Parameter parameter) {
			return new Sort("WRT_DATE",1);
		}
	},
	DISP_STRT_DTS{
		public Sort getSort(Parameter parameter) {
			return new Sort("DISP_STRT_DTS",1);
		}
	},
	VIRTUAL_CNT{
		public Sort getSort(Parameter parameter) {
			return new Sort("CNT",1);
		}
	},
	THRD{
		public Sort getSort(Parameter parameter) {
			return new Sort("SRCH_TYPE_THRD_SCR",1);
		}
	},
	TRECIPE_BEST_SCR{
		public Sort getSort(Parameter parameter) {
			return new Sort("TRECIPE_BEST_SCR",0);
		}
	},
	REG_DTS{
		public Sort getSort(Parameter parameter) {
			return new Sort("REG_DTS",1);
		}
	},
	CTGORDR{
		public Sort getSort(Parameter parameter) {
			return new Sort("DISP_CTG_ORDR.DISP_ORDR",0);
		}
	}
	;
	
	public abstract Sort getSort(Parameter parameter);
}