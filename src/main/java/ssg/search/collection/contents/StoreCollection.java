package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.Store;

import java.util.*;

public class StoreCollection implements Collection, Pageable, Prefixable {

	public String getCollectionName(Parameter parameter){
		return "store";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "store";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"APL_TGT_MEDIA_CD",
			"STORE_NM",
			"IMG_FILE_NM",
			"LINK_URL",
			"BG_VAL",
			"CATEGORY_LST",
			"SITE_LST",
			"RPLC_SRCHWD_NM"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{"STORE_NM","RPLC_SRCHWD_NM"};
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
				String aplTgtMediaCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
				
				prefix.append("<SITE_LST:contains:").append(strSiteNo).append(">");
				prefix.append("<APL_TGT_MEDIA_CD:contains:00|").append(aplTgtMediaCd).append(">");
				
	            return new Info(prefix.toString(),1);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<Store> storeList = Lists.newArrayList();
		Store store;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			store = new Store();
			store.setStoreNm(search.w3GetField(name, "STORE_NM", i));
			store.setLinkUrl(search.w3GetField(name, "LINK_URL", i));
			store.setBgVal(search.w3GetField(name, "BG_VAL", i));
			store.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
			List<Map<String,String>> cateList= new ArrayList<Map<String,String>>();
			String ctgLst = search.w3GetField(name, "CATEGORY_LST", i);
			for(Iterator<String> iter = Splitter.on(",").trimResults().split(ctgLst).iterator();iter.hasNext();){
				String ctgElement = iter.next();
				if(ctgElement!=null && !ctgElement.equals("")){
					int j=0;
					Map<String,String> cateMap = new HashMap<String,String>();
					for(Iterator<String> ctgToken = Splitter.on("^").trimResults().split(ctgElement).iterator();ctgToken.hasNext();){
						String ctg = ctgToken.next();
						if(ctg!=null && !ctg.equals("")){
							if( j == 1 ){
								cateMap.put("cateNm",ctg);
							}else if(j == 2 ){
								cateMap.put("cateUrl",ctg);
							}
						}
						j++;
					}
					cateList.add(cateMap);
				}
			}
			store.setCategoryList(cateList);
			storeList.add(store);
		}
		result.setStoreList(storeList);
		result.setStoreCount(search.w3GetResultTotalCount(name));
		return result;
	}
}
