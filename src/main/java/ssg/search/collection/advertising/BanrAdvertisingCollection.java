package ssg.search.collection.advertising;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ssg.search.parameter.Parameter;
import ssg.search.result.BanrAdvertising;
import ssg.search.result.Result;

import java.util.List;

public class BanrAdvertisingCollection extends AdvertisingCollection {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "adsrchbanr";
	}
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "adsrchbanr";
	}
	
	@Override
	public String getUrlPath(Parameter parameter) {
		return "/addpapi/dp/daDispCall.ssg";
	}
	
	@Override
	public  List<NameValuePair> getQuery(Parameter parameter) {
	    List<NameValuePair> params = Lists.newArrayList();
	    
	    params.add(new BasicNameValuePair("advertKindCd", "50"));
	    
	    /*
	     * 광고쪽요청으로 하드코딩 advertAcctGrpId 여기만 사용하므로 하드코딩, 다른곳 사용시 enum으로 빼야함
	     * 10000053: 이마트몰
         * 10000054: SSG.COM
         * 10000055: 신세계몰
         * 10000056: 신세계백화점
	     */
	    String advertAcctGrpId = "";
	    if(StringUtils.equalsIgnoreCase("6001", parameter.getSiteNo())){
	    	advertAcctGrpId = "10000053";
	    }else if(StringUtils.equalsIgnoreCase("6005", parameter.getSiteNo())){
	    	advertAcctGrpId = "10000054";
	    }else if(StringUtils.equalsIgnoreCase("6004", parameter.getSiteNo())){
	    	advertAcctGrpId = "10000055";
	    }else if(StringUtils.equalsIgnoreCase("6009", parameter.getSiteNo())){
	    	advertAcctGrpId = "10000056";
	    }
	    
	    params.add(new BasicNameValuePair("advertAcctGrpId", advertAcctGrpId));
	    
	    params.add(new BasicNameValuePair("advertQuery", parameter.getQuery()));
	    params.add(new BasicNameValuePair("siteNo", parameter.getSiteNo()));
	    params.add(new BasicNameValuePair("deviceCd", parameter.getAplTgtMediaCd()));
		
	    return params;
	}
	
	@Override
	public Result getResult(String searchResult, String name, Parameter parameter, Result result) {
		
		List<BanrAdvertising> advertisingList = Lists.newArrayList();
		int advertBanrCnt = 0;

		// 임시 주석처리 - 140880
//		JSONObject json = JSONObject.fromObject(searchResult);
//		String resCode = json.getString("res_code");
//
//		if ("200".equals(resCode) && json.getInt("advertDispTgtCnt")> 0) {
//			advertBanrCnt = json.getInt("advertDispTgtCnt");
//			JSONArray arr = json.getJSONArray("dispTgtAccumDtoList");
//
//			for(int i=0; i<arr.size(); i++){
//				BanrAdvertising banrAdvertising = new BanrAdvertising();
//				JSONObject o = arr.getJSONObject(i);
//
//				banrAdvertising.setAdvertAcctId(o.getString("advertAcctId"));
//				banrAdvertising.setAdvertBidId(o.getString("advertBidId"));
//				banrAdvertising.setAdvertBilngTypeCd(o.getString("advertBilngTypeCd"));
//				banrAdvertising.setAdvertKindCd(o.getString("advertKindCd"));
//				banrAdvertising.setQuery(o.getString("query"));
//				banrAdvertising.setSiteNo(o.getString("siteNo"));
//				banrAdvertising.setLinkUrl(o.getString("linkUrl"));
//				banrAdvertising.setImgFileNm(o.getString("imgFileNm"));
//				banrAdvertising.setBanrRplcTextNm(o.getString("banrRplcTextNm"));
//				banrAdvertising.setPopYn(o.getString("popYn"));
//				banrAdvertising.setAdvertExtensTeryDivCd("");
//
//				advertisingList.add(banrAdvertising);
//			}
//		} else if (!"200".equals(resCode)) {
//			MonitorInformation.logging(Level.ERROR, "DP001_SMS_GROUP001 AdvertisingQueryBuilder result fail message {} : " + json.getString("res_message"));
//  	}
		
		result.setBanrAdvertisingCount(advertBanrCnt);
		result.setBanrAdvertisingList(advertisingList);
		
		return result;
	}
}
