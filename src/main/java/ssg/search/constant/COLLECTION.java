package ssg.search.constant;

import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.parameter.Parameter;
import ssg.search.parameter.ParameterUtil;
import ssg.search.util.CollectionUtils;

import java.util.StringTokenizer;

/**
 * 검색 API 의 MAIN 이 되는 상수 클래스 ENUM,
 * refactor 09.23
 * 인터페이스와 클래스로 나눌지 여부 결정하지 않음.
 * @author 131544
 *
 */
@Deprecated
public enum COLLECTION {
	VIRTUAL{
		public String getDocumentField(Parameter parameter) {
			return "SITE_NO,ITEM_ID,DISP_CTG_LST";
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID";
		}
		public String getPrefixField(Parameter parameter) {
			/* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            prefix
	            .append("<SRCH_PREFIX:contains:VIRTUAL").append(">")
	        ;

            // SALESTR_LST
            prefix
            .append("<SALESTR_LST:contains:").append(userInfo.getEmSaleStrNo()).append(userInfo.getEmRsvtShppPsblYn()).append(">");

            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }
            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }
            // BRAND_ID
            String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
            if(!strBrandId.equals("")){
                prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
            }
            // 전시카테고리 (전시 카운팅용)
            String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
            if(!strDispCtgId.equals("")){
                prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
            return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 40;
        }
        public String getMultiGroupBy(Parameter parameter) {
            return "DISP_CTG_LST";
        }
        public String getFilter(Parameter parameter) {
        	return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "virtual";
        }
        public String getCollectionAliasName() {
            return "virtual";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "item";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return "book";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BRAND_TOTAL{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
            sb
            .append("SITE_NO,ITEM_ID,ITEM_NM,DISP_CTG_LCLS_ID,DISP_CTG_LCLS_NM,DISP_CTG_MCLS_ID,DISP_CTG_MCLS_NM,")
            .append("DISP_CTG_SCLS_ID,DISP_CTG_SCLS_NM,DISP_CTG_DCLS_ID,DISP_CTG_DCLS_NM,")
            .append("SELLPRC,SHPP_TYPE_DTL_CD,SALESTR_LST")
            ;
            return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID";
		}
		public String getPrefixField(Parameter parameter) {
			/* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            prefix
	            .append("<SRCH_PREFIX:contains:")
	            .append(site.getSitePrefixNm(parameter))
	            .append(">")
	        ;

            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
                prefix
                    .append("<SALESTR_LST:contains:")
                    .append(site.getSiteSalestrMallTab(parameter))
                    .append(">")
                ;
            }


            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }

            // BRAND_ID
            String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
            if(!strBrandId.equals("")){
                prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
            }

            // COLOR
            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
            if(!strColor.equals("")){
                if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
            }
            // SIZE
            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
            if(!strSize.equals("")){
                if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
            // SCOM 에서만
         	if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
         		prefix.append("<SCOM_EXPSR_YN:contains:Y>");
         	}

            return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 40;
        }
        public String getMultiGroupBy(Parameter parameter) {
            return "DISP_CTG_LST";
        }
        public String getFilter(Parameter parameter) {
        	return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "brand_total";
        }
        public String getCollectionAliasName() {
            return "brand_total";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "item";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return "book";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BOOK_BRAND_TOTAL{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
			sb
			.append("SITE_NO,ITEM_ID,ITEM_NM,DISP_CTG_LCLS_ID,DISP_CTG_LCLS_NM,DISP_CTG_MCLS_ID,DISP_CTG_MCLS_NM,")
			.append("DISP_CTG_SCLS_ID,DISP_CTG_SCLS_NM,DISP_CTG_DCLS_ID,DISP_CTG_DCLS_NM,")
			.append("SELLPRC,SHPP_TYPE_DTL_CD,SALESTR_LST")
			;
			return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID";
		}
		public String getPrefixField(Parameter parameter) {
			/* 필수적인 파라메터 */
			StringBuilder prefix = new StringBuilder();
			SITE   site   = util.getSite(parameter);
			FrontUserInfo userInfo = parameter.getUserInfo();

			prefix
			.append("<SRCH_PREFIX:contains:")
			.append(site.getSitePrefixNm(parameter))
			.append(">")
			;

			// SALESTR OR MBR_PREFIX
			String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
			if(!mbrPrefix.equals("")){
				prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
			}
			else{
				prefix
				.append("<SALESTR_LST:contains:")
				.append(site.getSiteSalestrMallTab(parameter))
				.append(">")
				;
			}


			// B2C, B2E
			String type = userInfo.getMbrType();
			String coId = userInfo.getMbrCoId();
			if(type!=null && coId!=null){
				if(type.equals("B2C")){
					prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
				}else if(type.equals("B2E")){
					prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
				}
			}

			// BRAND_ID
			String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
			if(!strBrandId.equals("")){
				prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
			}
			// 전시적용범위코드(디바이스코드)
			String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
			if(!strAplTgtMediaCd.equals("")){
				prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
			}
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
         		prefix.append("<SCOM_EXPSR_YN:contains:Y>");
         	}

			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 40;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return "DISP_CTG_LST";
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "book_brand_total";
		}
		public String getCollectionAliasName() {
			return "book_brand_total";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "book";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
            return "book";
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	BRAND_MALL_DISP{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
			sb
			.append("SITE_NO,ITEM_ID,ITEM_NM,DISP_CTG_LCLS_ID,DISP_CTG_LCLS_NM,DISP_CTG_MCLS_ID,DISP_CTG_MCLS_NM,")
			.append("DISP_CTG_SCLS_ID,DISP_CTG_SCLS_NM,DISP_CTG_DCLS_ID,DISP_CTG_DCLS_NM,")
			.append("SELLPRC,SHPP_TYPE_DTL_CD,SALESTR_LST")
			;
			return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID";
		}
		public String getPrefixField(Parameter parameter) {
			/* 필수적인 파라메터 */
			StringBuilder prefix = new StringBuilder();
			SITE   site   = util.getSite(parameter);
			FrontUserInfo userInfo = parameter.getUserInfo();

			prefix
			.append("<SRCH_PREFIX:contains:")
			.append(site.getSitePrefixNm(parameter))
			.append(">")
			;
			// SALESTR OR MBR_PREFIX
			String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
			if(!mbrPrefix.equals("")){
				prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
			}
			else{
				prefix
				.append("<SALESTR_LST:contains:")
				.append(site.getSiteSalestrPrefixNm(parameter))
				.append(">")
				;
			}


			// B2C, B2E
			String type = userInfo.getMbrType();
			String coId = userInfo.getMbrCoId();
			if(type!=null && coId!=null){
				if(type.equals("B2C")){
					prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
				}else if(type.equals("B2E")){
					prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
				}
			}

			// SPL_VEN_ID
			String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
			if(!splVenId.equals("")){
				prefix
				.append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
				;
			}
			// LRNK_SPL_VEN_ID
			String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
			if(!lrnkSplVenId.equals("")){
				prefix
				.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
			}

			// BRAND_ID
			String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
			if(!strBrandId.equals("")){
				prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
			}

			// 전시카테고리 (전시 카운팅용)
			String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
			if(!strDispCtgId.equals("")){
				prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
			}

			// COLOR
			String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
			if(!strColor.equals("")){
				if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
			}
			// SIZE
			String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
			if(!strSize.equals("")){
				if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
			}
			// CLS
			String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
			if(!strCls.equals("")){
				prefix.append("<CLS:contains:");
				for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
					prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
				}
				prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
			}
			// 전시적용범위코드(디바이스코드)
			String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
			if(!strAplTgtMediaCd.equals("")){
				prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
			}
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 40;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return "SITE_NO,SCOM_EXPSR_YN";
		}
		public String getFilter(Parameter parameter) {
			return "SELLPRC";
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "brand_mall_disp";
		}
		public String getCollectionAliasName() {
			return "brand_mall_disp";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "item";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
            return "book";
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	BOOK_BRAND_MALL_DISP{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
			sb
			.append("SITE_NO,ITEM_ID,ITEM_NM,DISP_CTG_LCLS_ID,DISP_CTG_LCLS_NM,DISP_CTG_MCLS_ID,DISP_CTG_MCLS_NM,")
			.append("DISP_CTG_SCLS_ID,DISP_CTG_SCLS_NM,DISP_CTG_DCLS_ID,DISP_CTG_DCLS_NM,")
			.append("SELLPRC,SHPP_TYPE_DTL_CD,SALESTR_LST")
			;
			return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID";
		}
		public String getPrefixField(Parameter parameter) {
			/* 필수적인 파라메터 */
			StringBuilder prefix = new StringBuilder();
			SITE   site   = util.getSite(parameter);
			TARGET target = util.getTarget(parameter);
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			FrontUserInfo userInfo = parameter.getUserInfo();

			prefix
			.append("<SRCH_PREFIX:contains:")
			.append(site.getSitePrefixNm(parameter))
			.append(">")
			;
			// SALESTR OR MBR_PREFIX
			String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
			if(!mbrPrefix.equals("")){
				prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
			}
			else{
				prefix
				.append("<SALESTR_LST:contains:")
				.append(site.getSiteSalestrPrefixNm(parameter))
				.append(">")
				;
			}


			// B2C, B2E
			String type = userInfo.getMbrType();
			String coId = userInfo.getMbrCoId();
			if(type!=null && coId!=null){
				if(type.equals("B2C")){
					prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
				}else if(type.equals("B2E")){
					prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
				}
			}
			// BRAND_ID
			String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
			if(!strBrandId.equals("")){
				prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
			}

			// 전시카테고리 (전시 카운팅용)
			String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
			if(!strDispCtgId.equals("")){
				prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
			}
			// 전시적용범위코드(디바이스코드)
			String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
			if(!strAplTgtMediaCd.equals("")){
				prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
			}

			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 40;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return "SITE_NO,SCOM_EXPSR_YN";
		}
		public String getFilter(Parameter parameter) {
			return "SELLPRC";
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "book_brand_mall_disp";
		}
		public String getCollectionAliasName() {
			return "book_brand_mall_disp";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "book";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
            return "book";
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	BRAND_DISP{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
            sb
            .append("SITE_NO,ITEM_ID,ITEM_NM,DISP_CTG_LCLS_ID,DISP_CTG_LCLS_NM,DISP_CTG_MCLS_ID,DISP_CTG_MCLS_NM,")
            .append("DISP_CTG_SCLS_ID,DISP_CTG_SCLS_NM,DISP_CTG_DCLS_ID,DISP_CTG_DCLS_NM,")
            .append("SELLPRC,SHPP_TYPE_DTL_CD,SALESTR_LST")
            ;
            return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID";
		}
		public String getPrefixField(Parameter parameter) {
			/* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }
            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
                prefix
                    .append("<SALESTR_LST:contains:")
                    .append(site.getSiteSalestrPrefixNm(parameter))
                    .append(">")
                ;
            }


            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }

            // BRAND_ID
            String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
            if(!strBrandId.equals("")){
                prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
            }

            // 전시카테고리 (전시 카운팅용)
            String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
            if(!strDispCtgId.equals("")){
                prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
            }

            // COLOR
            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
            if(!strColor.equals("")){
                if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
            }
            // SIZE
            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
            if(!strSize.equals("")){
                if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
            }
            // CLS
            String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
            if(!strCls.equals("")){
                prefix.append("<CLS:contains:");
                for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
            // SCOM 에서만
         	if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
         		prefix.append("<SCOM_EXPSR_YN:contains:Y>");
         	}
            return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 40;
        }
        public String getMultiGroupBy(Parameter parameter) {
        	return "SITE_NO,SCOM_EXPSR_YN";
        }
        public String getFilter(Parameter parameter) {
        	return "SELLPRC";
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "brand_disp";
        }
        public String getCollectionAliasName() {
            return "brand_disp";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "item";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return "book";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BOOK_BRAND_DISP{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
			sb
			.append("SITE_NO,ITEM_ID,ITEM_NM,DISP_CTG_LCLS_ID,DISP_CTG_LCLS_NM,DISP_CTG_MCLS_ID,DISP_CTG_MCLS_NM,")
			.append("DISP_CTG_SCLS_ID,DISP_CTG_SCLS_NM,DISP_CTG_DCLS_ID,DISP_CTG_DCLS_NM,")
			.append("SELLPRC,SHPP_TYPE_DTL_CD,SALESTR_LST")
			;
			return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID";
		}
		public String getPrefixField(Parameter parameter) {
			/* 필수적인 파라메터 */
			StringBuilder prefix = new StringBuilder();
			SITE   site   = util.getSite(parameter);
			TARGET target = util.getTarget(parameter);
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			FrontUserInfo userInfo = parameter.getUserInfo();

			// SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
			if(strFilterSiteNo.equals("")){
				prefix
				.append("<SRCH_PREFIX:contains:")
				.append(site.getSitePrefixNm(parameter))
				.append(">")
				;
			}
			// filterSiteNo에 조건이 들어올 경우 복합적으로 설정
			else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }
			// SALESTR OR MBR_PREFIX
			String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
			if(!mbrPrefix.equals("")){
				prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
			}
			else{
				prefix
				.append("<SALESTR_LST:contains:")
				.append(site.getSiteSalestrPrefixNm(parameter))
				.append(">")
				;
			}


			// B2C, B2E
			String type = userInfo.getMbrType();
			String coId = userInfo.getMbrCoId();
			if(type!=null && coId!=null){
				if(type.equals("B2C")){
					prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
				}else if(type.equals("B2E")){
					prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
				}
			}
			// BRAND_ID
			String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
			if(!strBrandId.equals("")){
				prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
			}

			// 전시카테고리 (전시 카운팅용)
			String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
			if(!strDispCtgId.equals("")){
				prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
			}
			// 전시적용범위코드(디바이스코드)
			String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
			if(!strAplTgtMediaCd.equals("")){
				prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
			}
			if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
         		prefix.append("<SCOM_EXPSR_YN:contains:Y>");
         	}

			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 40;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return "SITE_NO,SCOM_EXPSR_YN";
		}
		public String getFilter(Parameter parameter) {
			return "SELLPRC";
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "book_brand_disp";
		}
		public String getCollectionAliasName() {
			return "book_brand_disp";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "book";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
            return "book";
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
    MALL_DISP{
        public String getDocumentField(Parameter parameter) {
            return "SITE_NO,ITEM_ID";
        }
        public String getSearchField(Parameter parameter) {
            return "ITEM_ID";
        }
        public String getPrefixField(Parameter parameter) {
            /* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            // @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
            String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
            String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
            if(!strPerdcSalestrNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
                    .append(strPerdcSalestrNo)
                    .append(strPerdcRsvtShppPsblYn)
                    .append(">")
                ;
                return prefix.toString();
            }

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }

            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
                prefix
                    .append("<SALESTR_LST:contains:")
                    .append(site.getSiteSalestrMallTab(parameter))
                    .append(">")
                ;
            }


            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }

            // BRAND_ID
            String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
            if(!strBrandId.equals("")){
                prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
            }

            // 전시카테고리 (전시 카운팅용)
            String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
            if(!strDispCtgId.equals("")){
                prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
            }

            // COLOR
            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
            if(!strColor.equals("")){
                if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
            }
            // SIZE
            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
            if(!strSize.equals("")){
                if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 1;
        }
        public String getMultiGroupBy(Parameter parameter) {
            return "SITE_NO,SCOM_EXPSR_YN,SIZE_LST";
        }
        public String getFilter(Parameter parameter) {
            TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            if(site.equals(SITE.SSG)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
                return "SELLPRC";
            }
            return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "mall_disp";
        }
        public String getCollectionAliasName() {
            return "mall_disp";
        }
        public String getCollectionRealName(Parameter parameter) {
        	return "item";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
    },
    BOOK_MALL_DISP{
    	public String getDocumentField(Parameter parameter) {
    		return "SITE_NO,ITEM_ID";
    	}
    	public String getSearchField(Parameter parameter) {
    		return "ITEM_ID";
    	}
    	public String getPrefixField(Parameter parameter) {
    		/* 필수적인 파라메터 */
    		StringBuilder prefix = new StringBuilder();
    		SITE   site   = util.getSite(parameter);
    		TARGET target = util.getTarget(parameter);
    		String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
    		FrontUserInfo userInfo = parameter.getUserInfo();

    		// @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
    		String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
    		String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
    		if(!strPerdcSalestrNo.equals("")){
    			prefix
    			.append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
    			.append(strPerdcSalestrNo)
    			.append(strPerdcRsvtShppPsblYn)
    			.append(">")
    			;
    			return prefix.toString();
    		}

    		// SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
    		if(strFilterSiteNo.equals("")){
    			prefix
    			.append("<SRCH_PREFIX:contains:")
    			.append(site.getSitePrefixNm(parameter))
    			.append(">")
    			;
    		}

    		// SALESTR OR MBR_PREFIX
    		String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
    		if(!mbrPrefix.equals("")){
    			prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
    		}
    		else{
    			prefix
    			.append("<SALESTR_LST:contains:")
    			.append(site.getSiteSalestrMallTab(parameter))
    			.append(">")
    			;
    		}

    		// B2C, B2E
    		String type = userInfo.getMbrType();
    		String coId = userInfo.getMbrCoId();
    		if(type!=null && coId!=null){
    			if(type.equals("B2C")){
    				prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
    			}else if(type.equals("B2E")){
    				prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
    			}
    		}

    		// BRAND_ID
    		String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
    		if(!strBrandId.equals("")){
    			prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
    		}

    		// 전시카테고리 (전시 카운팅용)
    		String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
    		if(!strDispCtgId.equals("")){
    			prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
    		}

    		// 전시적용범위코드(디바이스코드)
    		String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
    		if(!strAplTgtMediaCd.equals("")){
    			prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
    		}
    		return prefix.toString();
    	}
    	public int getPrefixOperator(Parameter parameter) {
    		return 1;
    	}
    	public int getDefaultItemCount(Parameter parameter) {
    		return 1;
    	}
    	public String getMultiGroupBy(Parameter parameter) {
    		return "SITE_NO,SCOM_EXPSR_YN";
    	}
    	public String getFilter(Parameter parameter) {
    		TARGET target = util.getTarget(parameter);
    		SITE   site   = util.getSite(parameter);
    		if(site.equals(SITE.SSG)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
    			return "SELLPRC";
    		}
    		return null;
    	}
    	public boolean useAlias() {
    		return true;
    	}
    	public String getCollectionAliasName(Parameter parameter) {
    		return "book_mall_disp";
    	}
    	public String getCollectionAliasName() {
    		return "book_mall_disp";
    	}
    	public String getCollectionRealName(Parameter parameter) {
    		return "book";
    	}
    	public String getCategoryBoost(Parameter parameter) {
    		return null;
    	}
    	public String getCollectionRanking(Parameter parameter) {
    		return null;
    	}
    	public String getCollectionSort(Parameter parameter) {
    		return null;
    	}
    	public String getPropGrouping(Parameter parameter) {
    		return null;
    	}
    },
    DISP{
        public String getDocumentField(Parameter parameter) {
            return "SITE_NO,ITEM_ID";
        }
        public String getSearchField(Parameter parameter) {
            return "ITEM_ID";
        }
        public String getPrefixField(Parameter parameter) {
            /* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            // @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
            String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
            String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
            if(!strPerdcSalestrNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
                    .append(strPerdcSalestrNo)
                    .append(strPerdcRsvtShppPsblYn)
                    .append(">")
                ;
                return prefix.toString();
            }

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }

            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
                prefix
                    .append("<SALESTR_LST:contains:")
                    .append(site.getSiteSalestrPrefixNm(parameter))
                    .append(">")
                ;
            }


            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }

            // BRAND_ID
            String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
            if(!strBrandId.equals("")){
                prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
            }

            // 전시카테고리 (전시 카운팅용)
            String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
            if(!strDispCtgId.equals("")){
                prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
            }

            // COLOR
            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
            if(!strColor.equals("")){
                if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
            }
            // SIZE
            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
            if(!strSize.equals("")){
                if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
            }

            // CLS
            String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
            if(!strCls.equals("")){
                prefix.append("<CLS:contains:");
                for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }

            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 0;
        }
        public String getMultiGroupBy(Parameter parameter) {
            return null;
        }
        public String getFilter(Parameter parameter) {
            TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            if(site.equals(SITE.SSG)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
                return "SELLPRC";
            }
            return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "book";
        }
        public String getCollectionAliasName() {
            return "book";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "item";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return "book";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
    },
    BOOK_DISP{
    	public String getDocumentField(Parameter parameter) {
    		return "SITE_NO,ITEM_ID";
    	}
    	public String getSearchField(Parameter parameter) {
    		return "ITEM_ID";
    	}
    	public String getPrefixField(Parameter parameter) {
    		/* 필수적인 파라메터 */
    		StringBuilder prefix = new StringBuilder();
    		SITE   site   = util.getSite(parameter);
    		TARGET target = util.getTarget(parameter);
    		String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
    		FrontUserInfo userInfo = parameter.getUserInfo();

    		// @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
    		String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
    		String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
    		if(!strPerdcSalestrNo.equals("")){
    			prefix
    			.append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
    			.append(strPerdcSalestrNo)
    			.append(strPerdcRsvtShppPsblYn)
    			.append(">")
    			;
    			return prefix.toString();
    		}

    		// SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
    		if(strFilterSiteNo.equals("")){
    			prefix
    			.append("<SRCH_PREFIX:contains:")
    			.append(site.getSitePrefixNm(parameter))
    			.append(">")
    			;
    		}
    		// filterSiteNo에 조건이 들어올 경우 복합적으로 설정
    		else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }

    		// SALESTR OR MBR_PREFIX
    		String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
    		if(!mbrPrefix.equals("")){
    			prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
    		}
    		else{
    			prefix
    			.append("<SALESTR_LST:contains:")
    			.append(site.getSiteSalestrPrefixNm(parameter))
    			.append(">")
    			;
    		}


    		// B2C, B2E
    		String type = userInfo.getMbrType();
    		String coId = userInfo.getMbrCoId();
    		if(type!=null && coId!=null){
    			if(type.equals("B2C")){
    				prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
    			}else if(type.equals("B2E")){
    				prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
    			}
    		}
    		// BRAND_ID
    		String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
    		if(!strBrandId.equals("")){
    			prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
    		}

    		// 전시카테고리 (전시 카운팅용)
    		String strDispCtgId = StringUtils.defaultIfEmpty(parameter.getDispCtgId(), "");
    		if(!strDispCtgId.equals("")){
    			prefix.append("<DISP_CTG_LST:contains:").append(strDispCtgId).append(">");
    		}
    		// 전시적용범위코드(디바이스코드)
    		String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
    		if(!strAplTgtMediaCd.equals("")){
    			prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
    		}

    		return prefix.toString();
    	}
    	public int getPrefixOperator(Parameter parameter) {
    		return 1;
    	}
    	public int getDefaultItemCount(Parameter parameter) {
    		return 0;
    	}
    	public String getMultiGroupBy(Parameter parameter) {
    		return null;
    	}
    	public String getFilter(Parameter parameter) {
    		TARGET target = util.getTarget(parameter);
    		SITE   site   = util.getSite(parameter);
    		if(site.equals(SITE.SSG)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
    			return "SELLPRC";
    		}
    		return null;
    	}
    	public boolean useAlias() {
    		return true;
    	}
    	public String getCollectionAliasName(Parameter parameter) {
    		return "book_disp";
    	}
    	public String getCollectionAliasName() {
    		return "book_disp";
    	}
    	public String getCollectionRealName(Parameter parameter) {
    		return "book";
    	}
    	public String getCategoryBoost(Parameter parameter) {
    		return null;
    	}
    	public String getCollectionRanking(Parameter parameter) {
    		return null;
    	}
    	public String getCollectionSort(Parameter parameter) {
            return "book";
    	}
    	public String getPropGrouping(Parameter parameter) {
    		return null;
    	}
    },
    ITEM{
        public String getDocumentField(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            sb
            .append("SITE_NO,ITEM_ID,ITEM_NM,")
            .append("SELLPRC,ITEM_REG_DIV_CD,SHPP_TYPE_CD,SHPP_TYPE_DTL_CD,SALESTR_LST,")
            .append("EXUS_ITEM_DIV_CD,EXUS_ITEM_DTL_CD,SHPP_MAIN_CD,SHPP_MTHD_CD")
            ;
            return sb.toString();
        }
        public String getSearchField(Parameter parameter) {
            StringBuilder sb = new StringBuilder();
            sb.append("ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,BRAND_NM,TAG_NM,GNRL_STD_DISP_CTG");
            return sb.toString();
        }
        public String getPrefixField(Parameter parameter) {
            /* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            // @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
            String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
            String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
            if(!strPerdcSalestrNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
                    .append(strPerdcSalestrNo)
                    .append(strPerdcRsvtShppPsblYn)
                    .append(">")
                ;
                return prefix.toString();
            }

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }
            // SALESTR OR MBR_PREFIX
            String saleStrLst = "<SALESTR_LST:contains:" + site.getSiteSalestrPrefixNm(parameter) + ">" ;
            prefix
                .append(saleStrLst)
            ;

            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }


            // BRAND_ID
            String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
            if(!strBrandId.equals("")){
                prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
            }

            // CTG_ID ( DISP_CTG_*CLS_ID 를 사용 )
            String strCtgId     = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");                 // ctgId (한개)
            String strCtgIds    = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");                // ctgIds(여러개)
            String strThemeCtgIds    = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");      // theme ctgIds(여러개)
            String strCtglv     = StringUtils.defaultIfEmpty(parameter.getCtgLv(),"0");                 // ctgLv (현재레벨)
            String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
            String filterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(),"");
            String themeYn      = StringUtils.defaultIfEmpty(parameter.getThemeYn(),"N");
            prefix.append(getCtgPrefix(site,strCtgId,strCtgIds,strThemeCtgIds,strCtglv,strCtgLast,filterSiteNo,"item",themeYn));

            // COLOR
            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
            if(!strColor.equals("")){
                if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
            }
            // SIZE
            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
            if(!strSize.equals("")){
                if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
            }
            // 다면분류 
            String clsFilter = StringUtils.defaultIfEmpty(parameter.getClsFilter(), "");
			
			if(!clsFilter.equals("")){
	            for(StringTokenizer st = new StringTokenizer(clsFilter,"^");st.hasMoreTokens();){
	                String c = st.nextToken();
	                
	                prefix.append("<FILTER:contains:");
	                prefix.append(c.toUpperCase());
	                prefix.append(">");
	            }
	            
	        }

            // 배송 이라고 하나 상품과 연관있음 대신 SITE 관련 필드와 AND 조건
            // SSG -> 점포예약배송, 무료배송, 해외배송
            // EMART -> 무료배송, 사은품
			String shppPrefix = CollectionUtils.getShppPrefix(parameter);
            if(shppPrefix!=null && !shppPrefix.equals("")){
				prefix.append(shppPrefix);
			}

            // CLS
            String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
            if(!strCls.equals("")){
                prefix.append("<CLS:contains:");
                for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }

            // 검색은 ITEM_SRCH_YN 사용
            prefix.append("<SRCH_PSBL_YN:contains:Y>");

            // Filter < S.COM은 혜택 영역으로 한가지로 통합되어 있으므로 서로 or 조건이 되어 버린다 >
            String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
            String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
            if(!strFilter.equals("")){
                prefix.append("<FILTER:contains:").append(strFilter).append(">");
            }
            if(!strBenefit.equals("")){
                prefix.append("<FILTER:contains:").append(strBenefit).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
            if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
         		prefix.append("<SCOM_EXPSR_YN:contains:Y>");
         	}
            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            switch(util.getSite(parameter)){
                case EMART:
                    return 40;
                case TRADERS:
                    return 40;
                case BOONS:
                    return 40;
                default:
                    return 40;
            }
        }
        public String getMultiGroupBy(Parameter parameter) {
            SITE site = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            if((site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SSG) || site.equals(SITE.EMART)) && (target.equals(TARGET.MOBILE)||target.equals(TARGET.MOBILE_BOOK)) ){
            	if(target.equals(TARGET.MOBILE)){
            		String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
                    if(strCtgLast.equals("Y")){
                    	return "SIZE_LST";
                    }
            	}
                return null;
            }else{
                String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
                if(strCtgLast.equals("Y")){
                	if(site.equals(SITE.SSG)||site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT) || site.equals(SITE.EMART)){
                		return "BRAND_ID,SIZE_LST";
                	}
                    return "BRAND_ID";
                }
            }
            return null;
        }
        public String getFilter(Parameter parameter) {
            TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            if(target.equals(TARGET.ALL)||target.equals(TARGET.CATEGORY)||target.equals(TARGET.ITEM)||target.equals(TARGET.MOBILE)||target.equals(TARGET.DISP) || target.equals(TARGET.SHOP) || target.equals(TARGET.MOBILE_ITEM)){
                if(site.equals(SITE.SSG)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
                    return "SELLPRC";
                }
            }
            return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
            TARGET target = util.getTarget(parameter);
            if(target.equals(TARGET.ALL)){
                return "item"+strSiteNo;
            }else if(target.equals(TARGET.MOBILE)){
                return "mobile"+strSiteNo;
            }else if(target.equals(TARGET.PARTNER)){
            	return "partner";
            }
            return "item";
        }
        public String getCollectionAliasName() {
            return "item";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "item";
        }
        public String getCategoryBoost(Parameter parameter) {
            return "item";
        }
        public String getCollectionRanking(Parameter parameter) {
            String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
            String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
            if(!strPerdcSalestrNo.equals("") && !strPerdcRsvtShppPsblYn.equals("")){
                return "6001";
            }
            SITE site = util.getSite(parameter);
            if(site.equals(SITE.SSG)){
                return "6005";
            }else if(site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)){
                return "6004";
            }else if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
                return "6001";
            }
            return "6005";
        }
        public String getCollectionSort(Parameter parameter) {
            return "item";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
    },
	ITEM_TEST{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
			sb
					.append("SITE_NO,ITEM_ID,ITEM_NM,")
					.append("SELLPRC,ITEM_REG_DIV_CD,SHPP_TYPE_CD,SHPP_TYPE_DTL_CD,SALESTR_LST,")
					.append("EXUS_ITEM_DIV_CD,EXUS_ITEM_DTL_CD,SHPP_MAIN_CD,SHPP_MTHD_CD")
			;
			return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
			sb.append("ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,BRAND_NM,TAG_NM,GNRL_STD_DISP_CTG");
			return sb.toString();
		}
		public String getPrefixField(Parameter parameter) {
            /* 필수적인 파라메터 */
			StringBuilder prefix = new StringBuilder();
			SITE   site   = util.getSite(parameter);
			TARGET target = util.getTarget(parameter);
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			FrontUserInfo userInfo = parameter.getUserInfo();

			// @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
			String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
			String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
			if(!strPerdcSalestrNo.equals("")){
				prefix
						.append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
						.append(strPerdcSalestrNo)
						.append(strPerdcRsvtShppPsblYn)
						.append(">")
				;
				return prefix.toString();
			}

			// SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
			if(strFilterSiteNo.equals("")){
				prefix
						.append("<SRCH_PREFIX:contains:")
						.append(site.getSitePrefixNm(parameter))
						.append(">")
				;
			}
			// filterSiteNo에 조건이 들어올 경우 복합적으로 설정
			else{
				SITE fSite = util.getFilterSite(parameter);
				if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
					prefix
							.append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
				}else{
					prefix
							.append("<SRCH_PREFIX:contains:")
							.append(site.getSitePrefixNm(parameter))
							.append(" ")
							.append(fSite.getSitePrefixSpecificNm(parameter))
							.append(">")
					;
				}
			}
			// SALESTR OR MBR_PREFIX
			String saleStrLst = "<SALESTR_LST:contains:" + site.getSiteSalestrPrefixNm(parameter) + ">" ;
			prefix
					.append(saleStrLst)
			;

			// B2C, B2E
			String type = userInfo.getMbrType();
			String coId = userInfo.getMbrCoId();
			if(type!=null && coId!=null){
				if(type.equals("B2C")){
					prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
				}else if(type.equals("B2E")){
					prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
				}
			}

			// SPL_VEN_ID
			String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
			if(!splVenId.equals("")){
				prefix
						.append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
				;
			}
			// LRNK_SPL_VEN_ID
			String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
			if(!lrnkSplVenId.equals("")){
				prefix
						.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
			}


			// BRAND_ID
			String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
			if(!strBrandId.equals("")){
				prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
			}

			// CTG_ID ( DISP_CTG_*CLS_ID 를 사용 )
			String strCtgId     = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");                 // ctgId (한개)
			String strCtgIds    = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");                // ctgIds(여러개)
			String strThemeCtgIds    = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");      // theme ctgIds(여러개)
			String strCtglv     = StringUtils.defaultIfEmpty(parameter.getCtgLv(),"0");                 // ctgLv (현재레벨)
			String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
			String filterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(),"");
			String themeYn      = StringUtils.defaultIfEmpty(parameter.getThemeYn(),"N");
			prefix.append(getCtgPrefix(site,strCtgId,strCtgIds,strThemeCtgIds,strCtglv,strCtgLast,filterSiteNo,"item",themeYn));

