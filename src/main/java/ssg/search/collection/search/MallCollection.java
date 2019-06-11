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

public class MallCollection implements Collection, Prefixable, Groupable{

	public String getCollectionName(Parameter parameter) {
		return "item";
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
			"BRAND_NM",
			"TAG_NM",
			"GNRL_STD_DISP_CTG"
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
					  Prefixes.SRCH_PREFIX_MALL
					, Prefixes.SALESTR_LST_MALL
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SRCH_PSBL_YN
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
        	try{
        		result.setNoResultItemCount(Integer.parseInt(result.getMallCountMap().get("6005")));
        	}catch(NumberFormatException e){
        		result.setNoResultItemCount(0);
        	}
        }
		return result;
	}

}
