package ssg.search.constant;

import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.parameter.Parameter;

import java.util.StringTokenizer;

public enum Prefixes {
	SRCH_PREFIX_MALL{
		public String getPrefix(Parameter parameter){
			return "<SRCH_PREFIX:contains:SCOM>";
		}
	},
	SRCH_PREFIX{
		public String getPrefix(Parameter parameter){
			if(!StringUtils.isBlank(parameter.getSiteNo())){
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
				String mobileAppType = StringUtils.defaultIfEmpty(parameter.getMobileAppType(), "");

				if(strSiteNo.equals("6005")){
					return "<SRCH_PREFIX:contains:SCOM>";
				}
				else if(strSiteNo.equals("6004")){
					return "<SRCH_PREFIX:contains:SHIN>";
				}
				else if(strSiteNo.equals("6009")){
					return "<SRCH_PREFIX:contains:SD>";
				}
				else if(strSiteNo.equals("6001") || strSiteNo.equals("6002") ){
					if(mobileAppType.equals("41")){ //삼성냉장고 앱일경우(only 점포상품만, 딜제외)
						return "<SRCH_PREFIX:contains:FRG>";
					}
					
					String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
					if(!strFilterSiteNo.equals("")){
						return "";
					}
					return "<SRCH_PREFIX:contains:EMALL>";
				}else if(strSiteNo.equals("6100")){
					return "<SRCH_PREFIX:contains:HOWDY>";
				}else if(strSiteNo.equals("6200")){
					return "<SRCH_PREFIX:contains:STV>";
				}
				else if(strSiteNo.equals("6003")){ 
					return "<SRCH_PREFIX:contains:BOOTS>";
				}
				else if(strSiteNo.equals("6300")){ 
					return "<SRCH_PREFIX:contains:SIV>";
				}
			}
			return "";
		}
	},
	SRCH_PREFIX_GLOBAL{
		public String getPrefix(Parameter parameter){
			return "<SRCH_PREFIX:contains:GLOBAL>";
		}
	},
	SRCH_PREFIX_BSHOP{
		public String getPrefix(Parameter parameter){
			return "<SRCH_PREFIX:contains:BSHOP>";
		}
	},
	SRCH_PREFIX_DISP{
		public String getPrefix(Parameter parameter){
			if(!StringUtils.isBlank(parameter.getSiteNo())){
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
				if(strSiteNo.equals("6005")){
					return "<SRCH_PREFIX:contains:SCOM>";
				}
				else if(strSiteNo.equals("6004")){
					return "<SRCH_PREFIX:contains:SM>";
				}
				else if(strSiteNo.equals("6009")){
					return "<SRCH_PREFIX:contains:SD>";
				}
				else if(strSiteNo.equals("6001")){
					return "<SRCH_PREFIX:contains:EM>";
				}
				else if(strSiteNo.equals("6002")){
					return "<SRCH_PREFIX:contains:TR>";
				}
				else if(strSiteNo.equals("6003")){
					return "<SRCH_PREFIX:contains:BOOTS>";
				}
			}
			return "";
		}
	},
	FILTER_SITE_NO{
		public String getPrefix(Parameter parameter){
			if(!StringUtils.isBlank(parameter.getFilterSiteNo())){
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
				String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "6005");
				if(strSiteNo.equals("6005")){
					if(strFilterSiteNo.equals("6004")){
						return "<SRCH_PREFIX:contains:SCOM-SM>";
					}
					else if(strFilterSiteNo.equals("6009")){
						return "<SRCH_PREFIX:contains:SCOM-SD>";
					}
					else if(strFilterSiteNo.equals("6001")){
						return "<SRCH_PREFIX:contains:SCOM-EM>";
					}
					else if(strFilterSiteNo.equals("6002")){		
						return "<SRCH_PREFIX:contains:SCOM-TR>";
					}
					else if(strFilterSiteNo.equals("6003")){
						return "<SRCH_PREFIX:contains:SCOM-BT>";
					}
					else if(strFilterSiteNo.equals("6100")){
						return "<SRCH_PREFIX:contains:SCOM-HD>";
					}
					else if(strFilterSiteNo.equals("6200")){
						return "<SRCH_PREFIX:contains:SCOM-TV>";
					}
					else if(strFilterSiteNo.equals("6300")){
						return "<SRCH_PREFIX:contains:SCOM-SI>";
					}
				}else if(strSiteNo.equals("6001")||strSiteNo.equals("6002")){
					if(strFilterSiteNo.equals("6001")){
						return "<SRCH_PREFIX:contains:EM>";
					}
					else if(strFilterSiteNo.equals("6002")){
						return "<SRCH_PREFIX:contains:TR>";
					}
					else if(strFilterSiteNo.equals("6200")){
						return "<SRCH_PREFIX:contains:STV>";
					}
				}
			}
			return "";
		}
	},
	SITE_NO_USE_FILTER{
		public String getPrefix(Parameter parameter) {
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			if(!strFilterSiteNo.equals("")){
				return "<SITE_NO:contains:".concat(strFilterSiteNo).concat(">");
			}else{
				return "<SITE_NO:contains:".concat(strSiteNo).concat(">");
			}
		}
	},
	SITE_NO_ONLY{
		public String getPrefix(Parameter parameter) {
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			return "<SITE_NO:contains:".concat(strSiteNo).concat(">");
		}
	},
	SALESTR_LST{
		public String getPrefix(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String prefix = "<SALESTR_LST:contains:";
			String emSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmSaleStrNo(), "");
			String emRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getEmRsvtShppPsblYn(),"N");
			String trSalestrNo = StringUtils.defaultIfEmpty(userInfo.getTrSaleStrNo(), "");
			String trRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getTrRsvtShppPsblYn(),"N");
			String bnSalestrNo = StringUtils.defaultIfEmpty(userInfo.getBnSaleStrNo(), "");
			String bnRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getBnRsvtShppPsblYn(),"N");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			String deptSalestrNo = StringUtils.defaultIfEmpty(parameter.getSalestrNo(), "0001");
			String pickuSalestr =  StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");
			String hwSalestrNo = StringUtils.defaultIfEmpty(userInfo.getHwSaleStrNo(), "");
			String hwRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getHwRsvtShppPsblYn(), "N");
			String mobileAppType = StringUtils.defaultIfEmpty(parameter.getMobileAppType(), "");
			String emDualzSalestrNo = emSalestrNo + StringUtils.defaultIfEmpty(userInfo.getEmDualSaleStrNo(), "0000");
			
			//매직픽업 점포만 있을경우, picku점포코드 대체(매직픽업 없는 점포의 경우 shpp에서 걸러짐)
			if(!pickuSalestr.equals("") && StringUtils.isEmpty(parameter.getSalestrNo()) )deptSalestrNo = pickuSalestr;

			if(parameter.getTarget().equalsIgnoreCase("chat_ven_items")){
				return prefix + "6005|" + deptSalestrNo + ">";
			}
			
			if(strSiteNo.equals("6005")){
				return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" +hwSalestrNo + hwRsvtShppPsblYn + "|" + emDualzSalestrNo + ">"; 
			}
			else if(strSiteNo.equals("6004")){
				return prefix + "6005|" + deptSalestrNo  + ">";
			}
			else if(strSiteNo.equals("6009")){
				return prefix + deptSalestrNo + ">";
			}
			
			else if(strSiteNo.equals("6001")){
				//삼성냉장고 앱일경우 온라인생품 제외
				if(mobileAppType.equals("41")){
					return prefix + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
				}
				return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
			}
			else if(strSiteNo.equals("6002")){
				if(StringUtils.isBlank(parameter.getFilterSiteNo())){
					return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
				}
				return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
			}
			else if(strSiteNo.equals("6100")){
				return prefix +  hwSalestrNo + hwRsvtShppPsblYn  + ">";
			}
			else if(strSiteNo.equals("6200")){
				return prefix +  "6005" + ">";
			}
			else if(strSiteNo.equals("6003")){
				return prefix +  "6005|" + bnSalestrNo + bnRsvtShppPsblYn +">";
			}
			else if(strSiteNo.equals("6300")){
				return prefix +  "6005" + ">";
			}
			return "";
		}
	},
	SALESTR_LST_GROUP{
		public String getPrefix(Parameter parameter) {
			// 그룹핑 대상은 지점 검색으로 인해 필터링 되지 않도록 한다.
			FrontUserInfo userInfo = parameter.getUserInfo();
			String prefix = "<SALESTR_LST:contains:";
			String emSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmSaleStrNo(), "");
			String emRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getEmRsvtShppPsblYn(),"");
			String trSalestrNo = StringUtils.defaultIfEmpty(userInfo.getTrSaleStrNo(), "");
			String trRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getTrRsvtShppPsblYn(),"");
			String bnSalestrNo = StringUtils.defaultIfEmpty(userInfo.getBnSaleStrNo(), "");
			String bnRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getBnRsvtShppPsblYn(),"N");
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			String deptSalestrNo = "0001";
			String hwSalestrNo = StringUtils.defaultIfEmpty(userInfo.getHwSaleStrNo(), "");
			String hwRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getHwRsvtShppPsblYn(), "N");
			String mobileAppType = StringUtils.defaultIfEmpty(parameter.getMobileAppType(), "");
			String emDualzSalestrNo = emSalestrNo + StringUtils.defaultIfEmpty(userInfo.getEmDualSaleStrNo(), "0000");
			
			if(parameter.getTarget().equalsIgnoreCase("chat_ven_items")){
				return prefix + "6005|" + deptSalestrNo + ">";
			}

			if(strSiteNo.equals("6005")){
				return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" +hwSalestrNo + hwRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
			}
			else if(strSiteNo.equals("6004") && deptSalestrNo.equals("0001")){
				return prefix + "6005|" + deptSalestrNo  + ">";
			}
			else if(strSiteNo.equals("6004") && !deptSalestrNo.equals("0001")){
				return prefix + deptSalestrNo + ">";
			}
			else if(strSiteNo.equals("6009")){
				return prefix + deptSalestrNo + ">";
			}
			else if(strSiteNo.equals("6001")){
				//삼성냉장고 앱일경우 온라인생품 제외
				if(mobileAppType.equals("41")){
					return prefix + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
				}
				return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
			}
			else if(strSiteNo.equals("6002")){
				if(StringUtils.isBlank(parameter.getFilterSiteNo())){
					return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
				}
				return prefix + "6005|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
			}
			else if(strSiteNo.equals("6100")){
				return prefix +  hwSalestrNo + hwRsvtShppPsblYn + ">";
			}
			else if(strSiteNo.equals("6200")){
				return prefix +  "6005" + ">";
			}
			else if(strSiteNo.equals("6003")){
				return prefix +  "6005|" + bnSalestrNo + bnRsvtShppPsblYn +">";
			}
			else if(strSiteNo.equals("6300")){
				return prefix +  "6005" + ">";
			}
			return "";
		}
	},
	SALESTR_LST_MALL{
		public String getPrefix(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String prefix = "<SALESTR_LST:contains:";
			String emSalestrNo = StringUtils.defaultIfEmpty(userInfo.getEmSaleStrNo(), "");
			String emRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getEmRsvtShppPsblYn(),"");
			String trSalestrNo = StringUtils.defaultIfEmpty(userInfo.getTrSaleStrNo(), "");
			String trRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getTrRsvtShppPsblYn(),"");
			String bnSalestrNo = StringUtils.defaultIfEmpty(userInfo.getBnSaleStrNo(), "");
			String bnRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getBnRsvtShppPsblYn(),"N");
			String hwSalestrNo = StringUtils.defaultIfEmpty(userInfo.getHwSaleStrNo(), "");
			String hwRsvtShppPsblYn = StringUtils.defaultIfEmpty(userInfo.getHwRsvtShppPsblYn(), "N");
			String deptSalestrNo = "0001";
			String emDualzSalestrNo = emSalestrNo + StringUtils.defaultIfEmpty(userInfo.getEmDualSaleStrNo(), "0000");
			return prefix + "6005|" + deptSalestrNo + "|" + emSalestrNo + emRsvtShppPsblYn + "|" + trSalestrNo + trRsvtShppPsblYn + "|" + bnSalestrNo + bnRsvtShppPsblYn + "|" +hwSalestrNo + hwRsvtShppPsblYn + "|" + emDualzSalestrNo + ">";
		}
	},
	MBR_CO_TYPE{
		public String getPrefix(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String type = StringUtils.defaultIfEmpty(userInfo.getMbrType(), "");
			String coId = StringUtils.defaultIfEmpty(userInfo.getMbrCoId(), "");
			if(!type.equals("") && !coId.equals("")){
                if(type.equals("B2C")){
                    return "<APL_TGT_VEN_LST:contains:".concat(coId).concat(">");
                }else if(type.equals("B2E")){
                	return "<APL_B2E_MBRCO_LST:contains:B2E|".concat(coId).concat(">");
                }
            }
			return "";
		}
	},
	MBR_CO_TYPE_CONTENTS{
		public String getPrefix(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String type = StringUtils.defaultIfEmpty(userInfo.getMbrType(), "");
			String coId = StringUtils.defaultIfEmpty(userInfo.getMbrCoId(), "");
			if(!type.equals("") && !coId.equals("")){
				if(type.equals("B2C")){
					return "<APL_B2C_LST:contains:".concat(coId).concat(">");
				}else if(type.equals("B2E")){
					return "<APL_B2E_LST:contains:B2E|".concat(coId).concat(">");
				}
			}
			return "";
		}
	},
	SPL_VEN_ID{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getSplVenId())){
				return "<SPL_VEN_ID:contains:".concat(parameter.getSplVenId()).concat(">");
			}
			return "";
		}
	},
	LRNK_SPL_VEN_ID{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getLrnkSplVenId())){
				return "<LRNK_SPL_VEN_ID:contains:".concat(parameter.getLrnkSplVenId()).concat(">");
			}
			return "";
		}
	},
	BRAND_ID{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getBrand())){
				return "<BRAND_ID:contains:".concat(parameter.getBrand()).concat(">");
			}
			if(!StringUtils.isBlank(parameter.getBrandId())){
				return "<BRAND_ID:contains:".concat(parameter.getBrandId()).concat(">");
			}
			return "";
		}
	},
	BSHOP_ID{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getBshopId())){
				return "<BSHOPID_LST:contains:".concat(parameter.getBshopId()).concat(">");
			}
			return "";
		}
	},
	DISP_CTG_LST{
		public String getPrefix(Parameter parameter) {
			if(!parameter.getTarget().equalsIgnoreCase("all")){
				if(!StringUtils.isBlank(parameter.getDispCtgId())){
					return "<DISP_CTG_LST:contains:".concat(parameter.getDispCtgId()).concat(">");
				}else if(!StringUtils.isBlank(parameter.getCtgId())){
					return "<DISP_CTG_LST:contains:".concat(parameter.getCtgId()).concat(">");
				}
			}
			return "";
		}
	},
	TEM_DISP_CTG_ID{
		public String getPrefix(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
		    StringBuilder ctgNmPrefix = new StringBuilder();
		    String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
		    String ctgId 	= StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
		    String parentCtgId     = StringUtils.defaultIfEmpty(parameter.getParentCtgId(),"");  
		    String ctgIds 	= StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
		    String ctgLv 	= StringUtils.defaultIfEmpty(parameter.getCtgLv(), "1");
		    String ctgLast 	= StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
		    
		    String themeYn 	= StringUtils.defaultIfEmpty(parameter.getThemeYn(), "");
		    // CTG IDS 부터
		    // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
	        if(themeYn.equals("Y")){
	            ctgNmPrefix.append("TEM_");
	        }else if(strSiteNo.equals("6005")){
	        	ctgNmPrefix.append("SCOM_");
	        }
	        if(ctgLv.equals("1")){
	        	 if(ctgLast.equals("Y"))ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID");
	        	 else ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID");
            }else if(ctgLv.equals("2")){
            	if(ctgLast.equals("Y"))ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID");
            	else ctgNmPrefix.append("TEM_DISP_CTG_MCLS_ID");
            }else if(ctgLv.equals("3")){
            	if(ctgLast.equals("Y"))ctgNmPrefix.append("TEM_DISP_CTG_MCLS_ID");
            	else ctgNmPrefix.append("TEM_DISP_CTG_SCLS_ID");
            }else if(ctgLv.equals("4")){
            	if(ctgLast.equals("Y"))ctgNmPrefix.append("TEM_DISP_CTG_SCLS_ID");
            	else ctgNmPrefix.append("TEM_DISP_CTG_DCLS_ID");
            }else{
                ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID");
            }
		    if(!ctgIds.equals("")){
		    	if(ctgLast.equals("Y")){
		    		ctgId= parentCtgId;
		    		prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">");
		    	}else{
			        int operatorIdx = 0;
			        if(!ctgIds.equals("")){
			            prefix.append("<").append(ctgNmPrefix).append(":contains:");
			            int i=0;
			            for(StringTokenizer st = new StringTokenizer(ctgIds,"|");st.hasMoreTokens();){
			                String c = st.nextToken();
			                if(i>0)prefix.append("|");
			                prefix.append(c);
			                i++;
			            }
			            prefix.append(">");
			            operatorIdx++;
			        }
		    	}
	        }else if(!ctgId.equals("")){
	        	if(ctgLast.equals("Y")) ctgId= parentCtgId;
		        prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">");
		    }
		    return prefix.toString();
		}
	},
	TEM_DISP_ITEM_CTG_ID{
		public String getPrefix(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
		    StringBuilder ctgNmPrefix = new StringBuilder();
		    String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
		    String ctgId 	= StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
		    String parentCtgId     = StringUtils.defaultIfEmpty(parameter.getParentCtgId(),"");  
		    String ctgIds 	= StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
		    String ctgLv 	= StringUtils.defaultIfEmpty(parameter.getCtgLv(), "1");
		    String ctgLast 	= StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
		    
		    String themeYn 	= StringUtils.defaultIfEmpty(parameter.getThemeYn(), "");
		    // CTG IDS 부터
		    // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
	        if(themeYn.equals("Y")){
	            ctgNmPrefix.append("TEM_");
	        }else if(strSiteNo.equals("6005")){
	        	ctgNmPrefix.append("SCOM_");
	        }
	        if(ctgLv.equals("1")){
	        	 ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID");
            }else if(ctgLv.equals("2")){
            	ctgNmPrefix.append("TEM_DISP_CTG_MCLS_ID");
            }else if(ctgLv.equals("3")){
            	ctgNmPrefix.append("TEM_DISP_CTG_SCLS_ID");
            }else if(ctgLv.equals("4")){
            	ctgNmPrefix.append("TEM_DISP_CTG_DCLS_ID");
            }else{
                ctgNmPrefix.append("TEM_DISP_CTG_LCLS_ID");
            }
		    if(!ctgIds.equals("")){
		        int operatorIdx = 0;
		        if(!ctgIds.equals("")){
		            prefix.append("<").append(ctgNmPrefix).append(":contains:");
		            int i=0;
		            for(StringTokenizer st = new StringTokenizer(ctgIds,"|");st.hasMoreTokens();){
		                String c = st.nextToken();
		                if(i>0)prefix.append("|");
		                prefix.append(c);
		                i++;
		            }
		            prefix.append(">");
		            operatorIdx++;
		        }
	        }else if(!ctgId.equals("")){
		        prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">");
		    }
		    return prefix.toString();
		}
	},
	DEVICE_CD{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getAplTgtMediaCd())){
				return "<DISP_APL_RNG_TYPE_CD:contains:".concat(parameter.getAplTgtMediaCd()).concat(">");
			}
			return "";
		}
	},
	DVIC_DIV_CD{
		public String getPrefix(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String dvicDivCd = StringUtils.defaultIfEmpty(userInfo.getDeviceDivCd(), "10");
			if(!dvicDivCd.equals("10")){
				dvicDivCd = "20";
			}
			return "<DVIC_DIV_CD:contains:".concat(dvicDivCd).concat(">");
		}
	},
	COLOR{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getColor())){
				return "<COLOR_LST:contains:".concat(parameter.getColor()).concat(">");
			}
			return "";
		}
	},
	SIZE{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getSize())){
				return "<SIZE_LST:contains:".concat(parameter.getSize()).concat(">");
			}
			return "";
		}
	},
	BSHOP{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getBshopId())){
				return "<BSHOP_ID:contains:".concat(parameter.getBshopId()).concat(">");
			}
			return "";
		}
	},
	SRCH_PSBL_YN{
		public String getPrefix(Parameter parameter) {
			//업체 채팅은 검색가능여부가 N 이더라도 노출한다.
			String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "");
			if(strTarget.equalsIgnoreCase("chat_ven_items")){
				return "";
			}
			return "<SRCH_PSBL_YN:contains:Y>";
		}
	},
	SCOM_EXPSR_YN{
		public String getPrefix(Parameter parameter) {
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			if(strSiteNo.equals("6005") && ( strFilterSiteNo.equals("") || strFilterSiteNo.equals("6005")) ){
				return "<SCOM_EXPSR_YN:contains:Y>";
			}
			return "";
		}
	},
	SCOM_EXPSR_YN_ALL{
		public String getPrefix(Parameter parameter) {
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
			if(strSiteNo.equals("6005")){
				return "<SCOM_EXPSR_YN:contains:Y>";
			}
			return "";
		}
	},
	SCOM_EXPSR_YN_SSG{
		public String getPrefix(Parameter parameter) {
			return "<SCOM_EXPSR_YN:contains:Y>";
		}
	},
	SRCH_CTG_PREFIX{
		public String getPrefix(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
		    StringBuilder ctgNmPrefix = new StringBuilder();
		    String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
		    String ctgId 	= StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
		    String parentCtgId 	= StringUtils.defaultIfEmpty(parameter.getParentCtgId(), "");
		    String ctgIds 	= StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
		    String ctgLv 	= StringUtils.defaultIfEmpty(parameter.getCtgLv(), "1");
		    String ctgLast 	= StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
		    
		    String themeYn 	= StringUtils.defaultIfEmpty(parameter.getThemeYn(), "");
		    String themeCtgIds = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");
		    // CTG IDS 부터
		    // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
	        if(themeYn.equals("Y")){
	            ctgNmPrefix.append("TEM_");
	        }else if(strSiteNo.equals("6005")){
	        	ctgNmPrefix.append("SCOM_");
	        }

	        if(ctgLast.equals("Y") && !parentCtgId.equals("")){
	        	ctgId = parentCtgId;
	        }

	        if(ctgLv.equals("1")){
                if(ctgLast.equals("Y"))ctgNmPrefix.append("DISP_CTG_LCLS_ID");
                else ctgNmPrefix.append("DISP_CTG_LCLS_ID");
            }else if(ctgLv.equals("2")){
                if(ctgLast.equals("Y"))ctgNmPrefix.append("DISP_CTG_LCLS_ID");
                else ctgNmPrefix.append("DISP_CTG_MCLS_ID");
            }else if(ctgLv.equals("3")){
                if(ctgLast.equals("Y"))ctgNmPrefix.append("DISP_CTG_MCLS_ID");
                else ctgNmPrefix.append("DISP_CTG_SCLS_ID");
            }else if(ctgLv.equals("4")){
                if(ctgLast.equals("Y"))ctgNmPrefix.append("DISP_CTG_SCLS_ID");
                else ctgNmPrefix.append("DISP_CTG_DCLS_ID");
            }else{
                ctgNmPrefix.append("DISP_CTG_LCLS_ID");
            }
		    if(!ctgIds.equals("")||!themeCtgIds.equals("")){
		        int operatorIdx = 0;
		        if(!ctgIds.equals("")){
		            prefix.append("<").append(ctgNmPrefix).append(":contains:");
		            int i=0;
		            for(StringTokenizer st = new StringTokenizer(ctgIds,"|");st.hasMoreTokens();){
		                String c = st.nextToken();
		                if(i>0)prefix.append("|");
		                prefix.append(c);
		                i++;
		            }
		            prefix.append(">");
		            operatorIdx++;
		        }
		        if(!themeCtgIds.equals("")){
		            if(operatorIdx>0)prefix.append("|");
		            prefix.append("<TEM_").append(ctgNmPrefix).append(":contains:");
	                int i=0;
	                for(StringTokenizer st = new StringTokenizer(themeCtgIds,"|");st.hasMoreTokens();){
	                    String c = st.nextToken();
	                    if(i>0)prefix.append("|");
	                    prefix.append(c);
	                    i++;
	                }
	                prefix.append(">");
		        }
	        }else if(!ctgId.equals("")){
		        prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">");
		    }
		    return prefix.toString();
		}
	},
	BANR_TGT_DIV_CD{
		public String getPrefix(Parameter parameter) {
			if(!StringUtils.isBlank(parameter.getAplTgtMediaCd())){
				return "<SHRTC_TGT_TYPE_CD:contains:".concat(parameter.getAplTgtMediaCd()).concat(">");
			}
			return "";
		}
	},
	BANR_DIV_CD{
		public String getPrefix(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String devicedivcd= StringUtils.defaultIfEmpty(userInfo.getDeviceDivCd(),"");
			//모바일 웹/앱만 해당
			if(!devicedivcd.equals("") && !devicedivcd.equals("10") ){ 
				if(Integer.parseInt(devicedivcd) > 20 || StringUtils.isNotEmpty(userInfo.getMobileAppNo())) devicedivcd = "30";
				return "<SHRTC_DIV_CD:contains:".concat("00|"+devicedivcd).concat(">");
			}
			return "";
		}
	},
	SRCH_CTG_ITEM_PREFIX{
		public String getPrefix(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
		    StringBuilder ctgNmPrefix = new StringBuilder();
		    String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
		    String ctgId 	= StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
		    String ctgIds 	= StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
		    String ctgLv 	= StringUtils.defaultIfEmpty(parameter.getCtgLv(), "1");
		    String ctgLast 	= StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
		    
		    String themeYn 		= StringUtils.defaultIfEmpty(parameter.getThemeYn(), "");
		    String themeCtgIds 	= StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");
		    // CTG IDS 부터
		    // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
	        if(themeYn.equals("Y")){
	            ctgNmPrefix.append("TEM_");
	        }else if(strSiteNo.equals("6005")){
	        	ctgNmPrefix.append("SCOM_");
	        }
	        if(ctgLv.equals("1")){
                ctgNmPrefix.append("DISP_CTG_LCLS_ID");
            }else if(ctgLv.equals("2")){
                ctgNmPrefix.append("DISP_CTG_MCLS_ID");
            }else if(ctgLv.equals("3")){
                ctgNmPrefix.append("DISP_CTG_SCLS_ID");
            }else if(ctgLv.equals("4")){
                ctgNmPrefix.append("DISP_CTG_DCLS_ID");
            }else if(ctgLv.equals("5")){
                ctgNmPrefix.append("DISP_CTG_DCLS_ID");
            }
		    if(!ctgIds.equals("")||!themeCtgIds.equals("")){
		        int operatorIdx = 0;
		        if(!ctgIds.equals("")){
		            prefix.append("<").append(ctgNmPrefix).append(":contains:");
		            int i=0;
		            for(StringTokenizer st = new StringTokenizer(ctgIds,"|");st.hasMoreTokens();){
		                String c = st.nextToken();
		                if(i>0)prefix.append("|");
		                prefix.append(c);
		                i++;
		            }
		            prefix.append(">");
		            operatorIdx++;
		        }
		        if(!themeCtgIds.equals("")){
		            if(operatorIdx>0)prefix.append("|");
		            prefix.append("<TEM_").append(ctgNmPrefix).append(":contains:");
	                int i=0;
	                for(StringTokenizer st = new StringTokenizer(themeCtgIds,"|");st.hasMoreTokens();){
	                    String c = st.nextToken();
	                    if(i>0)prefix.append("|");
	                    prefix.append(c);
	                    i++;
	                }
	                prefix.append(">");
		        }
	        }else if(!ctgId.equals("")){
		        prefix.append("<").append(ctgNmPrefix).append(":contains:").append(ctgId).append(">");
		    }
		    return prefix.toString();
		}
	},
	BENEFIT_FILTER{
		public String getPrefix(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			String benefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
			
			if(!benefit.equals("")){
				prefix.append("<FILTER:contains:");
	            int i=0;
	            for(StringTokenizer st = new StringTokenizer(benefit,"|");st.hasMoreTokens();){
	                String c = st.nextToken();
	                if(i>0)prefix.append("|");
	                prefix.append(c.toUpperCase());
	                i++;
	            }
	            prefix.append(">");
	        }
			
			return prefix.toString();
		}
	},
	CLASSIFICATION_FILTER{
		public String getPrefix(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			String clsFilter = StringUtils.defaultIfEmpty(parameter.getClsFilter(), "");
			
			if(!clsFilter.equals("")){
				
	            int i=0;
	            for(StringTokenizer st = new StringTokenizer(clsFilter,"^");st.hasMoreTokens();){
	                String c = st.nextToken();
	                
	                prefix.append("<FILTER:contains:");
	                prefix.append(c.toUpperCase());
	                prefix.append(">");
	            }
	            
	        }
			
			return prefix.toString();
		}
	},
	USE_YN_PREFIX{
		public String getPrefix(Parameter parameter) {
			if(StringUtils.isNotBlank(parameter.getUseYn())){
				return "<USE_YN:contains:"+ parameter.getUseYn() +">";
			}
			return "";
		}
	}
	;
	public abstract String getPrefix(Parameter parameter);
}
