package ssg.search.collection.es.rsearch;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.core.SearchResult;
import ssg.search.parameter.Parameter;
import ssg.search.result.Banr;
import ssg.search.result.Result;

import java.util.List;

public class EsBanrCollection extends RecommendCollection {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "banr";
	}
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESBanr";
	}
	
	@Override
	public String getQuery(Parameter parameter) {
		JsonObject queries = new JsonObject();
		JsonArray sourceArray = new JsonArray();
		
		//get할 필드 지정
		sourceArray.add("SRCH_CRITN_ID");
		sourceArray.add("SHRTC_DIV_CD");
		sourceArray.add("IMG_FILE_NM");
		sourceArray.add("BANR_RPLC_TEXT_NM");
		sourceArray.add("LINK_URL");
		sourceArray.add("LIQUOR_YN");
		sourceArray.add("POP_YN");
		sourceArray.add("SITE_NO");
		sourceArray.add("SHRTC_TGT_TYPE_CD");
		
		//query문
		queries.add("query", queryString(parameter));
		
		//정렬
		queries.add("sortVo", sortString());
		
		//사이즈
		queries.addProperty("from", 0);
		queries.addProperty("size", 2);
		
		queries.add("_source", sourceArray);
		
		return new Gson().toJson(queries);
	}
	
	private JsonObject queryString(Parameter parameter){
		JsonArray termArray = new JsonArray();

		termArray.add(this.put("match", this.put("CRITN_SRCHWD_NM", parameter.getQuery().replaceAll(" ",""))));
		termArray.add(this.put("match", this.put("SITE_NO", parameter.getSiteNo())));
		termArray.add(this.put("match", this.put("SHRTC_TGT_TYPE_CD", parameter.getAplTgtMediaCd())));
		
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
		List<SearchResult.Hit<Banr, Void>> list = searchResult.getHits(Banr.class);
		List<Banr> banrList = Lists.newArrayList();
		// Rule Set
		for (SearchResult.Hit h : list) {
			String srchCritnId = ((Banr)h.source).getSrchCritnId();
			String shrtcDivCd = ((Banr)h.source).getShrtcDivCd();
			String imgFileNm = ((Banr)h.source).getImgFileNm();
			String banrRplcTextNm = ((Banr)h.source).getBanrRplcTextNm();
			String linkUrl = ((Banr)h.source).getLinkUrl();
			String liquorYn = ((Banr)h.source).getLiquorYn();
			String popYn = ((Banr)h.source).getPopYn();
			String siteNo = ((Banr)h.source).getSiteNo();
			String shrtcTgtTypeCd = ((Banr)h.source).getShrtcTgtTypeCd();
			
			//링크뒤에 자동으로 쿼리 넘김({}있을때만 처리되도록)
			if(linkUrl.indexOf("query={}") >-1){
				linkUrl = linkUrl.substring(0,linkUrl.indexOf("{}")) + parameter.getQuery();
			}
			
			Banr b = new Banr();
			
			b.setSrchCritnId(srchCritnId);
			b.setSiteNo(siteNo);
			b.setShrtcTgtTypeCd(shrtcTgtTypeCd);
			b.setShrtcDivCd(shrtcDivCd);
			b.setImgFileNm(imgFileNm);
			b.setBanrRplcTextNm(banrRplcTextNm);
			b.setLinkUrl(linkUrl);
			b.setLiquorYn(liquorYn);
			b.setPopYn(popYn);
			
			banrList.add(b);
		}
		result.setBanrList(banrList);
		result.setBanrCount(banrList.size());
		result.setBanrGcount(banrList.size());
		return result;
	}
}
