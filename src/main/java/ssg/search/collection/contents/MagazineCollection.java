package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Sorts;
import ssg.search.function.Highlightable;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Magazine;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MagazineCollection implements Collection, Pageable, Prefixable, Highlightable, Sortable{

	public String getCollectionName(Parameter parameter) {
		return "magazine";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "magazine";
	}

	public String[] getDocumentField(Parameter parameter) {
		return new String[]{
			"DISP_CMPT_ID",
			"DISP_CMPT_NM",
			"HTML_CNTT",
			"IMG_FILE_NM",
			"SRCHWD_NM",
			"APL_TGT_MEDIA_CD",
			"MOBILE_DISPLAY_YN",
			"PNSHOP_STRT_DTS",
			"PNSHOP_END_DTS",
			"WRT_DATE"
		};
	}

	public String[] getSearchField(Parameter parameter) {
		return new String[]{
			"DISP_CMPT_ID",
			"DISP_CMPT_NM",
			"SRCHWD_NM",
			"HTML_CNTT"
		};
	}

	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				StringBuilder prefix = new StringBuilder();
				FrontUserInfo userInfo = parameter.getUserInfo();
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
	            //모바일,pc
	            String aplTgtMediaCd = parameter.getAplTgtMediaCd();
	            if(aplTgtMediaCd != null){
	            	prefix.append("<APL_TGT_MEDIA_CD:contains:").append(aplTgtMediaCd).append(">");
	            }
				return new Info(prefix.toString(),1);
			}
		};
	}
	
	public Call<Info> getPage(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "20";
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
	
	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		List<Magazine> magazineList = Lists.newArrayList();
		Magazine magazine;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			magazine = new Magazine();
			magazine.setDispCmptId(search.w3GetField(name, "DISP_CMPT_ID", i));
			magazine.setDispCmptNm(search.w3GetField(name, "DISP_CMPT_NM", i));
			
			String srchwdNm = StringUtils.defaultIfEmpty(search.w3GetField(name, "SRCHWD_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"),"");
			magazine.setSrchwdNm(srchwdNm);
			//srchwdNm여러개일 경우 tagList 로 내려줌 
			if(!srchwdNm.equals("") && srchwdNm.indexOf(",") >-1){
				List<String> tagList = new ArrayList<String>();
				for(Iterator<String> tagIter = Splitter.on(",").trimResults().split(srchwdNm).iterator();tagIter.hasNext();){
					String srchwdNmTag = tagIter.next();
					tagList.add(srchwdNmTag);
				}
				magazine.setMagazineTag(tagList);
			}
			
			String imgFileNm = StringUtils.defaultIfEmpty(search.w3GetField(name, "IMG_FILE_NM", i), "");
			if(!imgFileNm.equals("")) imgFileNm = "http://static.ssgcdn.com"+ imgFileNm;
			magazine.setImgFileNm(imgFileNm);
			magazine.setHtmlCntt(search.w3GetField(name, "HTML_CNTT", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
			magazine.setAplTgtMediaCd(search.w3GetField(name, "APL_TGT_MEDIA_CD", i));
			magazine.setAplTgtMediaLst(search.w3GetField(name, "APL_TGT_MEDIA_LST", i));
			magazine.setMobileDisplayYn(search.w3GetField(name, "MOBILE_DISPLAY_YN", i));
			magazine.setPnshopStrtDts(search.w3GetField(name, "PNSHOP_STRT_DTS", i));
			magazine.setPnshopEndDts(search.w3GetField(name, "PNSHOP_END_DTS", i));
			magazine.setWrtDate(search.w3GetField(name, "WRT_DATE", i));
			magazineList.add(magazine);
		}
		result.setMagazineCount(search.w3GetResultTotalCount(name));
		result.setMagazineList(magazineList);
		return result;
	}

}
