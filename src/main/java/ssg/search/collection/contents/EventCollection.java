package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Sorts;
import ssg.search.function.*;
import ssg.search.parameter.Parameter;
import ssg.search.result.Event;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.List;

public class EventCollection implements Collection, Prefixable, Filterable, Pageable, Sortable, Highlightable{

	public String getCollectionName(Parameter parameter){
		return "event";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "event";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"SITE_NO", 
			"PROM_ID",
			"PROM_NM",
			"PROM_ENFC_STRT_DTS",
			"PROM_ENFC_END_DTS",
			"PROM_TYPE_CD",
			"EVNT_TYPE_CD",
			"OFFER_KIND_CD",
			"LINK_URL"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{ 
		        "PROM_ID",
		        "PROM_NM",
		        "BANR_CNTT"
		};
	}
	
	public Call<Info> getFilter(){
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				// NOT IN 을 이용해서 제외조건을 건다.
				FrontUserInfo userInfo = parameter.getUserInfo();
				StringBuilder filter = new StringBuilder();
				StringBuilder q = new StringBuilder();
				// B2C, B2E
		        String type = userInfo.getMbrType();
		        String coId = userInfo.getMbrCoId();
		        String chnl = userInfo.getChnlId();
		        if(type!=null && coId!=null){
		            if(type.equals("B2C")){
		                q.append("B2C_").append(coId);
		            }else if(type.equals("B2E")){
		                q.append("B2E_").append(coId);
		            }
		        }
		        if(chnl!=null && !chnl.equals("")){
		        	if(q!=null && !q.equals(""))q.append(" ");
		        	q.append("CHNL_").append(chnl);
		        }
				filter.append("<EXCP_TYPE_LST:notin:").append(q.toString()).append(">");
				return new Info(filter.toString());
			}
		};
	}

	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				FrontUserInfo userInfo = parameter.getUserInfo();
				StringBuilder prefix = new StringBuilder();
				// SITE_NO
				String siteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "");
				if(!siteNo.equals("")){
					prefix.append("<SITE_NO:contains:").append(siteNo).append(">");
				}
				// DISP_DVIC_CD
	            String deviceCd = StringUtils.defaultIfEmpty(parameter.getAplTgtMediaCd(), "");
	            if(!deviceCd.equals("")){
	            	prefix.append("<DISP_DVIC_CD:contains:").append(deviceCd).append(">");
	            }
				// B2C, B2E
	            String type = userInfo.getMbrType();
	            String coId = userInfo.getMbrCoId();
	            if(type!=null && coId!=null){
	                if(type.equals("B2C")){
	                    prefix.append("<APL_B2C_LST:contains:").append(coId).append(">");
	                }else if(type.equals("B2E")){
	                    prefix.append("<APL_B2E_LST:contains:B2E|").append(coId).append(">");
	                }
	            }
	            // APL_CHNL_LST
	            String chnl = userInfo.getChnlId();
	            if(chnl!=null && !chnl.equals("")){
	            	prefix.append("<APL_CHNL_LST:contains:ALL|").append(chnl).append(">");
	            }else{
	            	prefix.append("<APL_CHNL_LST:contains:ALL>");
	            }
				return new Info(prefix.toString(),1);
			}
		};
	}
	
	public Call<Info> getPage(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if (strTarget.equalsIgnoreCase("all")) {
					strPage  = "1";
					strCount = "5";
				}else if(strCount.equals("") || strCount.equals("0")){
					strCount = "5";
				}
				
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
	
	public Call<Info> getSort() {
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				List<Sort> sortList = Lists.newArrayList();
				sortList.add(Sorts.WRT_DATE.getSort(parameter));
				return new Info(sortList);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<Event> eventList = Lists.newArrayList();
		int count = search.w3GetResultCount(name);
		Event event;
		for(int i=0;i<count;i++){
			event = new Event();
			event.setPromId(search.w3GetField(name, "PROM_ID", i));
			event.setPromNm(search.w3GetField(name, "PROM_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
			event.setPromEnfcStrtDts(search.w3GetField(name, "PROM_ENFC_STRT_DTS", i));
			event.setPromEnfcEndDts(search.w3GetField(name, "PROM_ENFC_END_DTS", i));
			event.setSiteNo(search.w3GetField(name, "SITE_NO", i));
			event.setOfferKindCd(search.w3GetField(name, "OFFER_KIND_CD", i));
			event.setPromTypeCd(search.w3GetField(name, "PROM_TYPE_CD", i));
			event.setEvntTypeCd(search.w3GetField(name, "EVNT_TYPE_CD", i));
			event.setLinkUrl(search.w3GetField(name, "LINK_URL", i));
			eventList.add(event);
		}
		result.setEventCount(search.w3GetResultTotalCount(name));
		result.setEventList(eventList);
		return result;
	}

}
