package ssg.search.collection.contents;

import java.util.Iterator;
import java.util.List;

import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.SrchGuide;
import QueryAPI510.Search;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

public class SrchGuideCollection implements Collection, Prefixable{

	public String getCollectionName(Parameter parameter){
		return "srchguide";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "srchguide";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"CRITN_SRCHWD_NM",
			"SRCH_TYPE_CD",
			"PNSHOP",
			"ISSUE_ITEM",
			"SOCIAL",
			"RECIPE",
			"TGT_ITEM",
			"KEYWD_NM_LST",
			"ITEM_ID_LST",
			"SHRTC_TGT_TYPE_CD",
			"SHRTC_TGT_ID",
			"LINK_URL", 
			"KEYWD_TAG",
			"PNSHOP_TAG",
			"RECIPE_TAG"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"CRITN_SRCHWD_NM",
			"TGT_SRCHWD_NM"
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
				return new Info(sb.toString(), 1);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<SrchGuide> srchGuideList = Lists.newArrayList();
		SrchGuide srchGuide;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			srchGuide = new SrchGuide();
			srchGuide.setSrchCritnId(search.w3GetField(name, 	"SRCH_CRITN_ID", i));
			srchGuide.setCritnSrchwdNm(search.w3GetField(name,  "CRITN_SRCHWD_NM", i));
			srchGuide.setSrchTypeCd(search.w3GetField(name, 	"SRCH_TYPE_CD", i));
			srchGuide.setPnshop(search.w3GetField(name, 		"PNSHOP", i));
			srchGuide.setIssueItem(search.w3GetField(name, 		"ISSUE_ITEM", i));
			srchGuide.setSocial(search.w3GetField(name, 		"SOCIAL", i));
			srchGuide.setRecipe(search.w3GetField(name, 		"RECIPE", i));
			srchGuide.setTgtItem(search.w3GetField(name, 		"TGT_ITEM", i));
			srchGuide.setKeywdNmLst(search.w3GetField(name, 	"KEYWD_NM_LST", i));
			srchGuide.setItemIdLst(search.w3GetField(name, 		"ITEM_ID_LST", i));
			srchGuide.setShrtcTgtId(search.w3GetField(name, 	"SHRTC_TGT_TYPE_CD", i));
			srchGuide.setShrtcTgtTypeCd(search.w3GetField(name, "SHRTC_TGT_ID", i));
			srchGuide.setLinkUrl(search.w3GetField(name, 		"LINK_URL", i));
			srchGuide.setKeywdTag(search.w3GetField(name, 	"KEYWD_TAG", i));
			srchGuide.setPnshopTag(search.w3GetField(name, 	"PNSHOP_TAG", i));
			srchGuide.setRecipeTag(search.w3GetField(name, 	"RECIPE_TAG", i));
			srchGuideList.add(srchGuide);
		}
		result.setSrchGuideList(srchGuideList);
		result.setSrchGuideCount(search.w3GetResultTotalCount(name));
		return result;
	}
	
}
