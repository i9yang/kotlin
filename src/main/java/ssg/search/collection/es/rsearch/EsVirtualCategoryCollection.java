package ssg.search.collection.es.rsearch;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.searchbox.core.SearchResult;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.VirtualCategory;

public class EsVirtualCategoryCollection extends RecommendCollection {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "virtualcategory";
	}
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESVirtualcategory";
	}
	
	@Override
	public String getQuery(Parameter parameter) {
		JsonObject queries = new JsonObject();
		JsonArray sourceArray = new JsonArray();
		
		//get할 필드 지정
		sourceArray.add("VIRTUAL_CTG_ID");
		sourceArray.add("VIRTUAL_CTG_NM");
		
		//query문
		queries.add("query", queryString(parameter));
		
		//정렬
		queries.add("sortVo", sortString());
		
		//사이즈
		queries.addProperty("from", 0);
		queries.addProperty("size", 1);
		
		queries.add("_source", sourceArray);
		
		return new Gson().toJson(queries);
	}
	
	private JsonObject queryString(Parameter parameter){
		JsonArray termArray = new JsonArray();

		termArray.add(this.put("match", this.put("QUERY", parameter.getQuery())));
		
		return this.put("bool", this.put("must", termArray));
	}
	
	private JsonArray sortString(){
		JsonArray sortArray = new JsonArray();
		sortArray.add(this.put("CNT", "desc"));
		return sortArray;
	}
	
	private JsonObject put(String targetName, Object target){
		JsonObject object = new JsonObject();
		if(target instanceof JsonObject){
			object.add(targetName, (JsonObject)target);
		}else if(target instanceof String){
			object.addProperty(targetName, (String)target);
		}else if(target instanceof JsonArray){
			object.add(targetName, (JsonArray)target);
		}
		return object;
	}
	
	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		List<SearchResult.Hit<VirtualCategory, Void>> list = searchResult.getHits(VirtualCategory.class);
		List<VirtualCategory> vitualList = Lists.newArrayList();
		Map<String,String> virtualCategoryMap = Maps.newHashMap();
		
		// Rule Set
		for (SearchResult.Hit h : list) {
			String virtualCtgId = ((VirtualCategory)h.source).getVirtualCtgId();
			String virtualCtgNm = ((VirtualCategory)h.source).getVirtualCtgNm();

			virtualCategoryMap.put("VIRTUAL_CTG_ID", virtualCtgId);
			virtualCategoryMap.put("VIRTUAL_CTG_NM", virtualCtgNm);
			
			result.setVirtualCtgId(virtualCtgId);
			result.setVirtualCategoryMap(virtualCategoryMap);
			
			result.setVirtualCategoryMap(virtualCategoryMap);
		}
		return result;
	}
}
