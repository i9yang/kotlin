package ssg.search.collection.global;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.collection.search.ItemCollection;
import ssg.search.constant.Filters;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.Iterator;
import java.util.List;

public class GlobalItemCollection extends ItemCollection implements Collection, Prefixable{
	
	public String getCollectionName(Parameter parameter) {
		return "item";
	}
	
	public String getCollectionAliasName(Parameter parameter){
		return "global_item";
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
					  Prefixes.SRCH_PREFIX_GLOBAL
					, Prefixes.TEM_DISP_ITEM_CTG_ID
					, Prefixes.SALESTR_LST
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SRCH_PSBL_YN
					, Prefixes.BRAND_ID
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
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
	
	public Call<Info> getFilter() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info(Filters.PRC_FILTER.getFilter(parameter));
			}
		};
	}
}
