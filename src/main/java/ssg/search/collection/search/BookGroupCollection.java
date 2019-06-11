package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Groupable;
import ssg.search.function.Prefixable;
import ssg.search.function.PropertyGroupable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Brand;
import ssg.search.result.Prc;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.Iterator;
import java.util.List;

public class BookGroupCollection implements Collection, Prefixable, Groupable, PropertyGroupable{

	public String getCollectionName(Parameter parameter) {
		return "book";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "book_group";
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
				return new Info("BRAND_ID");
			}
		};
	}

	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX
					, Prefixes.SRCH_CTG_ITEM_PREFIX
					, Prefixes.SALESTR_LST
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
	
	public Result getResult(Search search, String name, Parameter parameter,Result result) {
		// 가격 그루핑 결과
		int propct = search.w3GetCountPropertyGroup(name);
		int min = search.w3GetMinValuePropertyGroup(name);
		int max = search.w3GetMaxValuePropertyGroup(name);
		List<Prc> prcGroupList = Lists.newArrayList();
		Prc prc;
		for(int i=0;i<propct;i++){
		    prc = new Prc();
		    prc.setMinPrc(search.w3GetMinValueInPropertyGroup(name,i));
		    prc.setMaxPrc(search.w3GetMaxValueInPropertyGroup(name,i));
		    prcGroupList.add(prc);
		}
		result.setMinPrc(min);
		result.setMaxPrc(max);
		result.setPrcGroupList(prcGroupList);
		// 브랜드 그룹핑
		String strBrandInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "BRAND_ID"),"");
		if(!strBrandInfo.equals("")){
			List<Brand> brandList = ResultUtils.getBrandGroup(strBrandInfo);
			result.setBrandList(brandList);
		}
		return result;
	}

	public Call<Info> getPropertyGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("SELLPRC");
			}
		};
	}
	

}
