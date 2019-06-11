package ssg.search.collection.bshop;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.collection.search.ItemCollection;
import ssg.search.constant.BenefitFilters;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.Iterator;
import java.util.List;

public class BshopItemCollection extends ItemCollection implements Collection, Prefixable{
	
	public String getCollectionName(Parameter parameter) {
		return "item";
	}
	
	public String getCollectionAliasName(Parameter parameter){
		return "bshop_item";
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
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				Sorts  	 	sorts 			= Sorts.BEST;
				String	 	strSort 		= StringUtils.defaultIfEmpty(parameter.getSort(), "best");
				List<Sort> 	sortList 		= Lists.newArrayList();
				try{
					sorts = Sorts.valueOf(strSort.toUpperCase());
				} catch(IllegalArgumentException e){}
				if(sorts.equals(Sorts.BEST)){
					sortList.add(Sorts.WEIGHT.getSort(parameter));
					sortList.add(Sorts.RANK.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
				}else{
					sortList.add(sorts.getSort(parameter));
				}
				return new Info(sortList);
			}
		};
	}
	
	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_BSHOP
					, Prefixes.SRCH_CTG_PREFIX
					, Prefixes.SALESTR_LST
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SRCH_PSBL_YN
					, Prefixes.BSHOP_ID
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				String strCls = StringUtils.defaultIfEmpty(parameter.getCls(), "");
				String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
				String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
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
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		return ResultUtils.getItemResult(name, parameter, result, search);
	}
	
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "40";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
	
}
