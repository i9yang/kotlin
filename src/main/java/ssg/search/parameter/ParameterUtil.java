package ssg.search.parameter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.constant.COLLECTION;
import ssg.search.constant.SITE;
import ssg.search.constant.SORT;
import ssg.search.constant.TARGET;

import java.util.StringTokenizer;
/**
 * Parameter Bean 과 관련된 공통적인 기능들을 정의한 클래스
 * @author 131544
 *
 */
public class ParameterUtil {
	private Logger logger = LoggerFactory.getLogger(ParameterUtil.class);
	enum DefaultValues{
		TARGET("ALL")
		,SITE("SSG")
		,QUERY("")
		,COUNT("30")
		,PAGE("1")
		,CTG_LV("0")
		,CTG_LAST("N")
		,CTG_ID("")
		,HIGHLIGHT("N")
		;

		String value;
		DefaultValues(String value){
			this.value = value;
		}
		public String getDefault(){
			return this.value;
		}
	}
	public String getQuery(Parameter parameter){
		return getDefaultStr(parameter.getQuery(), DefaultValues.QUERY, true);
	}
	public String getQuery(Parameter parameter,boolean useUpperCase){
		return getDefaultStr(parameter.getQuery(), DefaultValues.QUERY, useUpperCase);
	}

	public String getCount(Parameter parameter){
		return getDefaultStr(parameter.getCount(), DefaultValues.COUNT, true);
	}
	public String getCount(Parameter parameter,boolean useUpperCase){
		return getDefaultStr(parameter.getCount(), DefaultValues.COUNT, useUpperCase);
	}

	public String getPage(Parameter parameter){
		return getDefaultStr(parameter.getPage(), DefaultValues.PAGE, true);
	}
	public String getPage(Parameter parameter,boolean useUpperCase){
		return getDefaultStr(parameter.getPage(), DefaultValues.PAGE, useUpperCase);
	}
	public String getCtgLv(Parameter parameter){
		return getDefaultStr(parameter.getCtgLv(), DefaultValues.CTG_LV, true);
	}
	public String getCtgLv(Parameter parameter,boolean useUpperCase){
		return getDefaultStr(parameter.getCtgLv(), DefaultValues.CTG_LV, useUpperCase);
	}
	public String getCtgLast(Parameter parameter){
		return getDefaultStr(parameter.getCtgLast(), DefaultValues.CTG_LAST, true);
	}
	public String getCtgLast(Parameter parameter,boolean useUpperCase){
		return getDefaultStr(parameter.getCtgLast(), DefaultValues.CTG_LAST, useUpperCase);
	}
	public String getCtgId(Parameter parameter){
		return getDefaultStr(parameter.getCtgId(), DefaultValues.CTG_ID, true);
	}
	public String getCtgIds(Parameter parameter){
		return getDefaultStr(parameter.getCtgIds(), DefaultValues.CTG_ID, true);
	}
	public String getHighlight(Parameter parameter,boolean useUpperCase){
		return getDefaultStr(parameter.getHighlight(), DefaultValues.HIGHLIGHT, useUpperCase);
	}
	public String getHighlight(Parameter parameter){
		return getDefaultStr(parameter.getHighlight(), DefaultValues.HIGHLIGHT, true);
	}

	private String getDefaultStr(String s, DefaultValues d, boolean useUpperCase){
		if(useUpperCase)return StringUtils.defaultIfEmpty(s, d.getDefault()).toUpperCase();
		else return StringUtils.defaultIfEmpty(s, d.getDefault()).toUpperCase();
	}

