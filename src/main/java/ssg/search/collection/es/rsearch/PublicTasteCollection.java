package ssg.search.collection.es.rsearch;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.searchbox.core.SearchResult;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.Taste;

public class PublicTasteCollection extends RecommendCollection {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "public";
	}
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "public";
	}
	@Override
	public String getQuery(Parameter parameter) {
		List<String> returnList = Lists.newArrayList();
		JsonObject queries = new JsonObject();
		JsonArray sourceArray = new JsonArray();
		sourceArray.add("ITEM_ID");
		sourceArray.add("TASTE_TYPE");
		sourceArray.add("ORD_CONT");
		queries.add("query", queryString(parameter.getQuery()));
		queries.add("sortVo", sortString());
		queries.addProperty("from", 0);
		queries.addProperty("size", 100);
		queries.add("_source", sourceArray);
		return new Gson().toJson(queries);
	}
	private JsonObject queryString(String query){
		JsonObject bool = new JsonObject();
		return this.put("bool", this.put("filterVo", this.put("match", this.put("QUERY", query))));
	}
	private JsonArray sortString(){
		String[] sorts = {"CLICK_CNT:desc", "ORD_CONT:desc", "ORD_QTY:desc"};
		JsonArray sortArray = new JsonArray();
		for(String sort : sorts){
			JsonObject sortObject = new JsonObject();
			if(sort.indexOf(":")>-1){
				String[] splitSort = sort.split(":");
				sortObject.addProperty(splitSort[0], splitSort[1]);
			}else{
				sortObject.addProperty(sort, "asc");
			}
			sortArray.add(sortObject);
		}
		return sortArray;
	}
	private JsonObject put(String targetName, Object target){
		JsonObject object = new JsonObject();
		if(target instanceof JsonObject){
			object.add(targetName, (JsonObject)target);
		}else if(target instanceof String){
			object.addProperty(targetName, (String)target);
		}
		return object;
	}
	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		List<SearchResult.Hit<Taste, Void>> list = searchResult.getHits(Taste.class);
		List<Taste> tasteList = Lists.newArrayList();
		StringBuilder srchItemIds = new StringBuilder();
		// Rule Set
		for (SearchResult.Hit h : list) {
			String itemId = ((Taste)h.source).getItemId();
			String tasteType = ((Taste)h.source).getTasteType();
			String ordCont = StringUtils.defaultIfEmpty(((Taste)h.source).getOrdCont(), "0");

			
			Taste t = new Taste();
			t.setItemId(itemId);
			t.setTaste("public");
			t.setTasteType(tasteType);
			srchItemIds.append(itemId).append(":").append("public:").append(tasteType).append(":").append(ordCont).append(",");
			tasteList.add(t);
		}
		result.setPublicTasteIds(srchItemIds.toString());
		result.setPublicTasteList(tasteList);
		return result;
	}
}
