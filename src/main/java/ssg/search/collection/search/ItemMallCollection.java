package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.*;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.Iterator;
import java.util.List;

public class ItemMallCollection implements Collection, Pageable, Prefixable, Groupable, Sortable, Rankable, Boostable{

	public String getCollectionName(Parameter parameter) {
		return "item";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "mall";
	}

	public String[] getDocumentField(Parameter parameter){
		// SITE
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
		if(!strSiteNo.equals("6005")){
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
	
	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				// Item Mall Collection 은 SSG 기준으로만 호출한다.
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_MALL
					, Prefixes.SALESTR_LST_MALL
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SRCH_PSBL_YN
					, Prefixes.SCOM_EXPSR_YN_SSG
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(),1);
			}
		};
	}
	
	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("SITE_NO,SCOM_EXPSR_YN");
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result) {
        String strSiteInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SITE_NO"),"");
        String strScomInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SCOM_EXPSR_YN"),"");
        if(!strSiteInfo.equals("") && !strScomInfo.equals("")){
        	result.setMallCountMap(ResultUtils.getSiteGroup(strSiteInfo, strScomInfo));
        }
        // S.COM 이 아닌 다른 몰의 경우 실패검색어 SSG 상품 추천 기능을 사용
        if(!StringUtils.defaultIfEmpty(parameter.getSiteNo(), "").equals("6005")){
        	result = ResultUtils.getItemResult(name, parameter, result, search);
        }
		return result;
	}

	public Call<Info> getPage(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = "1";
				String strCount = "1";
				// 일반적인 유입
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
				// S.COM 을 제외하고는 실패검색어 SSG 상품 추천 기능을 사용
				if(strSiteNo.equals("6005")){
					strCount = "1";
				}
				// 검색결과 없음 SSG 상품 추천
				else{
					strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
					strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
					if(strCount.equals("") || strCount.equals("0")){
						strCount = "40";
					}
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	public Call<Info> getSort(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
				List<Sort> 	sortList 		= Lists.newArrayList();
				if(strSiteNo.equals("6005")){
					sortList.add(Sorts.RANK.getSort(parameter));
				}else{
					sortList.add(Sorts.WEIGHT.getSort(parameter));
					sortList.add(Sorts.RANK.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
				}
				return new Info(sortList);
			}
			
		};
	}
	public Call<Info> getRank(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				if(!parameter.getSiteNo().equals("6005")){
					return new Info("6005".concat("/").concat(CollectionUtils.getOriQuery(parameter)));
				}else return null;
			}
		};
	}
	public Call<Info> getBoost(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				if(!parameter.getSiteNo().equals("6005")){
					return new Info("SSGBOOST");
				}else return null;
			}
		};
	}
}
