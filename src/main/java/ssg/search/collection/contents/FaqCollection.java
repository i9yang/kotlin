package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Highlightable;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Faq;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.List;

public class FaqCollection implements Collection, Prefixable, Highlightable, Pageable{

	public String getCollectionName(Parameter parameter){
		return "faq";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "faq";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"POSTNG_ID",
			"POSTNG_TITLE_NM",
			"POSTNG_CNTT",
			"POSTNG_BRWS_CNT"
		};
	}

	public String[] getSearchField(Parameter parameter){
	    //고객센터에서 쓰기 때문에 분기 처리
	    String strTarget = parameter.getTarget();
	    if (StringUtils.isNotBlank(strTarget) && strTarget.equals("faq")) {
	        return new String[]{
	                "POSTNG_TITLE_NM",
	                "POSTNG_CNTT"
	            };
	    }else {
	        return new String[]{
                    "POSTNG_TITLE_NM"
                };
	    }
		
	}

	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				FrontUserInfo userInfo = parameter.getUserInfo();
			    String strPrefix = Prefixes.DVIC_DIV_CD.getPrefix(parameter);
			    if(userInfo!=null && "B2E".equals(userInfo.getMbrType())){
			        // SEC
			        if("0000000040".equals(userInfo.getMbrCoId()) ||
			           "0000000041".equals(userInfo.getMbrCoId())
			                ){
			        	strPrefix += "<SITE_TYPE_CD:contains:SEC>";
			        }
			        // SFC
			        else if("0000000107".equals(userInfo.getMbrCoId()) ||
			                "0000000109".equals(userInfo.getMbrCoId())
			                ){
			        	strPrefix += "<SITE_TYPE_CD:contains:SFC>";
			        }else{
			        	strPrefix += "<SITE_TYPE_CD:contains:SSG>";
			        }
			    }else{
			    	strPrefix += "<SITE_TYPE_CD:contains:SSG>";
			    }
				return new Info(strPrefix, 1);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<Faq> faqList = Lists.newArrayList();
		Faq faq;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			faq = new Faq();
			faq.setPostngId(search.w3GetField(name, "POSTNG_ID", i));
			faq.setPostngTitleNm(search.w3GetField(name, "POSTNG_TITLE_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
			faq.setPostngCntt(search.w3GetField(name, "POSTNG_CNTT", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
			faq.setPostngBrwsCnt(search.w3GetField(name, "POSTNG_BRWS_CNT", i));
			faqList.add(faq);
		}
		result.setFaqList(faqList);
		result.setFaqCount(search.w3GetResultTotalCount(name));
		return result;
	}
	
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "5";
				} 
				if(strTarget.equalsIgnoreCase("all")) {
					strPage = "1";
					strCount = "5";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
}
