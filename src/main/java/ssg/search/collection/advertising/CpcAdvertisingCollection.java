package ssg.search.collection.advertising;

import com.google.common.collect.Lists;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ssg.search.parameter.Parameter;
import ssg.search.result.Advertising;
import ssg.search.result.Result;

import java.util.List;

public class CpcAdvertisingCollection extends AdvertisingCollection {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "adcpc";
	}
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "adcpc";
	}
	
	@Override
	public String getUrlPath(Parameter parameter) {
		return "/addpapi/dp/cpcDispCall.ssg";
	}
	
	@Override
	public  List<NameValuePair> getQuery(Parameter parameter) {
	    List<NameValuePair> params = Lists.newArrayList();
	    
	    params.add(new BasicNameValuePair("advertKindCd", "20"));
	    params.add(new BasicNameValuePair("advertQuery", parameter.getQuery()));
	    params.add(new BasicNameValuePair("siteNo", parameter.getSiteNo()));
	    params.add(new BasicNameValuePair("salestrLst", ""));
	    params.add(new BasicNameValuePair("returnTypeCd", "10"));
		
	    return params;
	}
	
	@Override
	public Result getResult(String searchResult, String name, Parameter parameter, Result result) {
		
		List<Advertising> advertisingList = Lists.newArrayList();
		int advertItemCnt = 0;
		
//		JSONObject json = JSONObject.fromObject(searchResult);
//		String resCode = json.getString("res_code");
//
//		if ("200".equals(resCode) && json.getInt("advertDispTgtCnt")> 0) {
//			advertItemCnt = json.getInt("advertDispTgtCnt");
//			JSONArray arr = json.getJSONArray("dispTgtAccumDtoList");
//
//			for(int i=0; i<arr.size(); i++){
//				Advertising advertising = new Advertising();
//				JSONObject o = arr.getJSONObject(i);
//
//				advertising.setAdvertAcctId(o.getString("advertAcctId"));
//				advertising.setAdvertBidId(o.getString("advertBidId"));
//				advertising.setAdvertBilngTypeCd(o.getString("advertBilngTypeCd"));
//				advertising.setAdvertKindCd(o.getString("advertKindCd"));
//				advertising.setSiteNo(o.getString("siteNo"));
//				advertising.setItemId(o.getString("itemId"));
//				advertising.setItemNm(o.getString("itemNm"));
//				advertising.setSellprc(o.getString("sellprc"));
//				advertising.setItemRegDivCd(o.getString("itemRegDivCd"));
//				advertising.setShppTypeDtlCd(o.getString("shppTypeDtlCd"));
//				advertising.setExusItemDivCd(o.getString("exusItemDivCd"));
//				advertising.setExusItemDtlCd(o.getString("exusItemDtlCd"));
//				advertising.setShppMainCd(o.getString("shppMainCd"));
//				advertising.setShppMthdCd(o.getString("shppMthdCd"));
//				advertising.setDispOrdr(o.getString("advertDispTgtCnt"));
//				advertising.setAdvertExtensTeryDivCd(o.getString("advertExtensTeryDivCd"));
//
//				advertisingList.add(advertising);
//			}
//		} else if (!"200".equals(resCode)) {
//			MonitorInformation.logging(Level.ERROR, "DP001_SMS_GROUP001 AdvertisingQueryBuilder result fail message {} : " + json.getString("res_message"));
//		}
		
		result.setAdvertisingCount(advertItemCnt);
		result.setAdvertisingList(advertisingList);
		
		return result;
	}
}
