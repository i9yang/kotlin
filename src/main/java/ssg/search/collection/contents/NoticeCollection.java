package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
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
import ssg.search.result.Notice;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.List;

public class NoticeCollection implements Collection, Pageable, Prefixable, Highlightable, Sortable{
	
	public String getCollectionName(Parameter parameter){
		return "notice";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "notice";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"POSTNG_ID",
			"POSTNG_TITLE_NM",
			"WRT_DATE",
			"SITE_NO",
			"SITE_NM",
			"NEWIMG",
			"PAGENO"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"POSTNG_TITLE_NM",
			"SITE_NM",
			"POSTNG_CNTT"
		};
	}

	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder prefix = new StringBuilder();
				//모바일,pc
	            String aplTgtMediaCd = parameter.getAplTgtMediaCd();
	            if(aplTgtMediaCd != null){
	            	prefix.append("<DVIC_DIV_CD:contains:").append(aplTgtMediaCd).append(">");
	            }
	            return new Info(prefix.toString(),1);
			}
			
		};
	}
	
	public Call<Info> getPage(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0")){
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
		List<Notice> NoticeList = Lists.newArrayList();
		Notice notice;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			notice = new Notice();
			notice.setPostngId(search.w3GetField(name, "POSTNG_ID", i));
			notice.setPostngTitleNm(search.w3GetField(name, "POSTNG_TITLE_NM", i).replaceAll("<!HS>", "<strong>").replaceAll("<!HE>", "</strong>"));
			notice.setWrtDate(search.w3GetField(name, "WRT_DATE", i));
			notice.setSiteNo(search.w3GetField(name, "SITE_NO", i));
			notice.setSiteNm(search.w3GetField(name, "SITE_NM", i));
			notice.setNewImg(search.w3GetField(name, "NEWIMG", i));
			notice.setPageNo(search.w3GetField(name, "PAGENO", i));
			NoticeList.add(notice);
		}
		result.setNoticeCount(search.w3GetResultCount(name));
		result.setNoticeList(NoticeList);
		return result;
	}

}
