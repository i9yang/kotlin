package ssg.search.collection.brand;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Groupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.Iterator;

public class BrandDispCollection implements Collection, Prefixable, Groupable{

	public String getCollectionName(Parameter parameter) {
		return "item";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "brand_disp";
	}

	public String[] getDocumentField(Parameter parameter){
		// DISP COLLECTION 은 상품의 META DATA를 리턴
		return new String[]{
			"SITE_NO",
			"ITEM_ID"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"ITEM_ID"
		};
	}
	
	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("DISP_CTG_LST");
			}
		};
	}

	public Call<Info> getPrefix(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_DISP
					, Prefixes.BRAND_ID
					, Prefixes.SALESTR_LST_GROUP
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SCOM_EXPSR_YN_ALL
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		String ctgLst = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "DISP_CTG_LST"), "");
		if(!ctgLst.equals("")){
	        result.setCategoryList(ResultUtils.getBrandCategoryGroup(parameter.getSiteNo(), parameter.getDispCtgId(), ctgLst));
		}
		return result;
	}

}
