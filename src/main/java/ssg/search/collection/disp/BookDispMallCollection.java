package ssg.search.collection.disp;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Filters;
import ssg.search.constant.Prefixes;
import ssg.search.function.Filterable;
import ssg.search.function.Groupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.Iterator;

public class BookDispMallCollection implements Collection, Prefixable, Groupable, Filterable{

	public String getCollectionName(Parameter parameter){
		return "book";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "book_disp_mall";
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
				return new Info("SITE_NO,SCOM_EXPSR_YN");
			}
		};
	}

	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_MALL
					, Prefixes.DISP_CTG_LST
					, Prefixes.BRAND_ID
					, Prefixes.SALESTR_LST_MALL
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}

	public Call<Info> getFilter(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
			    return new Info(Filters.PRC_FILTER.getFilter(parameter));
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		// DISP_MALL 은 전시카테고리 사이트 그룹핑 결과를 사용
        String strSiteInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SITE_NO"),"");
        String strScomInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN"),"");
        if(!strSiteInfo.equals("") && !strScomInfo.equals("")){
        	result.setBookMallCountMap(ResultUtils.getSiteGroup(strSiteInfo, strScomInfo));
        }
		return result;
	}
}
