package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.Boostable;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.Spprice;
import ssg.search.util.CollectionUtils;

import java.util.List;

public class SppriceCollection implements Collection, Pageable, Prefixable, Boostable, Sortable{

	public String getCollectionName(Parameter parameter){
		return "spprice";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "spprice";
	}
	
	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO",
			"ITEM_ID",
			"ITEM_NM",
			"BRAND_NM",
			"SP_TYPE",
			"SSG_DISP_YN"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
				"ITEM_ID",
				"ITEM_NM",
				"ITEM_SRCHWD_NM",
				"BRAND_NM"
		};
	}
	
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info(0, 40);
			}
		};
	}
	
	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder prefix = new StringBuilder();
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
				//SpType :10-오반장, 20-해바
				//SSG에서는 모두 노출 
				if(strSiteNo.equals("6005")){
					prefix.append("<SITE_NO:contains:6001|6002|6003|6004|6009>");
					prefix.append("<SSG_DISP_YN:contains:Y>");
				}else if(strSiteNo.equals("6004")){
					prefix.append("<SITE_NO:contains:6004|6009>");
					prefix.append("<SP_TYPE:contains:20|30>"); 
				}else if(strSiteNo.equals("6001")){
					prefix.append("<SITE_NO:contains:6001|6002>");
					prefix.append("<SP_TYPE:contains:10|30>"); 
				}else if(strSiteNo.equals("6002")){      //@ BOOT개발시확인 
					prefix.append("<SITE_NO:contains:").append(strSiteNo).append(">");
					prefix.append("<SP_TYPE:contains:10|30>"); 
				}else if(strSiteNo.equals("6009")){
					prefix.append("<SITE_NO:contains:").append(strSiteNo).append(">");
					prefix.append("<SP_TYPE:contains:20|30>"); 
				}
				prefix
					.append(Prefixes.DEVICE_CD.getPrefix(parameter))
					.append(Prefixes.MBR_CO_TYPE.getPrefix(parameter))
				;
				
				return new Info(prefix.toString(),1);
			}
			
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<Spprice> spList = Lists.newArrayList();
		Spprice spprice;
		int count = search.w3GetResultCount(name);
		StringBuilder srchItemIds = new StringBuilder();
		
		for(int i=0;i<count;i++){
			spprice = new Spprice();
			spprice.setSiteNo(search.w3GetField(name, "SITE_NO", i));
			spprice.setItemId(search.w3GetField(name, "ITEM_ID", i));
			spprice.setItemNm(search.w3GetField(name, "ITEM_NM", i));
			spprice.setSpType(search.w3GetField(name, "SP_TYPE", i));
			StringBuilder ids = new StringBuilder().append(spprice.getSiteNo()).append(":").append(spprice.getItemId()).append(",");
			if(srchItemIds.indexOf(ids.toString()) < 0){
				if(!spprice.getSpType().equals("30")){
					srchItemIds.append(ids);
					spList.add(spprice);
				}
        	}
		}
		
		//딜상품 추가하기 위해 한번 더 돌림(해바 +딜상품, 오반장 +딜상품 일 경우 해바, 오반장만 우선으로 보여줌)
		for(int i=0;i<count;i++){
			spprice = new Spprice();
			spprice.setSiteNo(search.w3GetField(name, "SITE_NO", i));
			spprice.setItemId(search.w3GetField(name, "ITEM_ID", i));
			spprice.setItemNm(search.w3GetField(name, "ITEM_NM", i));
			spprice.setSpType(search.w3GetField(name, "SP_TYPE", i));
			StringBuilder ids = new StringBuilder().append(spprice.getSiteNo()).append(":").append(spprice.getItemId()).append(",");
			if(srchItemIds.indexOf(ids.toString()) < 0){
				if(spprice.getSpType().equals("30")){
					srchItemIds.append(ids);
					spList.add(spprice);
				}
			}
		}
		
		result.setSppriceList(spList);
		result.setSppriceItemIds(srchItemIds.toString()); //가이드와 동일한 형식(itemId만 가지고 select)
		result.setSppriceCount(spList.size());
		return result;
	}


	@Override
	public Call<Info> getBoost(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return CollectionUtils.getCategoryBoosting(parameter);
			}
		};
	}

	@Override
	public Call<Info> getSort() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				List<Sort> 	sortList 		= Lists.newArrayList();
				sortList.add(Sorts.WEIGHT.getSort(parameter));
				sortList.add(Sorts.RANK.getSort(parameter));
				return new Info(sortList);
			}
		};
	}
}