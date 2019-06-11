package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.CategoryGroupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.MobileCategory;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.*;

public class ItemCommCategoryCollection implements  Collection, Prefixable, CategoryGroupable{
	
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
					, Prefixes.SRCH_CTG_ITEM_PREFIX
					, Prefixes.SALESTR_LST_GROUP
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SRCH_PSBL_YN
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
				if(parameter.getTarget().equalsIgnoreCase("chat_ven_items")){
					return CollectionUtils.getMobileCategoryGroupBy(parameter, 2);
				}
				return CollectionUtils.getMobileCategoryGroupBy(parameter, 4);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		// result 에서 추천 카테고리 리스트를 가져온다.
		List<Map<String,String>> recomList = result.getRecomDispCategoryList();
		Set<String> recomLctgIdSet = Sets.newHashSet();
		Set<String> recomMctgIdSet = Sets.newHashSet();
		Set<String> recomSctgIdSet = Sets.newHashSet();
		if(recomList!=null && recomList.size()>0){
			for(Map<String,String> rMap : recomList){
				recomLctgIdSet.add(rMap.get("DctgId"));
				recomMctgIdSet.add(rMap.get("MctgId"));
				recomSctgIdSet.add(rMap.get("SctgId"));
			}
		}
		
		String idx = "DISP_CTG_IDX";
		if(StringUtils.defaultIfEmpty(parameter.getSiteNo(), "").equals("6005")) idx = "SCOM_DISP_CTG_IDX";
		int maxLevel = 4;
		if(parameter.getTarget().equalsIgnoreCase("chat_ven_items")) maxLevel = 2;
		
		List<MobileCategory> cList = new ArrayList<MobileCategory>();
		Map<String, List<MobileCategory>> categoryMap = Maps.newHashMap();
		Set<String> existLctgIdSet = Sets.newHashSet();
		Set<String> existMctgIdSet = Sets.newHashSet();
		Set<String> existSctgIdSet = Sets.newHashSet();
		for(int level=1;level<=maxLevel;level++){
			List<MobileCategory> categoryList = Lists.newArrayList();
			
			//1레벨부터 전체 카테고리 돌린다.(부모/자식 상관없이 일단 모든 카테고리 다 넣는다)
			for(int i=0;i<search.w3GetCategoryCount(name, idx, level);i++){
				
				//1레벨은 1레벨만   ( 6001^0006510000^생활용품/세제/제지 )
				//2레벨부터는 자기자신 포함 전 레벨을 다 가져옴 ( 6001^0006510000^생활용품/세제/제지:6001^0006510003^세탁/주방/생활 세제 )
				String s = search.w3GetCategoryName(name, idx, level, i);
				String[] t = s.split(":");
				String[] categoryToken = t[level-1].split("\\^");
				
				MobileCategory category = new MobileCategory();
				category.setSiteNo(categoryToken[0]);
				category.setDispCtgId(categoryToken[1]);
				category.setDispCtgNm(categoryToken[2]);
				category.setItemCount(String.valueOf(search.w3GetDocumentCountInCategory(name, idx, level, i)));
				category.setDispCtgLvl(String.valueOf(level));
				category.setHasChild("false");
				if(level>1)category.setPriorDispCtgId(t[level-2].split("\\^")[1]);
				else category.setPriorDispCtgId("");
				categoryList.add(category);
				
				//추천카테고리에 존재하는 카테고리를 추려내기 위함
				if(level==1 && recomLctgIdSet!=null && recomLctgIdSet.contains(categoryToken[1])){
					if(!existLctgIdSet.contains(categoryToken[1])) existLctgIdSet.add(categoryToken[1]);
				}
				if(level==2 && recomMctgIdSet!=null && recomMctgIdSet.contains(categoryToken[1])){
					if(!existMctgIdSet.contains(categoryToken[1])) existMctgIdSet.add(categoryToken[1]);
				}
				if(level==3 && recomSctgIdSet!=null && recomSctgIdSet.contains(categoryToken[1])){
					if(!existSctgIdSet.contains(categoryToken[1])) existSctgIdSet.add(categoryToken[1]);
				}
			}
			
			// CATEGORY SORT(상품 많은순)
			if(categoryList!=null && categoryList.size()>0){
				Collections.sort(categoryList, new Comparator <MobileCategory>(){
					public int compare(MobileCategory c1,MobileCategory c2){
		                if(Integer.parseInt(c1.getItemCount())> Integer.parseInt(c2.getItemCount())){
		                    return -1;
		                } else if(Integer.parseInt(c1.getItemCount())<Integer.parseInt(c2.getItemCount())){
		                    return 1;
		                } else{
		                    return 0;
		                }
		            }
		        });
			}
			categoryMap.put(String.valueOf(level), categoryList);
		}

		// Set 한 데이터의 참조를 가지고 부모/자식관계를 생성한다.
		for(int level=maxLevel;level>1;level--){
			for(MobileCategory prior : categoryMap.get(String.valueOf(level-1))){
				for(MobileCategory target : categoryMap.get(String.valueOf(level))){
					if(target.getPriorDispCtgId().equals(prior.getDispCtgId())){
						prior.setHasChild("true");
						prior.add(target);
					}
				}
			}
		}
		cList = categoryMap.get("1");
		result.setCommCategoryList(cList);
		
		//추천카테고리 정리(존재하지 않는 카테고리 삭제처리)
		if(recomList!=null && recomList.size()>0){
		Iterator<Map<String,String>> iter = recomList.iterator();
			while (iter.hasNext()) {
				Map<String,String> recomMap = iter.next();
				String dispLctgId = recomMap.get("DctgId");
				String dispMcCtgId = recomMap.get("MctgId");
				String dispScCtgId = recomMap.get("SctgId");
				if(!existLctgIdSet.contains(dispLctgId) || !existMctgIdSet.contains(dispMcCtgId) || !existSctgIdSet.contains(dispScCtgId)) {
					iter.remove();
				}
			}
			result.setRecomDispCategoryList(recomList);
		}
		
		return result;
	}
}
