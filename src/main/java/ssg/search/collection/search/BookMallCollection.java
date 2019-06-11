package ssg.search.collection.search;

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

public class BookMallCollection implements Collection, Prefixable, Groupable{

	public String getCollectionName(Parameter parameter) {
		return "book";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "mall";
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
				"ISBN",
				"BOOK_ENG_NM",
				"ORTITL_NM",
				"SUBTITL_NM",
				"AUTHOR_NM",
				"TRLTPE_NM",
				"PUBSCO_NM"
		};
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
				// Mall Collection 은 SSG 기준으로만 호출한다.
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
						Prefixes.SRCH_PREFIX
						, Prefixes.SALESTR_LST_GROUP
						, Prefixes.MBR_CO_TYPE
						, Prefixes.DEVICE_CD
						, Prefixes.SCOM_EXPSR_YN
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(),1);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		String strSiteInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SITE_NO"),"");
		String strScomInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN"),"");
		if(!strSiteInfo.equals("") && !strScomInfo.equals("")){
			result.setMallCountMap(ResultUtils.getSiteGroup(strSiteInfo, strScomInfo));
		}
		return result;
	}

}
