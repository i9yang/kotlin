package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.CategoryGroupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.*;

public class ItemRecomCategoryCollection implements  Collection, Prefixable, CategoryGroupable{
	private final Logger logger = LoggerFactory.getLogger(ItemRecomCategoryCollection.class);
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
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		if(strSiteNo.equals("6002")||strSiteNo.equals("6003")){
			return result;
		}
		String ctgIdxNm  = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"DISP_CTG_IDX";
		// 상품수가 가장 많은 순으로 정렬을 위해 리스트에 담는다.
		List<Map<String,String>> ctgSortList = new ArrayList<Map<String,String>>();
		for(int i=0;i<search.w3GetCategoryCount(name, ctgIdxNm, 3);i++){
			// 필요한 카테고리 정보만 추출하고 Sorting 한다.
			// 6005^5500002768^등산/캠핑/낚시:6005^5500002804^낚시용품:6005^5500002805^낚시장비(대/릴/찌/바늘)
			Map<String,String> ctgSortMap = new HashMap<String,String>();
			ctgSortMap.put("CTG_INFO", search.w3GetCategoryName(name, ctgIdxNm, 3, i));
			ctgSortMap.put("CTG_COUNT", String.valueOf(search.w3GetDocumentCountInCategory(name, ctgIdxNm, 3, i)));
			ctgSortList.add(ctgSortMap);
		}
		// 정렬 실행
		Collections.sort(ctgSortList, new Comparator<Map<String, String>>() {
			@Override
			public int compare(Map<String, String> o1, Map<String, String> o2) {
				if(Integer.parseInt(o1.get("CTG_COUNT")) > Integer.parseInt(o2.get("CTG_COUNT")))return -1;
				else if(Integer.parseInt(o1.get("CTG_COUNT")) < Integer.parseInt(o2.get("CTG_COUNT")))return 1;
				return 0;
			}
		});

		// 정렬된 리스트에서 5개만 담아서 리턴한다.
		List<Map<String,String>> recomCtgList = new ArrayList<Map<String, String>>();
		for(int i=0;i<ctgSortList.size();i++){
			Map<String,String> ctgMap = new HashMap<String,String>();
			String token = ctgSortList.get(i).get("CTG_INFO");
			String count = ctgSortList.get(i).get("CTG_COUNT");
			String[] ctgs = token.split(":");
			if(ctgs!=null && ctgs.length>2){

				String[] dctg = (ctgs[0]).split("\\^");
				String[] mctg = (ctgs[1]).split("\\^");
				String[] sctg = (ctgs[2]).split("\\^");

				ctgMap.put("SctgId",	sctg[1]);
				ctgMap.put("SctgNm",	sctg[2]);
				ctgMap.put("SctgCount",	count);
				ctgMap.put("MctgId",	mctg[1]);
				ctgMap.put("MctgNm",	mctg[2]);
				ctgMap.put("DctgId",	dctg[1]);
				ctgMap.put("DctgNm",	dctg[2]);

				recomCtgList.add(ctgMap);

				if(i==4)break;
			}
		}
		result.setRecomCategoryList(recomCtgList);
		return result;
	}

	@Override
	public Call<Info> getCategoryGroup() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return CollectionUtils.getRecomCategoryGroupBy(parameter);
			}
		};
	}
}
