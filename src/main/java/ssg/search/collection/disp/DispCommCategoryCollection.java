package ssg.search.collection.disp;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.CategoryGroupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.Iterator;

public class DispCommCategoryCollection implements  Collection, Prefixable, CategoryGroupable{
	
	public String getCollectionName(Parameter parameter) {
		return "item";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "category";
	}

	public String[] getDocumentField(Parameter parameter) {
		return new String[]{
			"ITEM_ID", "SITE_NO"
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

	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX
					, Prefixes.FILTER_SITE_NO
					, Prefixes.SALESTR_LST_GROUP
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SCOM_EXPSR_YN
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(),1);
			}
		};
	}

	public Call<Info> getCategoryGroup() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return CollectionUtils.getMobileCategoryGroupBy(parameter,4);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		String idx = "DISP_CTG_IDX";
		if(StringUtils.defaultIfEmpty(parameter.getSiteNo(), "").equals("6005")) idx = "SCOM_DISP_CTG_IDX";
	    result.setCommCategoryList(ResultUtils.getMobileCategoryGroup(search, name, idx, 4));
		return result;
	}
}