	public COLLECTION getCollection(String collectionName){
		return COLLECTION.valueOf(StringUtils.defaultIfEmpty(collectionName, "item").toUpperCase());
	}
	public TARGET getTarget(Parameter parameter){
		return TARGET.valueOf(StringUtils.defaultIfEmpty(parameter.getTarget(), "ALL").toUpperCase());
	}
	public SITE getSite(Parameter parameter){
		String siteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		for(SITE site: SITE.values()){
			if(siteNo.equals(site.getSiteNo())){
				return site;
			}
		}
		return null;
	}
	public SITE getFilterSite(Parameter parameter){
		String siteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "6005");
		for(SITE site: SITE.values()){
			if(siteNo.equals(site.getSiteNo())){
				return site;
			}
		}
		return null;
	}
	public SORT getSort(Parameter parameter){
		return SORT.valueOf(StringUtils.defaultIfEmpty(parameter.getSort(), "BEST").toUpperCase());
	}
	/**
	 * 현재 상태에 맞는 컬렉션 리스트 RETURN
	 * @return
	 */
	public COLLECTION[] getCollectionList(Parameter parameter){
		TARGET target = this.getTarget(parameter);
		SITE   site   = this.getSite(parameter);
		String strFilterSiteNo = StringUtils.defaultIfEmpty(parameter.getFilterSiteNo(), "");
		FrontUserInfo userInfo = parameter.getUserInfo();
		if(target.equals(TARGET.ALL)){
			if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_WARNING) || parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_EXCEPT_AD_WARNING)){
				return new COLLECTION[]{COLLECTION.ITEM, COLLECTION.CATEGORY};
			}else if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_LIMIT) || parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_EXCEPT_AD_LIMIT)){
				return new COLLECTION[]{COLLECTION.ITEM};
			}
			switch(site){
				case EMART :
					if(strFilterSiteNo.equals("") || strFilterSiteNo.equals("6001")){
						return new COLLECTION[]{
								COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
								COLLECTION.RECOM,COLLECTION.PNSHOP,  COLLECTION.POSTNG,
								COLLECTION.FAQ,  COLLECTION.BOOK,	COLLECTION.RECIPE,
								COLLECTION.BANR, COLLECTION.SPELL, 
								COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
						};
					}else{
						return new COLLECTION[]{
								COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
								COLLECTION.RECOM,COLLECTION.PNSHOP,  COLLECTION.POSTNG,
								COLLECTION.BANR, COLLECTION.SPELL, 
								COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
						};
					}
				case TRADERS :
					if(strFilterSiteNo.equals("") || strFilterSiteNo.equals("6001")){
						return new COLLECTION[]{
								COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
								COLLECTION.RECOM,COLLECTION.PNSHOP,  COLLECTION.POSTNG,
								COLLECTION.FAQ,  COLLECTION.BOOK,	COLLECTION.RECIPE,
								COLLECTION.BANR, COLLECTION.SPELL,  
								COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
						};
					}else{
						return new COLLECTION[]{
								COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
								COLLECTION.RECOM,COLLECTION.PNSHOP,  COLLECTION.POSTNG,
								COLLECTION.BANR, COLLECTION.SPELL, 
								COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
						};
					}
				case BOONS :
					if(strFilterSiteNo.equals("") || strFilterSiteNo.equals("6001")){
						return new COLLECTION[]{
								COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
								COLLECTION.RECOM,COLLECTION.PNSHOP,  COLLECTION.POSTNG,
								COLLECTION.FAQ,  COLLECTION.BOOK,	COLLECTION.RECIPE,
								COLLECTION.BANR, COLLECTION.SPELL,
								COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
						};
					}else{
						return new COLLECTION[]{
								COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
								COLLECTION.RECOM,COLLECTION.PNSHOP,  COLLECTION.POSTNG,
								COLLECTION.BANR, COLLECTION.SPELL,  
								COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
						};
					}
				case SHINSEGAE :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
							COLLECTION.RECOM,COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD,  COLLECTION.POSTNG,
							COLLECTION.BOOK, COLLECTION.SPSHOP,  COLLECTION.BANR,
							COLLECTION.SPELL, COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
					};
				case DEPARTMENT :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
							COLLECTION.RECOM,COLLECTION.PNSHOP,  COLLECTION.POSTNG,
							COLLECTION.SPSHOP,  COLLECTION.BANR,
							COLLECTION.SPELL, COLLECTION.SRCHWDRL, COLLECTION.BANR_EVER, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
					};
				case SSG :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
							COLLECTION.RECOM,COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD,  COLLECTION.POSTNG,
							COLLECTION.BOOK, COLLECTION.BANR,	COLLECTION.RECIPE,
							COLLECTION.SPELL,COLLECTION.ISSUETHEME, COLLECTION.SRCHWDRL,
							COLLECTION.BANR_EVER,COLLECTION.LIFEMAGAZINE,COLLECTION.NOTICE, COLLECTION.EVENT, COLLECTION.FAQ, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
					};
				default :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY,COLLECTION.MALL,
							COLLECTION.RECOM,COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD,  COLLECTION.POSTNG,
							COLLECTION.BOOK, COLLECTION.BANR,	COLLECTION.RECIPE,
							COLLECTION.SPELL,COLLECTION.ISSUETHEME, COLLECTION.SRCHWDRL, COLLECTION.SRCHWDRLDW, COLLECTION.SPPRICE
					};
			}
		}else if(target.equals(TARGET.ITEM)){
			if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_WARNING)){
				return new COLLECTION[]{COLLECTION.ITEM};
			}else if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_LIMIT)){
				return new COLLECTION[]{COLLECTION.ITEM};
			}
			switch(site){
				case SHINSEGAE:
					return new COLLECTION[]{COLLECTION.ITEM};
				case DEPARTMENT:
					return new COLLECTION[]{COLLECTION.ITEM};
				case SSG:
					return new COLLECTION[]{COLLECTION.ITEM};
				default :
					return new COLLECTION[]{COLLECTION.ITEM};
			}
		}else if(target.equals(TARGET.CATEGORY)){
			switch(site){
				case SSG :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY, COLLECTION.MALL, COLLECTION.POSTNG
					};
				case EMART :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY, COLLECTION.PNSHOP, COLLECTION.MALL, COLLECTION.POSTNG
					};
				case TRADERS :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY, COLLECTION.PNSHOP, COLLECTION.MALL, COLLECTION.POSTNG
					};
				case BOONS :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY, COLLECTION.PNSHOP, COLLECTION.MALL, COLLECTION.POSTNG
					};
				default :
					return new COLLECTION[]{
							COLLECTION.ITEM, COLLECTION.CATEGORY, COLLECTION.MALL, COLLECTION.POSTNG
					};
			}
		}else if(target.equals(TARGET.POSTNG)){
			switch(site){
				default :
					return new COLLECTION[]{COLLECTION.POSTNG};
			}
		}else if(target.equals(TARGET.PNSHOP)){
			switch(site){
				default :
					return new COLLECTION[]{COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD};
			}
		}
		else if(target.equals(TARGET.FAQ)){
			switch(site){
				default :
					return new COLLECTION[]{COLLECTION.FAQ};
			}
		}else if(target.equals(TARGET.BOOK)){
			if(site.equals(SITE.SSG)){
				return new COLLECTION[]{ COLLECTION.BOOK, COLLECTION.MALL_BOOK, COLLECTION.BOOK_CATEGORY, COLLECTION.PNSHOP, COLLECTION.SRCHWDRL, COLLECTION.SPELL, COLLECTION.SRCHWDRLDW };
			}else return new COLLECTION[]{ COLLECTION.BOOK, COLLECTION.BOOK_CATEGORY, COLLECTION.PNSHOP, COLLECTION.SRCHWDRL, COLLECTION.SPELL, COLLECTION.SRCHWDRLDW };
		}else if(target.equals(TARGET.RECIPE)){
			switch(site){
				default :
					return new COLLECTION[]{COLLECTION.RECIPE};
			}
		}else if(target.equals(TARGET.RECOM)){
			switch(site){
				default :
					return new COLLECTION[]{COLLECTION.RECOM};
			}
		}else if(target.equals(TARGET.MOBILE)){
			if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_WARNING)){
				return new COLLECTION[]{COLLECTION.ITEM, COLLECTION.CATEGORY};
			}else if(parameter.getLoadLevel().equals(parameter.LOAD_LEVEL_LIMIT)){
				return new COLLECTION[]{COLLECTION.ITEM};
			}
			switch(site){
				case EMART:
					return new COLLECTION[]{COLLECTION.ITEM,COLLECTION.BOOK,COLLECTION.SRCHWDRL,COLLECTION.BANR,COLLECTION.SPELL,COLLECTION.MALL, COLLECTION.SRCHWDRLDW};
				case SSG:
					return new COLLECTION[]{COLLECTION.ITEM,COLLECTION.BOOK,COLLECTION.BRAND,COLLECTION.CATEGORY,COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD,COLLECTION.MALL,COLLECTION.SRCHWDRL,COLLECTION.SPELL, COLLECTION.SRCHWDRLDW};
				case SHINSEGAE:
					return new COLLECTION[]{COLLECTION.ITEM,COLLECTION.BOOK,COLLECTION.BRAND,COLLECTION.CATEGORY,COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD,COLLECTION.MALL,COLLECTION.SRCHWDRL,COLLECTION.SPELL, COLLECTION.SRCHWDRLDW};
				case DEPARTMENT:
					return new COLLECTION[]{COLLECTION.ITEM,COLLECTION.BOOK,COLLECTION.BRAND,COLLECTION.CATEGORY,COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD,COLLECTION.MALL,COLLECTION.SRCHWDRL,COLLECTION.SPELL, COLLECTION.SRCHWDRLDW};
				default :
					return new COLLECTION[]{COLLECTION.ITEM,COLLECTION.BOOK,COLLECTION.CATEGORY,COLLECTION.PNSHOP,COLLECTION.PNSHOP_SD,COLLECTION.MALL,COLLECTION.SRCHWDRL,COLLECTION.SPELL, COLLECTION.SRCHWDRLDW};
			}
		}else if(target.equals(TARGET.MOBILE_BOOK)){
			return new COLLECTION[]{COLLECTION.BOOK,COLLECTION.BOOK_CATEGORY,COLLECTION.SRCHWDRL,COLLECTION.MALL};
		}else if(target.equals(TARGET.MOBILE_ITEM)){
			return new COLLECTION[]{COLLECTION.ITEM};
		}else if(target.equals(TARGET.MOBILE_DTL)){
			return new COLLECTION[]{COLLECTION.CATEGORY,COLLECTION.BRAND};
		}else if(target.equals(TARGET.ISSUETHEME)){
			return new COLLECTION[]{COLLECTION.ISSUETHEME};
		}else if(target.equals(TARGET.SPSHOP)){
			return new COLLECTION[]{COLLECTION.SPSHOP};
		}else if(target.equals(TARGET.DISP)){
			switch(site){
				case SSG : return new COLLECTION[]{COLLECTION.DISP, COLLECTION.MALL_DISP, COLLECTION.BOOK_DISP, COLLECTION.BOOK_MALL_DISP};
				default  : return new COLLECTION[]{COLLECTION.DISP, COLLECTION.BOOK_DISP};
			}
		}
		// TARGET : SHOP -> 페레가모
		else if(target.equals(TARGET.SHOP)){
			return new COLLECTION[]{COLLECTION.ITEM};
		}
		// TARGET : BRAND_DISP -> 브랜드 전문관
		else if(target.equals(TARGET.BRAND_DISP)){
			return new COLLECTION[]{COLLECTION.BRAND_TOTAL,COLLECTION.BRAND_DISP, COLLECTION.BRAND_MALL_DISP, COLLECTION.BOOK_BRAND_DISP, COLLECTION.BOOK_BRAND_TOTAL, COLLECTION.BOOK_BRAND_MALL_DISP};
		}
		// TARGET : VIRTUAL -> P&G
		else if(target.equals(TARGET.VIRTUAL)){
			return new COLLECTION[]{COLLECTION.VIRTUAL};
		}
		// TARGET : BRAND_DTL
		else if(target.equals(TARGET.BRAND_DTL)){
			return new COLLECTION[]{COLLECTION.BRAND_DISP};
		}
		// TARGET : NOTICE
		else if(target.equals(TARGET.NOTICE)){
			return new COLLECTION[]{COLLECTION.NOTICE};
		}
		// TARGET : EVENT
		else if(target.equals(TARGET.EVENT)){
			return new COLLECTION[]{COLLECTION.EVENT};
		}
		// TARGET : LIFEMAGAZINE
		else if(target.equals(TARGET.LIFEMAGAZINE)){
			return new COLLECTION[]{COLLECTION.LIFEMAGAZINE};
		}
		// TARGET : PARTNER
		else if(target.equals(TARGET.PARTNER)){
			return new COLLECTION[]{COLLECTION.ITEM};
		}

		//TARGET : SPPRICE
		else if(target.equals(TARGET.SPPRICE)){
			return new COLLECTION[]{COLLECTION.SPPRICE};
		}
		return null;
	}
	public String getCommonQuery(Parameter parameter){
		String strQuery   = StringUtils.defaultIfEmpty(parameter.getQuery(), "");
		String strInclude = StringUtils.defaultIfEmpty(parameter.getInclude(), "");
		String strExclude = StringUtils.defaultIfEmpty(parameter.getExclude(), "");
		String strReplace = StringUtils.defaultIfEmpty(parameter.getReplaceQuery(), "");

		// 검색어 조합

		// 1. 치환키워드가 있을시 검색어는 치환키워드로
		if(!strReplace.equals("")){
			strQuery = strReplace;
		}
		// 2. 결과내 검색어 생성
		if(!strInclude.equals("")){
			StringBuilder sb = new StringBuilder();
			sb.append(strQuery);
			for(StringTokenizer st = new StringTokenizer(strInclude,"|");st.hasMoreTokens();){
				String t = st.nextToken();
				sb.append(" ").append(t);
			}
			strQuery = sb.toString();
		}
		// 3. 제외 키워드 생성
		if(!strExclude.equals("")){
			StringBuilder sb = new StringBuilder();
			sb.append(strQuery);
			for(StringTokenizer st = new StringTokenizer(strExclude,"|");st.hasMoreTokens();){
				String t = st.nextToken();
				sb.append(" !").append(t);
			}
			strQuery = sb.toString();
		}
		return strQuery;
	}
	public String getCategoryGroupField(COLLECTION collection, Parameter parameter){
		SITE   site		   = this.getSite(parameter);
		if(collection.equals(COLLECTION.CATEGORY)){
			if(site.equals(SITE.SSG)){
				return "SCOM_DISP_CTG_IDX";
			}else if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
				return "DISP_CTG_IDX,TEM_DISP_CTG_IDX";
			}else{
				return "DISP_CTG_IDX";
			}
		}else if(collection.equals(COLLECTION.BOOK_CATEGORY)){
			if(site.equals(SITE.SSG)){
				return "SCOM_DISP_CTG_IDX";
			}else if(site.equals(SITE.EMART)||site.equals(SITE.TRADERS)||site.equals(SITE.BOONS)){
				return "DISP_CTG_IDX";
			}else{
				return "DISP_CTG_IDX";
			}
		}
		return "DISP_CTG_IDX";
	}
	public String getCategoryGroupDepth(COLLECTION collection,Parameter parameter){
		TARGET target = this.getTarget(parameter);
		SITE   site   = this.getSite(parameter);
		if(collection.equals(COLLECTION.CATEGORY)||collection.equals(COLLECTION.BOOK_CATEGORY)){
			String ctgLv = this.getCtgLv(parameter);
			if(target.equals(TARGET.CATEGORY)){
				if(ctgLv.equals("0")){
					if(site.equals(SITE.SSG)){
						return "1,2,3";
					}
					return "1,2";
				}
				else if(ctgLv.equals("1"))return "1,2,3";
				else if(ctgLv.equals("2"))return "1,2,3,4";
				else if(ctgLv.equals("3"))return "1,2,3,4";
				else if(ctgLv.equals("4"))return "1,2,3,4";
				return ctgLv;
			}else if(target.equals(TARGET.ALL)){
				if(site.equals(SITE.SSG)){
					return "1,2,3";
				}else return "1,2";
			}else if(target.equals(TARGET.BOOK)){
				if(ctgLv.equals("1"))return "2,3";
				if(ctgLv.equals("2"))return "2,3";
				if(ctgLv.equals("3"))return "2,3,4";
				else return "2,3";
			}else if(target.equals(TARGET.MOBILE)){
				return "1,2";
			}else if(target.equals(TARGET.MOBILE_BOOK)){
				return "2,3";
			}
		}
		return null;
	}
	public String getCategoryViewLevel(COLLECTION collection, Parameter parameter){
		TARGET target = this.getTarget(parameter);
		SITE   site   = this.getSite(parameter);
		if(collection.equals(COLLECTION.CATEGORY)||collection.equals(COLLECTION.BOOK_CATEGORY)){
			String ctgLv   = this.getCtgLv(parameter);
			if(target.equals(TARGET.CATEGORY)){
				if(ctgLv.equals("0")){
					if(site.equals(SITE.SSG)){
						return "1,2";
					}
					return "1";
				}
				else if(ctgLv.equals("1"))return "2";
				else if(ctgLv.equals("2"))return "3";
				else if(ctgLv.equals("3"))return "4";
				else if(ctgLv.equals("4"))return "4";
				return ctgLv;
			}else if(target.equals(TARGET.ALL)){
				if(site.equals(SITE.SSG)){
					return "1,2";
				}else return "1";
			}else if(target.equals(TARGET.BOOK)){
				if(ctgLv.equals("1"))return "2";
				if(ctgLv.equals("2"))return "3";
				if(ctgLv.equals("3"))return "4";
				if(ctgLv.equals("4"))return "4";
				return "2";
			}else if(target.equals(TARGET.MOBILE)){
				return "1";
			}else if(target.equals(TARGET.MOBILE_BOOK)){
				return "2";
			}
		}
		return null;
	}
}
