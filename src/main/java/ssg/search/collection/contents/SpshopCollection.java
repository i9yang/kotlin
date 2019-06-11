package ssg.search.collection.contents;

import java.util.List;

import com.google.common.collect.Lists;

import QueryAPI510.Search;
import ssg.search.base.Collection;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.SpShop;

public class SpshopCollection implements Collection{

	public String getCollectionName(Parameter parameter){
		return "spshop";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "spshop";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO",
			"ITEM_ID",
			"ITEM_NM",
			"SELLPRC"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"ITEM_ID",
			"ITEM_NM",
			"ITEM_SRCHWD_NM",
			"MDL_NM",
			"BRAND_NM",
			"GNRL_STD_DISP_CTG"
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<SpShop> spShopllList = Lists.newArrayList();
		SpShop spShop;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			spShop = new SpShop();
			spShop.setSiteNo(search.w3GetField(name, "SITE_NO", i));
			spShop.setItemId(search.w3GetField(name, "ITEM_ID", i));
			spShop.setItemNm(search.w3GetField(name, "ITEM_NM", i));
			spShop.setSellprc(search.w3GetField(name, "SELLPRC", i));
			spShopllList.add(spShop);
		}
		result.setSpShopList(spShopllList);
		result.setSpShopCount(search.w3GetResultTotalCount(name));
		return result;
	}

}
