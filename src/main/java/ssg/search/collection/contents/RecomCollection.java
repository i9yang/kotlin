package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Recom;
import ssg.search.result.Result;

import java.util.Iterator;
import java.util.List;

public class RecomCollection implements Collection, Pageable, Prefixable{

	public String getCollectionName(Parameter parameter){
		return "rsearch";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "rsearch";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO",
			"ITEM_ID",
			"ITEM_NM",
			"DISP_ORDR"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{"CRITN_SRCHWD_NM"};
	}
	
	public Call<Info> getPage(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info(0, 12);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		StringBuilder srchItemIds = new StringBuilder();
		List<Recom> recomList = Lists.newArrayList();
		int count = search.w3GetResultCount(name);
		Recom recom;
		for(int i=0;i<count;i++){
			recom = new Recom();
        	String strItemId = search.w3GetField(name,"ITEM_ID",i);
        	String strItemNm = search.w3GetField(name,"ITEM_NM",i);
        	String strSiteNo = search.w3GetField(name,"SITE_NO",i);

        	recom.setItemId(strItemId);
        	recom.setItemNm(strItemNm);
        	recom.setSiteNo(strSiteNo);

        	StringBuilder ids = new StringBuilder().append(strItemId).append(",");
            if(srchItemIds.indexOf(ids.toString()) < 0){
            	srchItemIds.append(ids);
            }
            
            recomList.add(recom);
		}
		result.setRecomList(recomList);
		result.setRecomCount(search.w3GetResultTotalCount(name));
		result.setRecomItemIds(srchItemIds.toString());//itemId만 가지고 select 하기 위함
		return result;
	}
	
	public Call<Info> getPrefix() {
		return new Call<Info>() {
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					    Prefixes.SITE_NO_ONLY
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}
}
