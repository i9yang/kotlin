package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.BenefitFilters;
import ssg.search.constant.Filters;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.*;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.Iterator;
import java.util.List;

public class ItemCollection implements Collection, Prefixable, Sortable, Filterable, Pageable, Morphable, Rankable, Boostable{

	public String getCollectionName(Parameter parameter){
		return "item";
	}

	public String getCollectionAliasName(Parameter parameter){
		// SITE
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
				
		// DEVICE
		String strAplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "10");
		if(strAplTgtMediaCd.equals("10")){
			return "item".concat(strSiteNo);
		}else{
			//채팅,선물하기
			String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "");
			if(strTarget.equalsIgnoreCase("chat_search_all") || strTarget.equalsIgnoreCase("chat_search_item") ){
				return "mobileChat".concat(strSiteNo);
			}else if(strTarget.equalsIgnoreCase("chat_gift_all") || strTarget.equalsIgnoreCase("chat_gift_item")){
				return "mobileGift".concat(strSiteNo);
			}
			
			return "mobile".concat(strSiteNo);
		}
		
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			// NORMAL META DATA
			"SITE_NO",
			"ITEM_ID",
			"ITEM_NM",
			"SELLPRC",
			"ITEM_REG_DIV_CD",
			"SHPP_TYPE_CD",
			"SHPP_TYPE_DTL_CD",
			"SALESTR_LST",
			"EXUS_ITEM_DIV_CD",
			"EXUS_ITEM_DTL_CD",
			"SHPP_MAIN_CD",
			"SHPP_MTHD_CD"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"ITEM_ID",
			"ITEM_NM",
			"ITEM_SRCHWD_NM",
			"MDL_NM",
			"BRAND_NM",
			"TAG_NM",
			"GNRL_STD_DISP_CTG"
		};
	}

	public Call<Info> getSort() {
		return parameter -> {
			Sorts  	 	sorts 			= Sorts.BEST;
			String	 	strSort 		= StringUtils.defaultIfEmpty(parameter.getSort(), "best");
			List<Sort> 	sortList 		= Lists.newArrayList();
			try{
				sorts = Sorts.valueOf(strSort.toUpperCase());
			} catch(IllegalArgumentException e){}
			if(sorts.equals(Sorts.BEST)){
				sortList.add(Sorts.WEIGHT.getSort(parameter));
				sortList.add(Sorts.RANK.getSort(parameter));
				sortList.add(Sorts.THRD.getSort(parameter));
				sortList.add(Sorts.REGDT.getSort(parameter));
				sortList.add(Sorts.PRCASC.getSort(parameter));
			}else{
				sortList.add(sorts.getSort(parameter));
			}
			return new Info(sortList);
		};
	}

	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX
					, Prefixes.FILTER_SITE_NO
					, Prefixes.SRCH_CTG_ITEM_PREFIX
					, Prefixes.SALESTR_LST
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SIZE
					, Prefixes.COLOR
					, Prefixes.SRCH_PSBL_YN
					, Prefixes.BRAND_ID
					, Prefixes.SCOM_EXPSR_YN
					, Prefixes.BENEFIT_FILTER
					, Prefixes.CLASSIFICATION_FILTER
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				String strCls = StringUtils.defaultIfEmpty(parameter.getCls(), "");
				String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
				String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
				String strShpp = StringUtils.defaultIfEmpty(parameter.getShpp(), "");
				if(strCls.equalsIgnoreCase("emart")){
					sb.append("<").append(BenefitFilters.EMART.getField()).append(":contains:").append(BenefitFilters.EMART.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("emartonline")){
					sb.append("<").append(BenefitFilters.EMARTONLINE.getField()).append(":contains:").append(BenefitFilters.EMARTONLINE.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("department")){
					sb.append("<").append(BenefitFilters.DEPARTMENT.getField()).append(":contains:").append(BenefitFilters.DEPARTMENT.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("shinsegae")){
					sb.append("<").append(BenefitFilters.SHINSEGAE.getField()).append(":contains:").append(BenefitFilters.SHINSEGAE.getPrefix()).append(">");
				}
				if(strBenefit.equalsIgnoreCase("free")){
					sb.append("<").append(BenefitFilters.FREE.getField()).append(":contains:").append(BenefitFilters.FREE.getPrefix()).append(">");
				}
				if(strBenefit.equalsIgnoreCase("prize")){
					sb.append("<").append(BenefitFilters.PRIZE.getField()).append(":contains:").append(BenefitFilters.PRIZE.getPrefix()).append(">");
				}
				if(strFilter.equalsIgnoreCase("free")){
					sb.append("<").append(BenefitFilters.FREE.getField()).append(":contains:").append(BenefitFilters.FREE.getPrefix()).append(">");
				}
				if(strFilter.equalsIgnoreCase("news")){
					sb.append("<").append(BenefitFilters.NEWS.getField()).append(":contains:").append(BenefitFilters.NEWS.getPrefix()).append(">");
				}
				if(strFilter.equalsIgnoreCase("obanjang|spprice")){
					sb.append("<")
						.append(BenefitFilters.OBANJANG.getField())
						.append(":contains:")
						.append(BenefitFilters.OBANJANG.getPrefix())
						.append("|")
						.append(BenefitFilters.SP_PRICE.getPrefix())
						.append(">");
				}
				String shppPrefix = CollectionUtils.getShppPrefix(parameter);
				if(shppPrefix!=null && !shppPrefix.equals("")){
					sb.append(shppPrefix);
				}
				return new Info(sb.toString(),1);
			}
		};
	}

	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "40";
				}
				
				// 검색광고관련추가
				if (parameter.isAdSearch() && parameter.getAdSearchItemCount() > 0) {
					return CollectionUtils.getAdPageInfo(strPage, strCount, parameter.getAdSearchItemCount());
				}
				
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	public Call<Info> getFilter() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info(Filters.PRC_FILTER.getFilter(parameter));
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		
		// 검색광고관련추가
		if (parameter.isAdSearch() && parameter.getAdSearchItemCount() > 0) {
				return ResultUtils.getAdItemResult(name, parameter, result, search);
		}
		
		return ResultUtils.getItemResult(name, parameter, result, search);
	}

	public Call<Info> getMorph(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("ITEM_NM");
			}
		};
	}

	public Call<Info> getBoost(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return CollectionUtils.getCategoryBoosting(parameter);
			}
		};
	}

	public Call<Info> getRank(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return CollectionUtils.getCollectionRanking(parameter);
			}
		};
	}
}
