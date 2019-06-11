package ssg.search.collection.es.rsearch;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import io.searchbox.core.SearchResult;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.Spell;

public class EsSpellCollection extends RecommendCollection {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "spell";
	}
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESSpell";
	}
	
	@Override
	public String getQuery(Parameter parameter) {
		JsonObject queries = new JsonObject();
		JsonArray sourceArray = new JsonArray();
		
		//get할 필드 지정
		sourceArray.add("CRITN_SRCHWD_NM");
		sourceArray.add("RPLC_KEYWD_NM");
		
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

		termArray.add(this.put("match", this.put("CRITN_SRCHWD_NM", parameter.getQuery())));
		termArray.add(this.put("match", this.put("SITE_NO", parameter.getSiteNo())));
		
		return this.put("bool", this.put("must", termArray));
	}
	
	private JsonArray sortString(){
		JsonArray sortArray = new JsonArray();
		sortArray.add(this.put("_id", "asc"));
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
		List<SearchResult.Hit<Spell, Void>> list = searchResult.getHits(Spell.class);
		List<Spell> spellList = Lists.newArrayList();
		
		// Rule Set
		for (SearchResult.Hit h : list) {
			String critnSrchwdNm = ((Spell)h.source).getCritnSrchwdNm();
			String rplcKeywdNm = ((Spell)h.source).getRplcKeywdNm();

			Spell s = new Spell();
			s.setCritnSrchwdNm(critnSrchwdNm);
			s.setRplcKeywdNm(rplcKeywdNm);
			
			spellList.add(s);
		}
		result.setSpellList(spellList);
		result.setSpellCount(spellList.size());
		return result;
	}
}
