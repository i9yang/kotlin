package ssg.search.collection.recom;

import QueryAPI510.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 131544 on 2016-11-03.
 */
public class CategoryRecomCollection implements Collection, Prefixable, Pageable{
	private Logger logger = LoggerFactory.getLogger(CategoryRecomCollection.class);

	@Override
	public String getCollectionName(Parameter parameter) {
		return "categoryrecom";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "categoryrecom";
	}

	@Override
	public String[] getDocumentField(Parameter parameter) {
		return new String[]{"DISP_CTG_LCLS_IDS", "DISP_CTG_LCLS_NMS", "DISP_CTG_MCLS_IDS", "DISP_CTG_MCLS_NMS", "DISP_CTG_SCLS_IDS", "DISP_CTG_SCLS_NMS"};
	}

	@Override
	public String[] getSearchField(Parameter parameter) {
		return new String[]{"QUERY"};
	}

	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		List<Map<String,String>> recomDispCtgCategoryList = new ArrayList<Map<String, String>>();
		if(search.w3GetResultTotalCount(name)>0){
			Map<String,String> ctgMap = new HashMap<String,String>();
			String dispCtgLclsIds = search.w3GetField(name, "DISP_CTG_LCLS_IDS", 0);
			String dispCtgLclsNms = search.w3GetField(name, "DISP_CTG_LCLS_NMS", 0);
			String dispCtgMclsIds = search.w3GetField(name, "DISP_CTG_MCLS_IDS", 0);
			String dispCtgMclsNms = search.w3GetField(name, "DISP_CTG_MCLS_NMS", 0);
			String dispCtgSclsIds = search.w3GetField(name, "DISP_CTG_SCLS_IDS", 0);
			String dispCtgSclsNms = search.w3GetField(name, "DISP_CTG_SCLS_NMS", 0);
			// 여러개 존재 ( 2개까지 Set )
			if(dispCtgLclsIds.indexOf(",")>-1){
				String[] dispCtgLclsId = dispCtgLclsIds.split(",");
				String[] dispCtgLclsNm = dispCtgLclsNms.split(",");
				String[] dispCtgMclsId = dispCtgMclsIds.split(",");
				String[] dispCtgMclsNm = dispCtgMclsNms.split(",");
				String[] dispCtgSclsId = dispCtgSclsIds.split(",");
				String[] dispCtgSclsNm = dispCtgSclsNms.split(",");
				for(int i=0;i<2;i++){
					ctgMap = new HashMap<String,String>();
					ctgMap.put("DctgId", dispCtgLclsId[i]);
					ctgMap.put("DctgNm", dispCtgLclsNm[i]);
					ctgMap.put("MctgId", dispCtgMclsId[i]);
					ctgMap.put("MctgNm", dispCtgMclsNm[i]);
					ctgMap.put("SctgId", dispCtgSclsId[i]);
					ctgMap.put("SctgNm", dispCtgSclsNm[i]);
					recomDispCtgCategoryList.add(ctgMap);
				}
			}
			// 한개만 있는 경우
			else{
				ctgMap.put("DctgId", dispCtgLclsIds);
				ctgMap.put("DctgNm", dispCtgLclsNms);
				ctgMap.put("MctgId", dispCtgMclsIds);
				ctgMap.put("MctgNm", dispCtgMclsNms);
				ctgMap.put("SctgId", dispCtgSclsIds);
				ctgMap.put("SctgNm", dispCtgSclsNms);
				recomDispCtgCategoryList.add(ctgMap);
			}
		}
		if(recomDispCtgCategoryList!=null && recomDispCtgCategoryList.size()>0){
			result.setRecomDispCategoryList(recomDispCtgCategoryList);
		}
		return result;
	}

	@Override
	public Call<Info> getPrefix() {
		return new Call<Info>() {
			@Override
			public Info apply(Parameter parameter) {
				return new Info(Prefixes.SITE_NO_ONLY.getPrefix(parameter), 1);
			}
		};
	}

	@Override
	public Call<Info> getPage() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return new Info(0, 1);
			}
		};
	}
}