			// COLOR
			String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
			if(!strColor.equals("")){
				if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
			}
			// SIZE
			String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
			if(!strSize.equals("")){
				if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
			}
			// 다면분류
			String clsFilter = StringUtils.defaultIfEmpty(parameter.getClsFilter(), "");

			if(!clsFilter.equals("")){
				for(StringTokenizer st = new StringTokenizer(clsFilter,"^");st.hasMoreTokens();){
					String c = st.nextToken();

					prefix.append("<FILTER:contains:");
					prefix.append(c.toUpperCase());
					prefix.append(">");
				}

			}

			// 배송 이라고 하나 상품과 연관있음 대신 SITE 관련 필드와 AND 조건
			// SSG -> 점포예약배송, 무료배송, 해외배송
			// EMART -> 무료배송, 사은품
			String shppPrefix = CollectionUtils.getShppPrefix(parameter);
			if(shppPrefix!=null && !shppPrefix.equals("")){
				prefix.append(shppPrefix);
			}

			// CLS
			String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
			if(!strCls.equals("")){
				prefix.append("<CLS:contains:");
				for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
					prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
				}
				prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
			}

			// 검색은 ITEM_SRCH_YN 사용
			prefix.append("<SRCH_PSBL_YN:contains:Y>");

			// Filter < S.COM은 혜택 영역으로 한가지로 통합되어 있으므로 서로 or 조건이 되어 버린다 >
			String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
			String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
			if(!strFilter.equals("")){
				prefix.append("<FILTER:contains:").append(strFilter).append(">");
			}
			if(!strBenefit.equals("")){
				prefix.append("<FILTER:contains:").append(strBenefit).append(">");
			}
			// 전시적용범위코드(디바이스코드)
			String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
			if(!strAplTgtMediaCd.equals("")){
				prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
			}
			if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
				prefix.append("<SCOM_EXPSR_YN:contains:Y>");
			}
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
				case EMART:
					return 40;
				case TRADERS:
					return 40;
				case BOONS:
					return 40;
				default:
					return 40;
			}
		}
		public String getMultiGroupBy(Parameter parameter) {
			SITE site = util.getSite(parameter);
			TARGET target = util.getTarget(parameter);
			if((site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SSG) || site.equals(SITE.EMART)) && (target.equals(TARGET.MOBILE)||target.equals(TARGET.MOBILE_BOOK)) ){
				if(target.equals(TARGET.MOBILE)){
					String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
					if(strCtgLast.equals("Y")){
						return "SIZE_LST";
					}
				}
				return null;
			}else{
				String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
				if(strCtgLast.equals("Y")){
					if(site.equals(SITE.SSG)||site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT) || site.equals(SITE.EMART)){
						return "BRAND_ID,SIZE_LST";
					}
					return "BRAND_ID";
				}
			}
			return null;
		}
		public String getFilter(Parameter parameter) {
			TARGET target = util.getTarget(parameter);
			SITE   site   = util.getSite(parameter);
			if(target.equals(TARGET.ALL)||target.equals(TARGET.CATEGORY)||target.equals(TARGET.ITEM)||target.equals(TARGET.MOBILE)||target.equals(TARGET.DISP) || target.equals(TARGET.SHOP) || target.equals(TARGET.MOBILE_ITEM)){
				if(site.equals(SITE.SSG)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
					return "SELLPRC";
				}
			}
			return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			TARGET target = util.getTarget(parameter);
			if(target.equals(TARGET.ALL)){
				return "item"+strSiteNo;
			}else if(target.equals(TARGET.MOBILE)){
				return "mobile"+strSiteNo;
			}else if(target.equals(TARGET.PARTNER)){
				return "partner";
			}
			return "item";
		}
		public String getCollectionAliasName() {
			return "item";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "item_test";
		}
		public String getCategoryBoost(Parameter parameter) {
			return "item_test";
		}
		public String getCollectionRanking(Parameter parameter) {
			String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
			String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
			if(!strPerdcSalestrNo.equals("") && !strPerdcRsvtShppPsblYn.equals("")){
				return "6001";
			}
			SITE site = util.getSite(parameter);
			if(site.equals(SITE.SSG)){
				return "6005";
			}else if(site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)){
				return "6004";
			}else if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
				return "6001";
			}
			return "6005";
		}
		public String getCollectionSort(Parameter parameter) {
			return "item";
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	CATEGORY{
		public String getDocumentField(Parameter parameter) {
			return "SITE_NO,ITEM_ID";
		}
		public String getSearchField(Parameter parameter) {
		    StringBuilder sb = new StringBuilder();
            sb.append("ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,BRAND_NM,TAG_NM,GNRL_STD_DISP_CTG");
            return sb.toString();
		}
		public String getPrefixField(Parameter parameter) {
		    /* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            // @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
            String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
            String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
            if(!strPerdcSalestrNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
                    .append(strPerdcSalestrNo)
                    .append(strPerdcRsvtShppPsblYn)
                    .append(">")
                ;
                return prefix.toString();
            }

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }

            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
            	prefix
	                .append("<SALESTR_LST:contains:")
	                .append(site.getSiteSalestrPrefixNm(parameter))
	                .append(">")
	            ;
            }


            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }
            // 모바일에서는 현재 페이지 이동으로 변경했기 때문에 로직이 바뀌었음
            if((site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SSG)) && (target.equals(TARGET.MOBILE)||target.equals(TARGET.MOBILE_BOOK)) ){
                // nothing
            }else{
                // BRAND_ID
                String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
                if(!strBrandId.equals("")){
                    prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
                }

                // CTG_ID
                String strCtgId     = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");                 // ctgId (한개)
                String strCtgIds    = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");                // ctgIds(여러개)
                String strThemeCtgIds    = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");      // theme ctgIds(여러개)
                String strCtglv     = StringUtils.defaultIfEmpty(parameter.getCtgLv(),"0");                 // ctgLv (현재레벨)
                String strParentCtgId     = StringUtils.defaultIfEmpty(parameter.getParentCtgId(),"");                 // ctgLv (현재레벨)
                String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
                String filterSiteNo =  StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(),"");
                String themeYn      = StringUtils.defaultIfEmpty(parameter.getThemeYn(),"N");
                if(strCtgLast.equals("Y")){
                    prefix.append(getCtgPrefix(site,strParentCtgId,strCtgIds,strThemeCtgIds,strCtglv,strCtgLast,filterSiteNo,"category",themeYn));
                }else{
                    prefix.append(getCtgPrefix(site,strCtgId,strCtgIds,strThemeCtgIds,strCtglv,strCtgLast,filterSiteNo,"category",themeYn));
                }
            }

            // COLOR
            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
            if(!strColor.equals("")){
                if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
            }
            // SIZE
            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
            if(!strSize.equals("")){
                if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
            }

            // 배송 이라고 하나 상품과 연관있음 대신 SITE 관련 필드와 AND 조건
            // SSG -> 점포예약배송, 무료배송, 해외배송
            // EMART -> 무료배송, 사은품
            String strShpp = StringUtils.defaultIfEmpty(parameter.getShpp(), "");
            if(!strShpp.equals("")){
                prefix.append("<SHPP:contains:");
                for(StringTokenizer st = new StringTokenizer(strShpp,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }

            // CLS
            String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
            if(!strCls.equals("")){
                prefix.append("<CLS:contains:");
                for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }

            // 검색은 ITEM_SRCH_YN 사용
            prefix.append("<SRCH_PSBL_YN:contains:Y>");

            // Filter < S.COM은 혜택 영역으로 한가지로 통합되어 있으므로 서로 or 조건이 되어 버린다 >
            String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
            String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
            if(!strFilter.equals("")){
                prefix.append("<FILTER:contains:").append(strFilter).append(">");
            }
            if(!strBenefit.equals("")){
                prefix.append("<FILTER:contains:").append(strBenefit).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
            if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
         		prefix.append("<SCOM_EXPSR_YN:contains:Y>");
         	}
            return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
		    return 1;
		}
		public String getMultiGroupBy(Parameter parameter) {
            SITE site = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            if((site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SSG)|| site.equals(SITE.EMART)) && (target.equals(TARGET.MOBILE)||target.equals(TARGET.MOBILE_BOOK)) ){
            	if(target.equals(TARGET.MOBILE)){
            		String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
                    if(strCtgLast.equals("N")){
                    	return "SIZE_LST";
                    }
            	}
            }else{
                String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
                if(strCtgLast.equals("N")){
                	if(site.equals(SITE.SSG)||site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT) || site.equals(SITE.EMART)){
                		return "BRAND_ID,SIZE_LST";
                	}
                    return "BRAND_ID";
                }
            }
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "category";
		}
		public String getCollectionAliasName() {
		    return "category";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "item";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BRAND{
	    public String getDocumentField(Parameter parameter) {
            return "SITE_NO,ITEM_ID";
        }
        public String getSearchField(Parameter parameter) {
         	return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,BRAND_NM,TAG_NM,GNRL_STD_DISP_CTG";
        }
        public String getPrefixField(Parameter parameter) {
            /* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            // @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
            String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
            String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
            if(!strPerdcSalestrNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
                    .append(strPerdcSalestrNo)
                    .append(strPerdcRsvtShppPsblYn)
                    .append(">")
                ;
                return prefix.toString();
            }

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }

            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
                prefix
                    .append("<SALESTR_LST:contains:")
                    .append(site.getSiteSalestrPrefixNm(parameter))
                    .append(">")
                ;
            }


            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }

            if((site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SSG)) && (target.equals(TARGET.MOBILE)||target.equals(TARGET.MOBILE_BOOK)) ){
                // CTG_ID
                String strCtgId     = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");                 // ctgId (한개)
                String strCtgIds    = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");                // ctgIds(여러개)
                String strThemeCtgIds    = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");      // theme ctgIds(여러개)
                String strCtglv     = StringUtils.defaultIfEmpty(parameter.getCtgLv(),"0");                 // ctgLv (현재레벨)
                String strParentCtgId     = StringUtils.defaultIfEmpty(parameter.getParentCtgId(),"");                 // ctgLv (현재레벨)
                String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
                String filterSiteNo =  StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(),"");
                String themeYn      = StringUtils.defaultIfEmpty(parameter.getThemeYn(),"N");
                if(strCtgLast.equals("Y")){
                    prefix.append(getCtgPrefix(site,strParentCtgId,strCtgIds,strThemeCtgIds,strCtglv,strCtgLast,filterSiteNo,"category",themeYn));
                }else{
                    prefix.append(getCtgPrefix(site,strCtgId,strCtgIds,strThemeCtgIds,strCtglv,strCtgLast,filterSiteNo,"category",themeYn));
                }
            }

            // COLOR
            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
            if(!strColor.equals("")){
                if(!strColor.equals(""))prefix.append("<COLOR_LST:contains:").append(strColor).append(">");
            }
            // SIZE
            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
            if(!strSize.equals("")){
                if(!strSize.equals(""))prefix.append("<SIZE_LST:contains:").append(strSize).append(">");
            }

            // 배송 이라고 하나 상품과 연관있음 대신 SITE 관련 필드와 AND 조건
            // SSG -> 점포예약배송, 무료배송, 해외배송
            // EMART -> 무료배송, 사은품
            String strShpp = StringUtils.defaultIfEmpty(parameter.getShpp(), "");
            if(!strShpp.equals("")){
                prefix.append("<SHPP:contains:");
                for(StringTokenizer st = new StringTokenizer(strShpp,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }

            // CLS
            String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
            if(!strCls.equals("")){
                prefix.append("<CLS:contains:");
                for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }

            // 검색은 ITEM_SRCH_YN 사용
            prefix.append("<SRCH_PSBL_YN:contains:Y>");

            // Filter < S.COM은 혜택 영역으로 한가지로 통합되어 있으므로 서로 or 조건이 되어 버린다 >
            String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
            String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
            if(!strFilter.equals("")){
                prefix.append("<FILTER:contains:").append(strFilter).append(">");
            }
            if(!strBenefit.equals("")){
                prefix.append("<FILTER:contains:").append(strBenefit).append(">");
            }
            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 1;
        }
        public String getMultiGroupBy(Parameter parameter) {
            SITE site = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            if((site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SSG)) && (target.equals(TARGET.MOBILE)||target.equals(TARGET.MOBILE_BOOK)) ){
                return "BRAND_ID";
            }
            return null;
        }
        public String getFilter(Parameter parameter) {
        	SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            if(site.equals(SITE.SSG) && target.equals(TARGET.MOBILE)){
            	return "SELLPRC";
            }
            return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "brand";
        }
        public String getCollectionAliasName() {
            return "brand";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "item";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	MALL_BOOK{
	    public String getDocumentField(Parameter parameter) {
            return "ITEM_ID";
        }
        public String getSearchField(Parameter parameter) {
            return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,ISBN,BOOK_ENG_NM,ORTITL_NM,SUBTITL_NM,AUTHOR_NM,TRLTPE_NM,PUBSCO_NM";
        }
        public String getPrefixField(Parameter parameter) {
            StringBuilder prefix = new StringBuilder();
            SITE site = util.getSite(parameter);
            FrontUserInfo userInfo = parameter.getUserInfo();

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(site.equals(SITE.SSG)){
                prefix.append("<SRCH_PREFIX:contains:SCOM>");
            }
            // filterSiteNo 사용하지 않음

			// SSG 의 경우 백화점 도서 상품이 존재함
			if(site.equals(SITE.SSG)){
				prefix
						.append("<SALESTR_LST:contains:")
						.append("6005|")
						.append("0001|")
						.append(userInfo.getEmSaleStrNo())
						.append(userInfo.getEmRsvtShppPsblYn())
						.append(">")
				;
			}else if(!site.equals(SITE.SHINSEGAE)){
				prefix
						.append("<SALESTR_LST:contains:")
						.append("6005|")
						.append(userInfo.getEmSaleStrNo())
						.append(userInfo.getEmRsvtShppPsblYn())
						.append(">")
				;
			}

            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }
            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 1;
        }
        public String getMultiGroupBy(Parameter parameter) {
            TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            if(site.equals(SITE.SSG) && target.equals(TARGET.BOOK)){
                return "SITE_NO,SCOM_EXPSR_YN";
            }else return null;
        }
        public String getFilter(Parameter parameter) {
            return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "mall_book";
        }
        public String getCollectionAliasName() {
            return "mall_book";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "book";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	MALL{
		public String getDocumentField(Parameter parameter) {
			StringBuilder sb = new StringBuilder();
            sb
            .append("SITE_NO,ITEM_ID,ITEM_NM,")
            .append("SELLPRC,ITEM_REG_DIV_CD,SHPP_TYPE_CD,SHPP_TYPE_DTL_CD,SALESTR_LST,")
            .append("EXUS_ITEM_DIV_CD,EXUS_ITEM_DTL_CD,SHPP_MAIN_CD,SHPP_MTHD_CD")
            ;
            SITE site = util.getSite(parameter);
            if(site.equals(SITE.SSG)){
            	return "SITE_NO,ITEM_ID";
            }
            return sb.toString();
		}
		public String getSearchField(Parameter parameter) {
		    return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,BRAND_NM,TAG_NM,GNRL_STD_DISP_CTG";
		}
		public String getPrefixField(Parameter parameter) {
		    /* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

			// SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(site.equals(SITE.SSG)){
                prefix.append("<SRCH_PREFIX:contains:SCOM>");
            }
            // filterSiteNo 사용하지 않음

            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
                prefix
                    .append("<SALESTR_LST:contains:")
                    .append(SITE.SSG.getSiteSalestrPrefixNm(parameter))
                    .append(">")
                ;
            }
            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }
            // 검색은 ITEM_SRCH_YN 사용
            prefix.append("<SRCH_PSBL_YN:contains:Y>");

            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			SITE site = util.getSite(parameter);
            if(site.equals(SITE.SSG)){
            	return 1;
            }else{
            	return 46;
            }
		}
		public String getMultiGroupBy(Parameter parameter) {
			return "SITE_NO,SCOM_EXPSR_YN";
		}
		public String getFilter(Parameter parameter) {
		    TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            if(target.equals(TARGET.DISP)){
                if(site.equals(SITE.SSG)){
                    return "SELLPRC";
                }
            }
            return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "mall";
		}
		public String getCollectionAliasName() {
		    return "mall";
		}
		public String getCollectionRealName(Parameter parameter) {
		    return "item";
		}
		public String getCategoryBoost(Parameter parameter) {
			if(!parameter.getSiteNo().equals("6005")){
        		return "item";
        	}
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
        	if(!parameter.getSiteNo().equals("6005")){
        		return "6005";
        	}
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	RECOM{
		public String getDocumentField(Parameter parameter) {
			return "SITE_NO,ITEM_ID,ITEM_NM,DISP_ORDR";
		}
		public String getSearchField(Parameter parameter) {
			return "CRITN_SRCHWD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			SITE site = util.getSite(parameter);

			// 이마트, 트레이더스, 분스 일 경우에는 filterSiteNo에 따라서 변함
			if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
			    if(strFilterSiteNo.equals("")){
	                prefix.append(site.getSitePrefix());
	            }
	            else {
	                SITE fSite = util.getFilterSite(parameter);
	                prefix.append(fSite.getSitePrefix());
	            }
			}else{
			    prefix.append(site.getSitePrefix());
			}

			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
			case EMART   : return 5;
			case TRADERS : return 5;
			case BOONS   : return 5;
			case SHINSEGAE   : return 10;
			default:
				return 4;
			}
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "recom";
		}
		public String getCollectionAliasName() {
		    return "recom";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "recom";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	SPSHOP{
		public String getDocumentField(Parameter parameter) {
			return "SITE_NO,ITEM_ID,ITEM_NM,SELLPRC";
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,BRAND_NM,GNRL_STD_DISP_CTG";
		}
		public String getPrefixField(Parameter parameter) {
			return null;
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
			default:
				return 20;
			}
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "spshop";
		}
		public String getCollectionAliasName() {
		    return "spshop";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "spshop";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BOOK{
		public String getDocumentField(Parameter parameter) {
		    //SELLPRC,SHPP_TYPE_DTL_CD
			return "SITE_NO,ITEM_ID,DISP_CTG_LCLS_ID,DISP_CTG_LCLS_NM,DISP_CTG_MCLS_ID,DISP_CTG_MCLS_NM,DISP_CTG_SCLS_ID,DISP_CTG_SCLS_NM,DISP_CTG_DCLS_ID,DISP_CTG_DCLS_NM,ITEM_NM,AUTHOR_NM,TRLTPE_NM,PUBSCO_NM,SELLPRC,SHPP_TYPE_DTL_CD,FXPRC,ITEM_REG_DIV_CD,SALESTR_LST";
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,ISBN,BOOK_ENG_NM,ORTITL_NM,SUBTITL_NM,AUTHOR_NM,TRLTPE_NM,PUBSCO_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			TARGET target = util.getTarget(parameter);
			FrontUserInfo userInfo = parameter.getUserInfo();

			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");

			// SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정 ( TRADERS, BOONS 통합검색의 경우에는 EMART와 동일하도록 셋 )
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }

			// SSG 의 경우 백화점 도서 상품이 존재함
			if(site.equals(SITE.SSG)){
				prefix
						.append("<SALESTR_LST:contains:")
						.append("6005|")
						.append("0001|")
						.append(userInfo.getEmSaleStrNo())
						.append(userInfo.getEmRsvtShppPsblYn())
						.append(">")
				;
			}else if(!site.equals(SITE.SHINSEGAE)){
				prefix
						.append("<SALESTR_LST:contains:")
						.append("6005|")
						.append(userInfo.getEmSaleStrNo())
						.append(userInfo.getEmRsvtShppPsblYn())
						.append(">")
				;
			}

            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

			// CTG_ID
			String strCtgId = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
			String strCtgIds = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
			String lvl = StringUtils.defaultIfEmpty(parameter.getCtgLv(),"0");
			if(target.equals(TARGET.MOBILE) && site.equals(SITE.SHINSEGAE)){
				// nothing
			}else{
				prefix.append(getBookCtgPrefix(site,lvl,strCtgId,strCtgIds));
			}
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
		    TARGET target = util.getTarget(parameter);
			switch(util.getSite(parameter)){
			case EMART   : if(target.equals(TARGET.BOOK)||target.equals(TARGET.MOBILE_BOOK)){return 40;}return 5;
			case TRADERS : if(target.equals(TARGET.BOOK)){return 40;}return 5;
			case BOONS   : if(target.equals(TARGET.BOOK)){return 40;}return 5;
			case SSG     : if(target.equals(TARGET.BOOK)){return 20;}return 2;
			default:
			    if(target.equals(TARGET.BOOK)){
			        return 40;
			    }
				return 4;
			}
		}
		public String getMultiGroupBy(Parameter parameter) {
		    TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            if(site.equals(SITE.SSG) && target.equals(TARGET.BOOK)){
                return "SITE_NO,SCOM_EXPSR_YN";
            }else return null;
		}
		public String getFilter(Parameter parameter) {
		    TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            if(target.equals(TARGET.ALL)||target.equals(TARGET.CATEGORY)||target.equals(TARGET.BOOK)||target.equals(TARGET.ITEM)||target.equals(TARGET.MOBILE)||target.equals(TARGET.MOBILE_BOOK)){
                if(site.equals(SITE.SSG)||site.equals(SITE.DEPARTMENT)||site.equals(SITE.SHINSEGAE)){
                    return "SELLPRC";
                }
            }
            return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "book";
		}
		public String getCollectionAliasName() {
		    return "book";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "book";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return "book";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BOOK_CATEGORY{
		public String getDocumentField(Parameter parameter) {
			return "SITE_NO,ITEM_ID";
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,ISBN,BOOK_ENG_NM,ORTITL_NM,SUBTITL_NM,AUTHOR_NM,TRLTPE_NM,PUBSCO_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			String strCtgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
			String strCtgId = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
			String strCtgIds = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
			String strParentCtgId = StringUtils.defaultIfEmpty(parameter.getParentCtgId(), "");
			String lvl = StringUtils.defaultIfEmpty(parameter.getCtgLv(),"0");
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			FrontUserInfo userInfo = parameter.getUserInfo();
			// SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정 ( TRADERS, BOONS 통합검색의 경우에는 EMART와 동일하도록 셋 )
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }

            // SSG 의 경우 백화점 도서 상품이 존재함
			if(site.equals(SITE.SSG)){
				prefix
						.append("<SALESTR_LST:contains:")
						.append("6005|")
						.append("0001|")
						.append(userInfo.getEmSaleStrNo())
						.append(userInfo.getEmRsvtShppPsblYn())
						.append(">")
				;
			}else if(!site.equals(SITE.SHINSEGAE)){
				prefix
						.append("<SALESTR_LST:contains:")
						.append("6005|")
						.append(userInfo.getEmSaleStrNo())
						.append(userInfo.getEmRsvtShppPsblYn())
						.append(">")
				;
			}

            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

			// CTG_ID
			if(strCtgLast.equals("N")){
				prefix.append(getBookCtgPrefix(site,lvl,strCtgId,strCtgIds));
			}else{
				prefix.append(getParentBookCtgPrefix(site,lvl,strParentCtgId,""));
			}
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
		    return 1;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "book_category";
		}
		public String getCollectionAliasName() {
		    return "book_category";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "book";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	PNSHOP_SD{
	    public String getDocumentField(Parameter parameter) {
            return "SITE_NO,ORI_SITE_NO,DISP_CMPT_ID,DISP_CMPT_NM,IMG_FILE_NM1,IMG_FILE_NM2,IMG_FILE_NM3,IMG_FILE_NM4,MOD_DTS,DISP_CMPT_TYPE_DTL_LST,MOBILE_DISPLAY_YN,MAI_TITLE_NM_1,MAI_TITLE_NM_2,SUBTITL_NM_1,SUBTITL_NM_2,OSMU_YN,PNSHOP_TYPE_CD";
        }
        public String getSearchField(Parameter parameter) {
            return "DISP_CMPT_NM,SRCHWD_NM,BRAND_LST";
        }
        public String getPrefixField(Parameter parameter) {
            StringBuilder prefix = new StringBuilder();
            TARGET target = util.getTarget(parameter);
            SITE   site   = util.getSite(parameter);
            FrontUserInfo userInfo = parameter.getUserInfo();
            String strCtgId = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
            // SITE_NO
            prefix.append("<ORI_SITE_NO:contains:6009>");
            // SHINSEGAE MALL
            if(site.getSiteNo().equals("6004")){
                prefix.append("<SITE_NO:contains:6009>");
            }
            // S.COM
            else if(site.getSiteNo().equals("6005")){
                prefix.append("<SITE_NO:contains:6005>");
            }
            // EMART, TRADERS, BOONS
            else if(site.getSiteNo().equals("6001")||site.getSiteNo().equals("6002")){  //@ BOOT개발시확인 
                String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
                if(!strFilterSiteNo.equals("")){
                    prefix.append("<SITE_NO:contains:").append(strFilterSiteNo).append(">");
                }else
                    prefix.append(site.getSitePrefixItem());
            }
            else{
                prefix.append(site.getSitePrefix());
            }
            // 도서인 경우에는 BOOK_YN 사용한다.
            if(target.equals(TARGET.BOOK)){
                prefix.append("<BOOK_YN:contains:Y>");
            }
            //CATEGORY
            if(!strCtgId.equals("")){
                prefix.append("<DISP_CTG_LST:contains:").append(strCtgId).append(">");
            }
            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_B2C_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_LST:contains:B2E|").append(coId).append(">");
                }
            }
            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            return 500;
        }
        public String getMultiGroupBy(Parameter parameter) {
            return null;
        }
        public String getFilter(Parameter parameter) {
            return null;
        }
        public boolean useAlias() {
            return true;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "pnshopSd";
        }
        public String getCollectionAliasName() {
            return "pnshopSd";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "pnshop";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	PNSHOP{
		public String getDocumentField(Parameter parameter) {
			return "SITE_NO,ORI_SITE_NO,DISP_CMPT_ID,DISP_CMPT_NM,IMG_FILE_NM1,IMG_FILE_NM2,IMG_FILE_NM3,IMG_FILE_NM4,MOD_DTS,DISP_CMPT_TYPE_DTL_LST,MOBILE_DISPLAY_YN,MAI_TITLE_NM_1,MAI_TITLE_NM_2,SUBTITL_NM_1,SUBTITL_NM_2,OSMU_YN,PNSHOP_TYPE_CD";
		}
		public String getSearchField(Parameter parameter) {
			return "DISP_CMPT_NM,SRCHWD_NM,BRAND_LST";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			TARGET target = util.getTarget(parameter);
			String strCtgId = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
			FrontUserInfo userInfo = parameter.getUserInfo();
			// SHINSEGAE MALL
			if(site.getSiteNo().equals("6004")){
			    prefix.append("<SITE_NO:contains:6004>");
            }
			// S.COM
			else if(site.getSiteNo().equals("6005")){
                prefix.append("<SITE_NO:contains:6005>");
                prefix.append("<ORI_SITE_NO:contains:6001|6002|6003|6004>");
            }
			// EMART, TRADERS, BOONS
			else if(site.getSiteNo().equals("6001")||site.getSiteNo().equals("6002")){ //@ BOOT개발시확인 
                String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
                if(!strFilterSiteNo.equals("")){
                    prefix.append("<SITE_NO:contains:").append(strFilterSiteNo).append(">");
                }else
                    prefix.append(site.getSitePrefixItem());
            }
			else{
			    prefix.append(site.getSitePrefix());
			}
			// 도서인 경우에는 BOOK_YN 사용한다.
			if(target.equals(TARGET.BOOK)){
				prefix.append("<BOOK_YN:contains:Y>");
			}
			//CATEGORY
			if(!strCtgId.equals("")){
				prefix.append("<DISP_CTG_LST:contains:").append(strCtgId).append(">");
			}
			// B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_B2C_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_LST:contains:B2E|").append(coId).append(">");
                }
            }
            if(!site.getSiteNo().equals("6009")){
            	prefix.append("<OSMU_YN:contains:Y>");
            }
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
		    return 500;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "pnshop";
		}
		public String getCollectionAliasName() {
		    return "pnshop";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "pnshop";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	POSTNG{
	    public String getDocumentField(Parameter parameter) {
            return "SITE_NO,ITEM_ID,ITEM_NM,POSTNG_ID,POSTNG_TITLE_NM,POSTNG_EVAL_SCR,POSTNG_WRTPE_IDNF_ID,SELLPRC,SALESTR_LST,ITEM_REG_DIV_CD";
        }
        public String getSearchField(Parameter parameter) {
            SITE site = util.getSite(parameter);
            return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,MDL_NM,BRAND_NM,TAG_NM,GNRL_STD_DISP_CTG";
        }
        public String getPrefixField(Parameter parameter) {
            /* 필수적인 파라메터 */
            StringBuilder prefix = new StringBuilder();
            SITE   site   = util.getSite(parameter);
            TARGET target = util.getTarget(parameter);
            String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            FrontUserInfo userInfo = parameter.getUserInfo();

            // @@@ 정기배송지 파라메터가 있을 경우에는 이마트 상품의 정기배송만 건다. (특수한 경우임) @@@
            String strPerdcSalestrNo = StringUtils.defaultIfEmpty(parameter.getPerdcSalestrNo(), "");
            String strPerdcRsvtShppPsblYn = StringUtils.defaultIfEmpty(parameter.getPerdcRsvtShppPsblYn(), "");
            if(!strPerdcSalestrNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:EM><SALESTR_LST:contains:")
                    .append(strPerdcSalestrNo)
                    .append(strPerdcRsvtShppPsblYn)
                    .append(">")
                ;
                return prefix.toString();
            }

            // SRCH_PREFIX, or 조건등의 버그가 있기 때문에 SITE에 제한적으로 걸기로 함
            if(strFilterSiteNo.equals("")){
                prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(">")
                ;
            }
            // filterSiteNo에 조건이 들어올 경우 복합적으로 설정
            else{
                SITE fSite = util.getFilterSite(parameter);
                if(fSite.equals(SITE.EMART)||fSite.equals(SITE.TRADERS)||fSite.equals(SITE.BOONS)){
                	prefix
                    .append("<SRCH_PREFIX:contains:").append(fSite.getSitePrefixSpecificNm(parameter)).append(">");
                }else{
                	prefix
                    .append("<SRCH_PREFIX:contains:")
                    .append(site.getSitePrefixNm(parameter))
                    .append(" ")
                    .append(fSite.getSitePrefixSpecificNm(parameter))
                    .append(">")
                ;
                }
            }

            // SALESTR OR MBR_PREFIX
            String mbrPrefix = StringUtils.defaultIfEmpty(parameter.getMbrPrefix(), "");
            if(!mbrPrefix.equals("")){
                prefix.append("<MBR_PREFIX:contains:").append(mbrPrefix).append(">");
            }
            else{
                prefix
                    .append("<SALESTR_LST:contains:")
                    .append(site.getSiteSalestrPrefixNm(parameter))
                    .append(">")
                ;
            }


            // B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_TGT_VEN_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_MBRCO_LST:contains:B2E|").append(coId).append(">");
                }
            }

            // SPL_VEN_ID
            String splVenId = StringUtils.defaultIfEmpty(parameter.getSplVenId(), "");
            if(!splVenId.equals("")){
                prefix
                    .append("<SPL_VEN_ID:contains:").append(splVenId).append(">")
                ;
            }
            // LRNK_SPL_VEN_ID
            String lrnkSplVenId = StringUtils.defaultIfEmpty(parameter.getLrnkSplVenId(), "");
            if(!lrnkSplVenId.equals("")){
            	prefix
            		.append("<LRNK_SPL_VEN_ID:contains:").append(lrnkSplVenId).append(">");
            }

            // BRAND_ID
            String strBrandId = StringUtils.defaultIfEmpty(parameter.getBrand(), "");
            if(!strBrandId.equals("")){
                prefix.append("<BRAND_ID:contains:").append(strBrandId).append(">");
            }

            // CTG_ID ( DISP_CTG_*CLS_ID 를 사용 )
            String strCtgId     = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");                 // ctgId (한개)
            String strCtgIds    = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");                // ctgIds(여러개)
            String strThemeCtgIds    = StringUtils.defaultIfEmpty(parameter.getThemeCtgIds(), "");                // ctgIds(여러개)
            String strCtglv     = StringUtils.defaultIfEmpty(parameter.getCtgLv(),"0");                 // ctgLv (현재레벨)
            String strCtgLast   = StringUtils.defaultIfEmpty(parameter.getCtgLast(),"N");               // 마지막카테고리여부
            String filterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(),"");
            String themeYn      = StringUtils.defaultIfEmpty(parameter.getThemeYn(),"N");
            prefix.append(getCtgPrefix(site,strCtgId,strCtgIds,strThemeCtgIds,strCtglv,strCtgLast,filterSiteNo,"item",themeYn));

