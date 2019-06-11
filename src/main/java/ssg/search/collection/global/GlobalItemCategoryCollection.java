package ssg.search.collection.global;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.collection.base.DispItemCollection;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.CategoryGroupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Category;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.*;

public class GlobalItemCategoryCollection extends DispItemCollection implements Collection, Prefixable, CategoryGroupable {
	
	public String getCollectionName(Parameter parameter) {
		return "item";
	}
	
	public String getCollectionAliasName(Parameter parameter){
		return "global";
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
					  Prefixes.SRCH_PREFIX_GLOBAL
					, Prefixes.FILTER_SITE_NO
					, Prefixes.TEM_DISP_CTG_ID
					, Prefixes.SALESTR_LST
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
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
				return CollectionUtils.getGlobalCategoryGroupBy(parameter);
			}
		};
	}

	public Call<Info> getSort() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				List<Sort> 	sortList 		= Lists.newArrayList();
				sortList.add(Sorts.RANK.getSort(parameter));
				return CollectionUtils.getGlobalCategoryGroupBy(parameter);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter,Result result) {
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String ctgIdxNm = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"TEM_DISP_CTG_IDX";
		String ctgId  = StringUtils.defaultIfEmpty(parameter.getCtgId(), "");
		String ctgIds  = StringUtils.defaultIfEmpty(parameter.getCtgIds(), "");
		String ctgLv  = StringUtils.defaultIfEmpty(parameter.getCtgLv(), "0");
		String ctgLast = StringUtils.defaultIfEmpty(parameter.getCtgLast(), "N");
        
        /* category */
		List<Category> categoryList = Lists.newArrayList();
		if(!ctgIds.equals("")){
			int  lv = 2;
            int totCnt = 0;
            try{
                lv = Integer.parseInt(ctgLv);
            }catch(NumberFormatException e){
                lv = 1;
            }
            
            int count = search.w3GetCategoryCount(name, ctgIdxNm, lv);
            String ctgStr ="";
            for(int k=0;k<count;k++){
            	Category category = new Category();
            	ctgStr = search.w3GetCategoryName(name, ctgIdxNm, lv, k);
                String[] ctgs = StringUtils.defaultIfEmpty(ctgStr, "").split("\\^");
                
                String[] ids = ctgIds.split("\\|");
                for (String id : ids) {
                    if(ctgStr.indexOf(id)>-1){
                        category = new Category();
                        category.setThemeYn("Y");
                        category.setCtgLevel(lv);
                        category.tokenizeCtg(ctgStr);
                        category.setCtgItemCount(search.w3GetDocumentCountInCategory(name,ctgIdxNm, lv, k));
                        category.setHasChild(true);
                        categoryList.add(category);
                        totCnt += search.w3GetDocumentCountInCategory(name,ctgIdxNm, lv, k);
                    }
                }
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
            // 1 Depth에서 선택검색하지 않은 경우에는 Depth를 보여준다.
            if(ctgStr!=null && !ctgStr.equals("")){
            	int k = 1;
            	if(ctgStr.indexOf(":")>-1){
                    for(StringTokenizer ctgSt = new StringTokenizer(ctgStr,":");ctgSt.hasMoreTokens();){
                        String tk = ctgSt.nextToken();
                        String[] splitStr = tk.split("\\^");
                        if(k<lv){
                            result.setCtgSiteNo(splitStr[0]);
                            result.setCtgId(k, splitStr[1]);
                            result.setCtgNm(k, splitStr[2]);
                            if(k==lv-1){
                                result.setCtgCount(lv, totCnt);
                            }
                            k++;
                        }
                    }
                }
            }
            result.setCategoryList(categoryList);
		}else{
			int viewLv = 1;
			if(ctgLv.equals("0")) viewLv = 2;
			else if(ctgLv.equals("1")) viewLv = 2;
			else if(ctgLv.equals("2")){
				if(ctgLast.equals("Y")) viewLv = 2;
                else viewLv = 3;
			}
			else if(ctgLv.equals("3")){
                if(ctgLast.equals("Y")) viewLv = 3;
                else viewLv = 4;
            }
			else if(ctgLv.equals("4")){
                if(ctgLast.equals("Y")) viewLv = 4;
                else viewLv = 5;
            }
			else viewLv = 1;
			
			/* 카테고리 SET */
            int count = search.w3GetCategoryCount(name, ctgIdxNm, viewLv);
            categoryList = new ArrayList<Category>();
            
            for(int k=0;k<count;k++){
                Category c = new Category();
                c.tokenizeCtg(search.w3GetCategoryName(name, ctgIdxNm, viewLv, k));
                c.setCtgItemCount(search.w3GetDocumentCountInCategory(name,ctgIdxNm, viewLv, k));
                c.setCtgLevel(viewLv);
                c.setThemeYn("Y");
                categoryList.add(c);
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
    		/* 카테고리 자식찾기 ( ctgLastYn이 N인 경우에는 다음 뎁스를 무조건 가져온다고 가정하고 ) */
            if(ctgLast.equals("N")){
                viewLv = viewLv + 1;
                count = search.w3GetCategoryCount(name, ctgIdxNm, viewLv);
                for(int j=0;j<categoryList.size();j++){
                    Category c = categoryList.get(j);
                    for(int k=0;k<count;k++){
                        String nm = search.w3GetCategoryName(name, ctgIdxNm, viewLv, k);
                        if(nm.indexOf(c.getCtgId())>-1){
                            c.setHasChild(true);
                            categoryList.set(j, c);
                            break;
                        }
                    }
                }
            }
            result.setCtgViewCount(categoryList.size());
			
            /* Result에 들어갈 카테고리( 경로정보 ) set */
            if(!ctgLv.equals("0") && !ctgId.equals("")){
            	int lv = 2;
                try{
                    lv = Integer.parseInt(ctgLv);
                }catch(NumberFormatException e){
                    lv = 1;
                }
                
                int count1 = search.w3GetCategoryCount(name, ctgIdxNm, lv);
                for(int k=0;k<count1;k++){
                	 String nm = search.w3GetCategoryName(name, ctgIdxNm, lv, k);
	                 int itemCount = search.w3GetDocumentCountInCategory(name,ctgIdxNm, lv, k);
	                 if(nm.indexOf(ctgId)>-1){
	                	 if(nm.indexOf(":")>-1){
	                		 int tkLv = 1;
	                            for(StringTokenizer resultToken = new StringTokenizer(nm,"\\:");resultToken.hasMoreTokens();){
	                                String[] strSplit = resultToken.nextToken().split("\\^");
	                                result.setCtgSiteNo(strSplit[0]);
	                                result.setCtgId(tkLv, strSplit[1]);
	                                result.setCtgNm(tkLv, strSplit[2]);
	                                tkLv++;
	                            }
	                	 }else{
	                		 	String[] strSplit = nm.split("\\^");
	                            result.setCtgSiteNo(strSplit[0]);
	                            result.setCtgId(lv, strSplit[1]);
	                            result.setCtgNm(lv, strSplit[2]);
	                	 }
	                	 result.setCtgCount(lv,itemCount);
	                 }
                }
            }
            result.setCategoryList(categoryList);
		}
		
		return result;
	}

}
