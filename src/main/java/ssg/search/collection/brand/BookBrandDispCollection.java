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

public class BookBrandDispCollection implements Collection, Groupable, Prefixable{
	public String getCollectionName(Parameter parameter){
		return "book";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "book_brand_disp";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO",
			"ITEM_ID"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{"ITEM_ID"};
	}

	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("DISP_CTG_LST");
			}
		};
	}

	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_DISP
					, Prefixes.BRAND_ID
					, Prefixes.SALESTR_LST
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
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
	        result.setBookCategoryList(ResultUtils.getBrandCategoryGroup(parameter.getSiteNo(), parameter.getDispCtgId(), ctgLst));
		}
		return result;
	}

}
