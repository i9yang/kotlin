package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.IssueTheme;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

public class IssueThemeCollection implements Collection, Prefixable, Pageable{

	public String getCollectionName(Parameter parameter){
		return "issuetheme";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "issuetheme";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"CURA_ID",
			"CURA_SRCH_TYPE_CD",
			"SITE_NO",
			"TGT_SRCHWD_NM",
			"CRITN_SRCHWD_NM",
			"RPLC_KEYWD_NM",
			"ITEM_ID_LST",
			"DISP_ORDR",
			"DISP_STRT_DTS",
			"DISP_END_DTS",
			"APL_TGT_MEDIA_CD"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"CRITN_SRCHWD_NM"
		};
	}
	
	public Call<Info> getPrefix() {
		return new Call<Info>() {
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					Prefixes.SITE_NO_USE_FILTER
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				String aplTgtMediaCd = parameter.getAplTgtMediaCd();
	            if(aplTgtMediaCd != null){
	            	sb.append("<APL_TGT_MEDIA_CD:contains:00|").append(aplTgtMediaCd).append(">");
	            }
				return new Info(sb.toString(), 1);
			}
		};
	}
	
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "40";
				}
				if(strTarget.equalsIgnoreCase("all")) {
					strPage = "1";
					strCount = "40";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<IssueTheme> issueThemeList = Lists.newArrayList();
		IssueTheme issueTheme;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			issueTheme = new IssueTheme();
			issueTheme.setCuraId(search.w3GetField(name, 	"CURA_ID", i));
			issueTheme.setCuraSrchTypeCd(search.w3GetField(name, 	"CURA_SRCH_TYPE_CD", i));
			issueTheme.setSiteNo(search.w3GetField(name, 	"SITE_NO", i));
			issueTheme.setTgtSrchwdNm(search.w3GetField(name, 	"TGT_SRCHWD_NM", i));
			issueTheme.setCritnSrchwdNm(search.w3GetField(name, 	"CRITN_SRCHWD_NM", i));
			issueTheme.setRplcKeywdNm(search.w3GetField(name, 	"RPLC_KEYWD_NM", i));
			issueTheme.setItemIdLst(search.w3GetField(name, 	"ITEM_ID_LST", i));
			issueTheme.setDispOrdr(search.w3GetField(name, 	"DISP_ORDR", i));
			issueTheme.setDispStrtDts(search.w3GetField(name, 	"DISP_STRT_DTS", i));
			issueTheme.setDispEndDts(search.w3GetField(name, 	"DISP_END_DTS", i));
			issueThemeList.add(issueTheme);
		}
		result.setIssueThemeList(issueThemeList);
		result.setIssueThemeCount(search.w3GetResultTotalCount(name));
		return result;
	}
	
}
