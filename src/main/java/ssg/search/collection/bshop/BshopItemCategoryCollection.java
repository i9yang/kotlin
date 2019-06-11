package ssg.search.collection.bshop;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.CategoryGroupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Category;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BshopItemCategoryCollection  implements Collection, Prefixable, CategoryGroupable {
	
	public String getCollectionName(Parameter parameter) {
		return "item";
	}
	
	public String getCollectionAliasName(Parameter parameter){
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

	public Call<Info> getPrefix(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_BSHOP
					, Prefixes.FILTER_SITE_NO
					, Prefixes.SRCH_CTG_PREFIX
					, Prefixes.SALESTR_LST
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.BSHOP_ID
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}
	
	public Call<Info> getCategoryGroup() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return CollectionUtils.getCategoryGroupBy(parameter);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter,Result result) {
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String ctgIdxNm = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"DISP_CTG_IDX";
		String strCtgId		  = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
		
		int count = search.w3GetCategoryCount(name, ctgIdxNm, 1);
		List<Category> categoryList = Lists.newArrayList();
		
		// Category Result Set
		for(int k=0;k<count;k++){
			Category category = new Category();
            String[] ctgs = StringUtils.defaultIfEmpty(search.w3GetCategoryName(name, ctgIdxNm, 1, k), "").split("\\^");
            if(ctgs.length == 3){
            	category.setSiteNo(ctgs[0]);
                category.setCtgId(ctgs[1]);
                category.setCtgNm(ctgs[2]);
                if(strCtgId.equals(ctgs[1])) category.setSelectedArea("selected");
            }else{
            	category.setSiteNo("");
            	category.setCtgId("");
            	category.setCtgNm("");
            }
            category.setCtgItemCount(search.w3GetDocumentCountInCategory(name,ctgIdxNm, 1, k));
            categoryList.add(category);
        }
		// Category Result Sort ( 상품 많은 순 )
		if(categoryList!=null && categoryList.size()>0){
			Collections.sort(categoryList, new Comparator <Category>(){
				public int compare(Category c1,Category c2){
	                if(c1.getCtgItemCount()>c2.getCtgItemCount()){
	                    return -1;
	                } else if(c1.getCtgItemCount()<c2.getCtgItemCount()){
	                    return 1;
	                } else{
	                    return 0;
	                }
	            }
	        });
			
		}
		result.setCategoryList(categoryList);
		return result;
	}

}