//            // COLOR
//            String strColor = StringUtils.defaultIfEmpty(parameter.getColor(), "");
//            if(!strColor.equals("")){
//                if(!strColor.equals(""))prefixVo.append("<COLOR_LST:contains:").append(strColor).append(">");
//            }
//            // SIZE
//            String strSize  = StringUtils.defaultIfEmpty(parameter.getSize(), "");
//            if(!strSize.equals("")){
//                if(!strSize.equals(""))prefixVo.append("<SIZE_LST:contains:").append(strSize).append(">");
//            }

            // 배송 이라고 하나 상품과 연관있음 대신 SITE 관련 필드와 AND 조건
            // SSG -> 점포예약배송, 무료배송, 해외배송
            // EMART -> 무료배송, 사은품
            String strShpp = StringUtils.defaultIfEmpty(parameter.getShpp(), "");
            if(!strShpp.equals("")){
                prefix.append("<SHPP:contains:");
                for(StringTokenizer st = new StringTokenizer(strShpp,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }

            // CLS
            String strCls = StringUtils.defaultIfEmpty(parameter.getCls(),"");
            if(!strCls.equals("")){
                prefix.append("<CLS:contains:");
                for(StringTokenizer st = new StringTokenizer(strCls,"|");st.hasMoreTokens();){
                    prefix.append(FILTER.valueOf(st.nextToken().toUpperCase()).getPrefix()).append("|");
                }
                prefix.deleteCharAt(prefix.lastIndexOf("|")).append(">");
            }

            // Filter < S.COM은 혜택 영역으로 한가지로 통합되어 있으므로 서로 or 조건이 되어 버린다 >
            String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
            String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
            if(!strFilter.equals("")){
                prefix.append("<FILTER:contains:").append(strFilter).append(">");
            }
            if(!strBenefit.equals("")){
                prefix.append("<FILTER:contains:").append(strBenefit).append(">");
            }

            // 전시적용범위코드(디바이스코드)
            String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!strAplTgtMediaCd.equals("")){
            	prefix.append("<DISP_APL_RNG_TYPE_CD:contains:").append(strAplTgtMediaCd).append(">");
            }

            if(site.equals(SITE.SSG) && strFilterSiteNo.equals("")){
         		prefix.append("<SCOM_EXPSR_YN:contains:Y>");
         	}

            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            if (parameter.getSrchVer() >= 2.0) {
            	return 9;		//개편때 9개로 통일
            } else {
	        	switch(util.getSite(parameter)){
	            case EMART:
	                return 3;
	            case TRADERS:
	                return 3;
	            case BOONS:
	                return 3;
	            case SSG:
	                return 2;
	            default:
	                return 3;
	            }
            }
        }
        public String getMultiGroupBy(Parameter parameter) {
            return null;
        }
        public String getFilter(Parameter parameter) {
            return null;
        }
        public boolean useAlias() {
            return false;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "postng";
        }
        public String getCollectionAliasName() {
            return "postng";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "postng";
        }
        public String getCategoryBoost(Parameter parameter) {
            return "postng";
        }
        public String getCollectionRanking(Parameter parameter) {
            SITE site = util.getSite(parameter);
            if(site.equals(SITE.SSG)){
                return "6005";
            }else if(site.equals(SITE.SHINSEGAE)||site.equals(SITE.DEPARTMENT)){
                return "6004";
            }else if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
                return "6001";
            }
            return "6005";
        }
        public String getCollectionSort(Parameter parameter) {
            return "postng";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	FAQ{
		public String getDocumentField(Parameter parameter) {
			return "POSTNG_ID,POSTNG_TITLE_NM,POSTNG_CNTT,POSTNG_BRWS_CNT";
		}
		public String getSearchField(Parameter parameter) {
			return "POSTNG_TITLE_NM,POSTNG_CNTT";
		}
		public String getPrefixField(Parameter parameter) {
		    FrontUserInfo userInfo = parameter.getUserInfo();
		    String strSiteNo       = parameter.getSiteNo();
		    if(userInfo!=null && "B2E".equals(userInfo.getMbrType())){
		        // SEC
		        if("0000000040".equals(userInfo.getMbrCoId()) ||
		           "0000000041".equals(userInfo.getMbrCoId())
		                ){
		            return "<SITE_TYPE_CD:contains:SEC>";
		        }
		        // SFC
		        else if("0000000107".equals(userInfo.getMbrCoId()) ||
		                "0000000109".equals(userInfo.getMbrCoId())
		                ){
		            return "<SITE_TYPE_CD:contains:SFC>";
		        }else{
		            return "<SITE_TYPE_CD:contains:SSG>";
		        }
		    }else{
		        return "<SITE_TYPE_CD:contains:SSG>";
		    }
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
			default:
				return 10;
			}
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "faq";
		}
		public String getCollectionAliasName() {
		    return "faq";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "faq";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	NOTICE{
		public String getDocumentField(Parameter parameter) {
			return "POSTNG_ID,POSTNG_TITLE_NM,WRT_DATE,SITE_NO,SITE_NM,NEWIMG,PAGENO";
		}
		public String getSearchField(Parameter parameter) {
			return "POSTNG_TITLE_NM,SITE_NM,POSTNG_CNTT";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			//모바일,pc
            String aplTgtMediaCd = parameter.getAplTgtMediaCd();
            if(aplTgtMediaCd != null){
            	prefix.append("<DVIC_DIV_CD:contains:").append(aplTgtMediaCd).append(">");
            }
            return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 5;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "notice";
		}
		public String getCollectionAliasName() {
		    return "notice";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "notice";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return "notice";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	RECIPE{
		public String getDocumentField(Parameter parameter) {
			return "RECIPE_NO,RECIPE_NM,COOK_CLS_CD,IMG_PATH_NM,IMG_FILE_NM,IMG_RPLC_WORD_NM,COOK_KEYWD_NM,READY_TIME_CD,RQRM_TIME_CD,COOK_DIFCLVL_CD,RECIPE_WRTPE_ID,RECIPE_BRWS_CNT,RECIPE_RCMD_CNT,ETC_INGRD_NM,COOK_TGT_AGEGRP_CD";
		}
		public String getSearchField(Parameter parameter) {
			return "RECIPE_NO,RECIPE_NM,COOK_KEYWD_NM,ETC_INGRD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			return null;
		}
		public int getDefaultItemCount(Parameter parameter) {
		    switch(util.getSite(parameter)){
            case EMART:
                return 5;
            case TRADERS:
                return 5;
            case BOONS:
                return 5;
            default:
                return 6;
            }
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "recipe";
		}
		public String getCollectionAliasName() {
		    return "recipe";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "recipe";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BANR{
		public String getDocumentField(Parameter parameter) {
			return "SRCH_CRITN_ID,SHRTC_DIV_CD,SHRTC_TGT_TYPE_CD,IMG_FILE_NM,LINK_URL,LIQUOR_YN,BANR_RPLC_TEXT_NM,POP_YN";
		}
		public String getSearchField(Parameter parameter) {
			return "CRITN_SRCHWD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			// 이마트, 트레이더스, 분스 일 경우에는 filterSiteNo에 따라서 변함
            if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
                if(strFilterSiteNo.equals("")){
                    prefix.append(site.getSitePrefix());
                }
                else {
                    SITE fSite = util.getFilterSite(parameter);
                    prefix.append(fSite.getSitePrefix());
                }
            }else{
                prefix.append(site.getSitePrefix());
            }
            
            //배너 디바이스 구분
            if(!StringUtils.isBlank(parameter.getAplTgtMediaCd())){
				prefix.append( "<SHRTC_TGT_TYPE_CD:contains:".concat(parameter.getAplTgtMediaCd()).concat(">"));
			}
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
			default:
				return 3;
			}
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "banr";
		}
		public String getCollectionAliasName() {
		    return "banr";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "banr";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	BANR_EVER{
		public String getDocumentField(Parameter parameter) {
			return "SRCH_CRITN_ID,SHRTC_DIV_CD,SHRTC_TGT_TYPE_CD,IMG_FILE_NM,LINK_URL,LIQUOR_YN,BANR_RPLC_TEXT_NM,SITE_NO,EVER_BANR_YN,POP_YN";
		}
		public String getSearchField(Parameter parameter) {
			return "CRITN_SRCHWD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
			// 이마트, 트레이더스, 분스 일 경우에는 filterSiteNo에 따라서 변함
			if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
				if(strFilterSiteNo.equals("")){
					prefix.append(site.getSitePrefix());
				}
				else {
					SITE fSite = util.getFilterSite(parameter);
					prefix.append(fSite.getSitePrefix());
				}
			}else{
				prefix.append(site.getSitePrefix());
			}
			prefix.append("<EVER_BANR_YN:contains:Y>");
			
			//배너 디바이스 구분
            if(!StringUtils.isBlank(parameter.getAplTgtMediaCd())){
				prefix.append( "<SHRTC_TGT_TYPE_CD:contains:".concat(parameter.getAplTgtMediaCd()).concat(">"));
			}
            
			return prefix.toString();
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
			default:
				return 10;
			}
		}
		public int getPrefixOperator(Parameter parameter) {
			return 0;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "banr_ever";
		}
		public String getCollectionAliasName() {
			return "banr_ever";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "banr";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
			return null;
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	SRCHGUIDE{
		public String getDocumentField(Parameter parameter) {
			return "CRITN_SRCHWD_NM,SRCH_TYPE_CD,PNSHOP,ISSUE_ITEM,SOCIAL,RECIPE,TGT_ITEM,KEYWD_NM_LST,ITEM_ID_LST,SHRTC_TGT_TYPE_CD,SHRTC_TGT_ID,LINK_URL";
		}
		public String getSearchField(Parameter parameter) {
			return "CRITN_SRCHWD_NM,TGT_SRCHWD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            // 이마트, 트레이더스, 분스 일 경우에는 filterSiteNo에 따라서 변함
            if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
                if(strFilterSiteNo.equals("")){
                    prefix.append(site.getSitePrefix());
                }
                else {
                    SITE fSite = util.getFilterSite(parameter);
                    prefix.append(fSite.getSitePrefix());
                }
            }else{
                prefix.append(site.getSitePrefix());
            }
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 5;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "srchguide";
		}
		public String getCollectionAliasName() {
		    return "srchguide";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "srchguide";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	SPELL{
		public String getDocumentField(Parameter parameter) {
			return "CRITN_SRCHWD_NM,RPLC_KEYWD_NM";
		}
		public String getSearchField(Parameter parameter) {
			return "CRITN_SRCHWD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			return prefix.toString();
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
			default:
				return 1;
			}
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "spell";
		}
		public String getCollectionAliasName() {
		    return "spell";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "spell";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	SRCHWDRL{
	    public String getDocumentField(Parameter parameter) {
            return "SRCHWD_NM,RL_KEYWD_NM";
        }
        public String getSearchField(Parameter parameter) {
            return "SRCHWD_NM";
        }
        public String getPrefixField(Parameter parameter) {
            StringBuilder prefix = new StringBuilder();
            SITE site = util.getSite(parameter);
            // SITE_NO
            prefix.append(site.getSitePrefix());
            return prefix.toString();
        }
        public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
        public int getDefaultItemCount(Parameter parameter) {
            switch(util.getSite(parameter)){
            default:
                return 10;
            }
        }
        public String getMultiGroupBy(Parameter parameter) {
            return null;
        }
        public String getFilter(Parameter parameter) {
            return null;
        }
        public boolean useAlias() {
            return false;
        }
        public String getCollectionAliasName(Parameter parameter) {
            return "srchrl";
        }
        public String getCollectionAliasName() {
            return "srchrl";
        }
        public String getCollectionRealName(Parameter parameter) {
            return "srchrl";
        }
        public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	SRCHWDRLDW{
		public String getDocumentField(Parameter parameter) {
			return "SRCHWD_NM,RL_KEYWD_NM";
		}
		public String getSearchField(Parameter parameter) {
			return "SRCHWD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			// SITE_NO
			prefix.append(site.getSitePrefix());
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
				default:
					return 10;
			}
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "srchrldw";
		}
		public String getCollectionAliasName() {
			return "srchrldw";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "srchrldw";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
			return null;
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	PERSON_ITEM{
		public String getDocumentField(Parameter parameter) {
			return "PURCH_ITEM_LST";
		}
		public String getSearchField(Parameter parameter) {
			return "MBR_ID";
		}
		public String getPrefixField(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			StringBuilder prefix = new StringBuilder();
			prefix.append("<MBR_PREFIX:contains:").append(userInfo.getMbrId()).append(">");
			return prefix.toString();
		}
		public int getDefaultItemCount(Parameter parameter) {
			switch(util.getSite(parameter)){
			default:
				return 1;
			}
		}
		public int getPrefixOperator(Parameter parameter) {
			return 0;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "person_item";
		}
		public String getCollectionAliasName() {
			return "person_item";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "person";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
			return null;
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	PERSON{
		public String getDocumentField(Parameter parameter) {
			return "PURCH_ITEM_LST";
		}
		public String getSearchField(Parameter parameter) {
			return "MBR_ID";
		}
		public String getPrefixField(Parameter parameter) {
			return null;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 1;
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "person";
		}
		public String getCollectionAliasName() {
			return "person";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "person";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
			return null;
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	LIFEMAGAZINE{
		public String getDocumentField(Parameter parameter) {
			return "SHPG_MGZ_ID,SHPG_MGZ_NM,SRCHWD,IMG_FILE_NM,MAI_TITLE_NM_1,MAI_TITLE_NM_2,BANR_DESC,DISP_STRT_DTS,LNKD_URL";
		}
		public String getSearchField(Parameter parameter) {
			return "SHPG_MGZ_ID,MAI_TITLE_NM_1,MAI_TITLE_NM_2,BANR_DESC";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();

			//접속 사이트
			String siteNo = parameter.getSiteNo();
			if (StringUtils.isNotBlank(siteNo)) {
				prefix.append("<APL_SITE_NO_LST:contains:").append(siteNo).append(">");
			}
			
			//접속 미디어 체크 10 : PC, 20 : 모바일 - 웹과 앱을 따로 구분하지 않는다.
			String aplTgtMediaCd = parameter.getAplTgtMediaCd();
			if (StringUtils.isNotBlank(aplTgtMediaCd)) {
				prefix.append("<APL_MEDIA_CD:contains:").append(aplTgtMediaCd).append(">");
			}
			
			//쇼핑매거진 유형 코드 D324 10:매거진, 20:매장습격 30:생활의발견
			prefix.append("<SHPG_MGZ_TYPE_CD:contains:").append("10").append(">");
			
			
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
		    return 5;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return true;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "lifemagazine";
		}
		public String getCollectionAliasName() {
		    return "lifemagazine";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "shoppingmagazine";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return "lifemagazine";
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	},
	EVENT{
		public String getDocumentField(Parameter parameter) {
			return "PROM_ID,PROM_NM,PROM_ENFC_STRT_DTS,PROM_ENFC_END_DTS,PROM_TYPE_CD,EVNT_TYPE_CD,OFFER_KIND_CD,SITE_NO,LINK_URL";
		}
		public String getSearchField(Parameter parameter) {
			return "PROM_NM";
		}
		public String getPrefixField(Parameter parameter) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			StringBuilder prefix = new StringBuilder();
			// SITE_NO
			String siteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			if(!siteNo.equals("")){
				prefix.append("<SITE_NO:contains:").append(siteNo).append(">");
			}
			// DISP_DVIC_CD
            String deviceCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
            if(!deviceCd.equals("")){
            	prefix.append("<DISP_DVIC_CD:contains:").append(deviceCd).append(">");
            }
			// B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    prefix.append("<APL_B2C_LST:contains:").append(coId).append(">");
                }else if(type.equals("B2E")){
                    prefix.append("<APL_B2E_LST:contains:B2E|").append(coId).append(">");
                }
            }
            // APL_CHNL_LST
            String chnl = userInfo.getChnlId();
            if(chnl!=null && !chnl.equals("")){
            	prefix.append("<APL_CHNL_LST:contains:ALL|").append(chnl).append(">");
            }else{
            	prefix.append("<APL_CHNL_LST:contains:ALL>");
            }
			return prefix.toString();
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 5;
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			// NOT IN 을 이용해서 제외조건을 건다.
			FrontUserInfo userInfo = parameter.getUserInfo();
			StringBuilder filter = new StringBuilder();
			StringBuilder q = new StringBuilder();
			// B2C, B2E
            String type = userInfo.getMbrType();
            String coId = userInfo.getMbrCoId();
            String chnl = userInfo.getChnlId();
            if(type!=null && coId!=null){
                if(type.equals("B2C")){
                    q.append("B2C_").append(coId);
                }else if(type.equals("B2E")){
                    q.append("B2E_").append(coId);
                }
            }
            if(chnl!=null && !chnl.equals("")){
            	if(q!=null && !q.equals(""))q.append(" ");
            	q.append("CHNL_").append(chnl);
            }
			filter.append("<EXCP_TYPE_LST:notin:").append(q.toString()).append(">");
			return filter.toString();
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "event";
		}
		public String getCollectionAliasName() {
			return "event";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "event";
		}
		public String getCategoryBoost(Parameter parameter) {
			return null;
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
			return "event";
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	SPPRICE{
		public String getDocumentField(Parameter parameter) {
			return "SITE_NO,ITEM_ID,ITEM_NM,BRAND_NM,SP_TYPE,SSG_DISP_YN";
		}
		public String getSearchField(Parameter parameter) {
			return "ITEM_ID,ITEM_NM,ITEM_SRCHWD_NM,BRAND_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
			//SpType :10-오반장, 20-해바
			//SSG에서는 모두 노출 
			if(strSiteNo.equals("6005")){
				prefix.append("<SITE_NO:contains:6001|6002|6003|6004|6009>");
				prefix.append("<SSG_DISP_YN:contains:Y>");
			}else if(strSiteNo.equals("6004")){
				prefix.append("<SITE_NO:contains:6004|6009>");
				prefix.append("<SP_TYPE:contains:20|30>"); 
			}else if(strSiteNo.equals("6001")){
				prefix.append("<SITE_NO:contains:6001|6002>");  //@ BOOT개발시확인 
				prefix.append("<SP_TYPE:contains:10|30>"); 
			}else if(strSiteNo.equals("6002")){ //@ BOOT개발시확인 
				prefix.append("<SITE_NO:contains:").append(strSiteNo).append(">");
				prefix.append("<SP_TYPE:contains:10|30>"); 
			}else if(strSiteNo.equals("6009")){
				prefix.append("<SITE_NO:contains:").append(strSiteNo).append(">");
				prefix.append("<SP_TYPE:contains:20|30>"); 
			}	
			return prefix.toString();
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 40;
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "spprice";
		}
		public String getCollectionAliasName() {
			return "spprice";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "spprice";
		}
		public String getCategoryBoost(Parameter parameter) {
			return "spprice";
		}
		public String getCollectionRanking(Parameter parameter) {
			return null;
		}
		public String getCollectionSort(Parameter parameter) {
			return "spprice";
		}
		public String getPropGrouping(Parameter parameter) {
			return null;
		}
	},
	ISSUETHEME{
		public String getDocumentField(Parameter parameter) {
			return "CURA_ID,CURA_SRCH_TYPE_CD,SITE_NO,TGT_SRCHWD_NM,CRITN_SRCHWD_NM,RPLC_KEYWD_NM,ITEM_ID_LST,DISP_ORDR,DISP_STRT_DTS,DISP_END_DTS";
		}
		public String getSearchField(Parameter parameter) {
			return "CRITN_SRCHWD_NM";
		}
		public String getPrefixField(Parameter parameter) {
			StringBuilder prefix = new StringBuilder();
			SITE site = util.getSite(parameter);
			String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
            // 이마트, 트레이더스, 분스 일 경우에는 filterSiteNo에 따라서 변함
            if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
                if(strFilterSiteNo.equals("")){
                    prefix.append(site.getSitePrefix());
                }
                else {
                    SITE fSite = util.getFilterSite(parameter);
                    prefix.append(fSite.getSitePrefix());
                }
            }else{
                prefix.append(site.getSitePrefix());
            }
			return prefix.toString();
		}
		public int getPrefixOperator(Parameter parameter) {
			return 1;
		}
		public int getDefaultItemCount(Parameter parameter) {
			return 10;
		}
		public String getMultiGroupBy(Parameter parameter) {
			return null;
		}
		public String getFilter(Parameter parameter) {
			return null;
		}
		public boolean useAlias() {
			return false;
		}
		public String getCollectionAliasName(Parameter parameter) {
			return "issuetheme";
		}
		public String getCollectionAliasName() {
		    return "issuetheme";
		}
		public String getCollectionRealName(Parameter parameter) {
			return "issuetheme";
		}
		public String getCategoryBoost(Parameter parameter) {
            return null;
        }
        public String getCollectionRanking(Parameter parameter) {
            return null;
        }
        public String getCollectionSort(Parameter parameter) {
            return null;
        }
        public String getPropGrouping(Parameter parameter) {
            return null;
        }
	}
	;

	/**
	 * ENUMSET -> Abstract Method 를 이용해 주요부분은 각자 구현하도록 합니다.
	 * @return
	 */

	public abstract String getDocumentField(Parameter parameter); 		// Document Meta Field 를 정의합니다.
	public abstract String getSearchField(Parameter parameter);			// Search Field ( 조건이 되는 검색 색인 필드 ) 를 정의합니다.
	public abstract String getPrefixField(Parameter parameter);			// Prefix 프로토콜을 만들어서 정의합니다.
	public abstract int    getPrefixOperator(Parameter parameter);		// Prefix 연산자를 결정합니다. (AND/OR)
	public abstract int    getDefaultItemCount(Parameter parameter);	// 결과 ITEM 수의 컬렉션 별 Default 숫자를 정의합니다.
	public abstract String getMultiGroupBy(Parameter parameter);		// MultiGroupBy 필드를 정의합니다
	public abstract String getFilter(Parameter parameter);				// Filter 필드를 정의합니다.
	public abstract boolean useAlias();									// Collection Name 에 Alias를 사용하여 여러컬렉션에 질의를 날리는지 여부를 정의합니다.
	public abstract String getCollectionAliasName(Parameter parameter);					// Alias 를 주었다면 Alias 명을, 아니면 real Name을 그대로 정의합니다.
	public abstract String getCollectionAliasName();					// Alias 를 주었다면 Alias 명을, 아니면 real Name을 그대로 정의합니다.
	public abstract String getCollectionRealName(Parameter parameter);						// Collection 별 엔진에서 사용되는 real name을 정의합니다.
	public abstract String getCategoryBoost(Parameter parameter);       // Collection 별 카테고리 부스팅 정보를 정의합니다.
	public abstract String getCollectionRanking(Parameter parameter);   // Collection 별 랭킹 정보를 정의합니다.
	public abstract String getCollectionSort(Parameter parameter);      // Sort
	public abstract String getPropGrouping(Parameter parameter);        // 속성 그룹핑 정의

	public ParameterUtil util = new ParameterUtil();					// Parameter 의 핸들링에 사용되는 공통 Class 입니다.

	/**
	 * 카테고리에 사용되는 공통 Prefix method
	 */
	public String getCtgPrefix(SITE site, String ctgId, String ctgIds, String themeCtgIds, String ctgLv, String ctgLast, String filterSiteNo, String collectionName, String themeYn){
	    StringBuilder prefix = new StringBuilder();
	    StringBuilder ctgNmPrefix = new StringBuilder();
	    // CTG IDS 부터
	    // SITE 구분 (SSG 인 경우 앞에 SCOM 을 붙인다)
        if(themeYn.equals("Y")){
            ctgNmPrefix.append("TEM_");
        }else{
            if(site.equals(SITE.SSG)){
                ctgNmPrefix.append("SCOM_");
            }
        }
        // 카테고리와 상품은 걸어야 할 카테고리들이 다르다.
        if(collectionName.equalsIgnoreCase("category")){
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
        }else{
            if(ctgLv.equals("1")){
                ctgNmPrefix.append("DISP_CTG_LCLS_ID");
            }else if(ctgLv.equals("2")){
                ctgNmPrefix.append("DISP_CTG_MCLS_ID");
            }else if(ctgLv.equals("3")){
                ctgNmPrefix.append("DISP_CTG_SCLS_ID");
            }else if(ctgLv.equals("4")){
                ctgNmPrefix.append("DISP_CTG_DCLS_ID");
            }else{
                ctgNmPrefix.append("DISP_CTG_LCLS_ID");
            }
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

	public String getBookCtgPrefix(SITE site,String lvl, String strCtgId, String strCtgIds){
		StringBuilder prefix = new StringBuilder();
		StringBuilder ctgNmPrefix = new StringBuilder();
		String ctgId = "";
		/* 이마트 몰의 경우 ctgIds 가 넘어왔을때는 선택된 카테고리만 내려준다. 신몰은 전체 카테고리를 변경하지 않음. */
		if(strCtgIds!=null && !strCtgIds.equals("") && (site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS))){
		    ctgId = strCtgIds;

		}else if(strCtgId!=null && !strCtgId.equals("")){
		    ctgId = strCtgId;
		}

		if(site.equals(SITE.SSG)){
            ctgNmPrefix.append("SCOM_");
        }

		for(StringTokenizer st = new StringTokenizer(ctgId,"|");st.hasMoreTokens();){
			String c = st.nextToken();
			if(prefix.length()>0)prefix.append("|");
			if(lvl.equals("1")){
			    ctgNmPrefix.append("DISP_CTG_LCLS_ID");
				prefix.append("<").append(ctgNmPrefix).append(":contains:").append(c).append(">");
			}else if(lvl.equals("2")){
			    ctgNmPrefix.append("DISP_CTG_MCLS_ID");
				prefix.append("<").append(ctgNmPrefix).append(":contains:").append(c).append(">");
			}else if(lvl.equals("3")){
			    ctgNmPrefix.append("DISP_CTG_SCLS_ID");
				prefix.append("<").append(ctgNmPrefix).append(":contains:").append(c).append(">");
			}else if(lvl.equals("4")){
			    ctgNmPrefix.append("DISP_CTG_DCLS_ID");
				prefix.append("<").append(ctgNmPrefix).append(":contains:").append(c).append(">");
			}
		}
		return prefix.toString();
	}
	public String getParentBookCtgPrefix(SITE site,String lvl, String strCtgId, String strCtgIds){
	    StringBuilder prefix = new StringBuilder();
	    StringBuilder ctgNmPrefix = new StringBuilder();
	    String ctgId = "";
	    if(strCtgIds!=null && !strCtgIds.equals(""))
	        ctgId = strCtgIds;
	    else if(strCtgId!=null && !strCtgId.equals(""))
	        ctgId = strCtgId;

	    if(site.equals(SITE.SSG)){
	        ctgNmPrefix.append("SCOM_");
	    }

	    for(StringTokenizer st = new StringTokenizer(ctgId,"|");st.hasMoreTokens();){
	        String c = st.nextToken();
	        if(prefix.length()>0)prefix.append("|");
	        if(lvl.equals("2")){
	            ctgNmPrefix.append("DISP_CTG_LCLS_ID");
	            prefix.append("<").append(ctgNmPrefix).append(":contains:").append(c).append(">");
	        }else if(lvl.equals("3")){
	            ctgNmPrefix.append("DISP_CTG_MCLS_ID");
	            prefix.append("<").append(ctgNmPrefix).append(":contains:").append(c).append(">");
	        }else if(lvl.equals("4")){
	            ctgNmPrefix.append("DISP_CTG_SCLS_ID");
	            prefix.append("<").append(ctgNmPrefix).append(":contains:").append(c).append(">");
	        }
	    }
	    return prefix.toString();
	}
}