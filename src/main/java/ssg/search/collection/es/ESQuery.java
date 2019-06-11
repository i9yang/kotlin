package ssg.search.collection.es;

import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.parameter.Parameter;

public enum ESQuery {
	SRCH_PREFIX_MALL{
		@Override public String getField(Parameter parameter) {
			return "SRCH_PREFIX";
		}

		@Override public String getValue(Parameter parameter){
			return "SCOM";
		}
	},
	SRCH_PREFIX_VIRTUAL{
		@Override public String getField(Parameter parameter) {
			return "SRCH_PREFIX";
		}

		@Override public String getValue(Parameter parameter){
			return "VIRTUAL";
		}
	},
	SRCH_PREFIX_BSHOP{
		@Override public String getField(Parameter parameter) {
			return "SRCH_PREFIX";
		}

		@Override public String getValue(Parameter parameter){
			return "BSHOP";
		}
	},
	SRCH_PREFIX_GLOBAL{
		@Override public String getField(Parameter parameter) {
			return "SRCH_PREFIX";
		}

		@Override public String getValue(Parameter parameter){
			return "GLOBAL";
		}
	},
	SRCH_PREFIX_DISP_FIX_SITE_NO{
		@Override public String getField(Parameter parameter) {
			return "SRCH_PREFIX";
		}

		@Override public String getValue(Parameter parameter){
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");

			if(strSiteNo.equals("6005")){
				return "SCOM";
			}
			else if(strSiteNo.equals("6004")){
				return "SM";
			}
			else if(strSiteNo.equals("6009")){
				return "SD";
			}
			else if(strSiteNo.equals("6001")){
				return "EM";
			}
			else if(strSiteNo.equals("6002")){
				return "TR";
			}
			else if(strSiteNo.equals("6003")){
				return "BOOTS";
			}
			else if(strSiteNo.equals("6100")){
				return "HOWDY";
			}
			else if(strSiteNo.equals("6200")){
				return "STV";
			}
			else if(strSiteNo.equals("6300")){
				return "SIV";
			}
			return "";
		}
	},
	SRCH_PREFIX_DISP{
		@Override public String getField(Parameter parameter) {
			return "SRCH_PREFIX";
		}

		@Override public String getValue(Parameter parameter){
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "6005");

			if(strSiteNo.equals("6005")){
				String value = "SCOM";

				if(strFilterSiteNo.equals("6004")){
					return value + " AND SCOM-SM";
				}
				else if(strFilterSiteNo.equals("6009")){
					return value + " AND SCOM-SD";
				}
				else if(strFilterSiteNo.equals("6001")){
					return value + " AND SCOM-EM";
				}
				else if(strFilterSiteNo.equals("6002")){
					return value + " AND SCOM-TR";
				}
				else if(strFilterSiteNo.equals("6003")){
					return value + " AND SCOM-BT";
				}
				else if(strFilterSiteNo.equals("6100")){
					return value + " AND SCOM-HD";
				}
				else if(strFilterSiteNo.equals("6200")){
					return value + " AND SCOM-TV";
				}
				else if(strFilterSiteNo.equals("6300")){
					return value + " AND SCOM-SI";
				}

				return value;
			}
			else if(strSiteNo.equals("6004")){
				return "SM";
			}
			else if(strSiteNo.equals("6009")){
				return "SD";
			}
			else if(strSiteNo.equals("6001")){
				return "EM";
			}
			else if(strSiteNo.equals("6002")){
				return "TR";
			}
			else if(strSiteNo.equals("6003")){
				return "BOOTS";
			}
			else if(strSiteNo.equals("6100")){
				return "HOWDY";
			}
			else if(strSiteNo.equals("6200")){
				return "STV";
			}
			else if(strSiteNo.equals("6300")){
				return "SIV";
			}
			return "";
		}
	},
	DISP_CTG_LST{
		@Override public String getField(Parameter parameter) {
			return "DISP_CTG_LST";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isNotEmpty(parameter.getDispCtgId())){
				return parameter.getDispCtgId();
			}else if(StringUtils.isNotEmpty(parameter.getCtgId())){
				return parameter.getCtgId();
			}

			return "";
		}
	},
	DISP_CTG_SUB_LST{
		@Override public String getField(Parameter parameter) {
			return "DISP_CTG_LST";
		}

		@Override public String getValue(Parameter parameter) {
			Joiner orJoiner = Joiner.on(" OR ").skipNulls();

			if(StringUtils.isNotEmpty(parameter.getDispCtgSubIds())){
				return orJoiner.join(parameter.getDispCtgSubIds().split("\\|"));
			}

			return "";
		}
	},
	BRAND_ID{
		@Override public String getField(Parameter parameter) {
			return "BRAND_ID";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isEmpty(parameter.getBrand()) && StringUtils.isEmpty(parameter.getBrandId())){
				return "";
			}
			StringBuilder sb = new StringBuilder();
			String[] brandList;
			if(StringUtils.isNotEmpty(parameter.getBrand())){
				brandList = parameter.getBrand().split("\\|");
			}else{
				brandList = parameter.getBrandId().split("\\|");
			}
			if(brandList.length > 0){
				for(int i = 0; i < brandList.length; i++){
					if(StringUtils.isNotEmpty(brandList[i])){
						if(i != 0 && brandList.length > 1){
							sb.append(" OR ");
						}
						sb.append(brandList[i]);
					}
				}	
			}
			return sb.toString();
		}
	},
	SALESTR_LST{
		@Override public String getField(Parameter parameter) {
			return "SALESTR_LST";
		}

		@Override public String getValue(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String emSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmSaleStrNo(), "");
			String emRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getEmRsvtShppPsblYn(),"N");
			String trSalestrNo = StringUtils.defaultIfEmpty(userInfo.getTrSaleStrNo(), "");
			String trRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getTrRsvtShppPsblYn(),"N");
			String bnSalestrNo = StringUtils.defaultIfEmpty(userInfo.getBnSaleStrNo(), "");
			String bnRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getBnRsvtShppPsblYn(),"N");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
			String hwSalestrNo = StringUtils.defaultIfEmpty(userInfo.getHwSaleStrNo(), "");
			String hwRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getHwRsvtShppPsblYn(), "N");
			String emDualzSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmDualSaleStrNo(), "0000");
			String pickuSalestr =  StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");

			//매직픽업 점포만 있을경우, picku점포코드 대체(매직픽업 없는 점포의 경우 shpp에서 걸러짐)
			if(!pickuSalestr.equals("") && StringUtils.isEmpty(parameter.getSalestrNo()) )deptSalestrNo = pickuSalestr;

			if(strSiteNo.equals("6005")){
				return "6005 OR " + deptSalestrNo + " OR " + emSalestrNo + emRsvtShppPsblYn + " OR " + trSalestrNo + trRsvtShppPsblYn + " OR " + bnSalestrNo + bnRsvtShppPsblYn + " OR " + hwSalestrNo + hwRsvtShppPsblYn + " OR " +  emSalestrNo + emDualzSalestrNo;
			}
			else if(strSiteNo.equals("6004")){
				return "6005 OR " + deptSalestrNo;
			}
			else if(strSiteNo.equals("6009")){
				return deptSalestrNo;
			}
			else if(strSiteNo.equals("6001") || strSiteNo.equals("6002")){
				return "6005 OR " + emSalestrNo + emRsvtShppPsblYn + " OR " + trSalestrNo + trRsvtShppPsblYn + " OR " + bnSalestrNo + bnRsvtShppPsblYn + " OR " +  emSalestrNo + emDualzSalestrNo;
			}
			else if(strSiteNo.equals("6100")){
				return hwSalestrNo + hwRsvtShppPsblYn;
			}
			else if(strSiteNo.equals("6200")) {
				return "6005";
			}
			else if(strSiteNo.equals("6003")){
				return "6005" +  " OR " + bnSalestrNo + bnRsvtShppPsblYn;
			}
			else if(strSiteNo.equals("6300")){
				return "6005";
			}
			return "";
		}
	},
	SALESTR_LST_MALL{
		@Override public String getField(Parameter parameter) {
			return "SALESTR_LST";
		}

		@Override public String getValue(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String emSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmSaleStrNo(), "");
			String emRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getEmRsvtShppPsblYn(),"");
			String trSalestrNo = StringUtils.defaultIfEmpty(userInfo.getTrSaleStrNo(), "");
			String trRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getTrRsvtShppPsblYn(),"");
			String bnSalestrNo = StringUtils.defaultIfEmpty(userInfo.getBnSaleStrNo(), "");
			String bnRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getBnRsvtShppPsblYn(),"N");
			String hwSalestrNo = StringUtils.defaultIfEmpty(userInfo.getHwSaleStrNo(), "");
			String hwRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getHwRsvtShppPsblYn(), "N");
			String emDualzSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmDualSaleStrNo(), "0000");
			String deptSalestrNo = "0001";

			return "6005 OR " + deptSalestrNo + " OR " + emSalestrNo + emRsvtShppPsblYn + " OR " + trSalestrNo + trRsvtShppPsblYn + " OR " + bnSalestrNo + bnRsvtShppPsblYn + " OR " + hwSalestrNo + hwRsvtShppPsblYn + " OR " +  emSalestrNo + emDualzSalestrNo;
		}
	},
	SIZE{
		@Override public String getField(Parameter parameter) {
			return "SIZE_LST";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isEmpty(parameter.getSize())){
				return "";	
			}
			StringBuilder sb = new StringBuilder();
			String[] sizeList = null;
			sizeList = parameter.getSize().split("\\|");
			if(sizeList.length > 0){
				for(int i = 0; i < sizeList.length; i++){
					if(StringUtils.isNotEmpty(sizeList[i])){
						if(i != 0 && sizeList.length > 1){
							sb.append(" OR ");
						}
						sb.append(sizeList[i]);
					}
				}	
			}
			return sb.toString();
		}
	},
	COLOR{
		@Override public String getField(Parameter parameter) {
			return "COLOR_LST";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isEmpty(parameter.getColor())){
				return "";	
			}
			StringBuilder sb = new StringBuilder();
			String[] colorList = null;
			colorList = parameter.getColor().split("\\|");
			if(colorList.length > 0){
				for(int i = 0; i < colorList.length; i++){
					if(StringUtils.isNotEmpty(colorList[i])){
						if(i != 0 && colorList.length > 1){
							sb.append(" OR ");
						}
						sb.append(colorList[i]);
					}
				}	
			}
			return sb.toString();
		}
	},
	DEVICE_CD{
		@Override public String getField(Parameter parameter) {
			return "DISP_APL_RNG_TYPE_CD";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isNotEmpty(parameter.getAplTgtMediaCd())){
				return parameter.getAplTgtMediaCd();
			}
			return "";
		}
	},
	MBR_CO_TYPE{
		@Override public String getField(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String type = StringUtils.defaultIfEmpty(userInfo.getMbrType(), "");
            if(type.equals("B2C")){
                return "APL_TGT_VEN_LST";
            }else if(type.equals("B2E")){
                return "APL_B2E_MBRCO_LST";
            }
			return "APL_TGT_VEN_LST";
		}

		@Override public String getValue(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String type = StringUtils.defaultIfEmpty(userInfo.getMbrType(), "");
			String value = "";
			if(type.equals("B2E")){
				value += "B2E OR ";
			}

			if(StringUtils.isNotEmpty(userInfo.getMbrCoId())){
                value += userInfo.getMbrCoId();
            }
			return value;
		}
	},
	SPL_VEN_ID{
		@Override public String getField(Parameter parameter) {
			return "SPL_VEN_ID";
		}

		@Override public String getValue(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getSplVenId())){
				return parameter.getSplVenId();
			}
			return "";
		}
	},
	LRNK_SPL_VEN_ID{
		@Override public String getField(Parameter parameter) {
			return "LRNK_SPL_VEN_ID";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isNotEmpty(parameter.getSplVenId())){
				return parameter.getSplVenId();
			}
			return "";
		}
	},
	SCOM_EXPSR_YN{
		@Override public String getField(Parameter parameter) {
			return "SCOM_EXPSR_YN";
		}

		@Override public String getValue(Parameter parameter) {
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			if(strSiteNo.equals("6005") && ( strFilterSiteNo.equals("") || strFilterSiteNo.equals("6005")) ){
				return "Y";
			}
			return "";
		}
	},
	PRC_FILTER{
		@Override public String getField(Parameter parameter) {
			return "SELLPRC";
		}

		@Override public String getValue(Parameter parameter) {
			String strMinPrc = StringUtils.defaultIfEmpty(parameter.getMinPrc(), "*");
			String strMaxPrc = StringUtils.defaultIfEmpty(parameter.getMaxPrc(), "*");

			if (StringUtils.equals(strMinPrc, "*") && StringUtils.equals(strMaxPrc, "*")) {
				return "";
			} else {
				return "[ " + strMinPrc + " TO " + strMaxPrc + " ]";
			}
		}
	},
	GRP_ADDR_ID{
		@Override public String getField(Parameter parameter) {
			return "GRP_ADDR_ID";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isNotEmpty(parameter.getGrpAddrId())){
				return parameter.getGrpAddrId();
			}
			return "";
		}
	},
	SHPPCST_ID{
		@Override public String getField(Parameter parameter) {
			return "SHPPCST_ID";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isNotEmpty(parameter.getShppcstId())){
				return parameter.getShppcstId();
			}
			return "";
		}
	},
	ITEM_SITE_NO{
		@Override public String getField(Parameter parameter) {
			return "SITE_NO";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.equals(parameter.getTarget(), "es_bundle") && !StringUtils.equals(parameter.getSiteNo(), "6005") && StringUtils.isNotEmpty(parameter.getItemSiteNo())){
				return parameter.getItemSiteNo();
			}
			return "";
		}
	},
	BSHOP_ID{
		@Override public String getField(Parameter parameter) {
			return "BSHOPID_LST";
		}

		@Override public String getValue(Parameter parameter) {
			if(StringUtils.isNotEmpty(parameter.getBshopId())){
				return parameter.getBshopId();
			}
			return "";
		}
	};

	public abstract String getValue(Parameter parameter);
	public abstract String getField(Parameter parameter);
}

