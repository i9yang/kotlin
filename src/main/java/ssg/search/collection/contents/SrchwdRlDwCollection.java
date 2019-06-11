package ssg.search.collection.contents;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import QueryAPI510.Search;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.SrchwdRl;

public class SrchwdRlDwCollection implements Collection, Prefixable{

	public String getCollectionName(Parameter parameter){
		return "srchrldw";
	}

	public String getCollectionAliasName(Parameter parameter){
		return "srchrldw";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{"SRCHWD_NM","RL_KEYWD_NM"};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{"SRCHWD_NM"};
	}

	public Call<Info> getPrefix() {
		return new Call<Info>() {
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
						Prefixes.SITE_NO_ONLY
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<SrchwdRl> rlList = Lists.newArrayList();
		SrchwdRl srchwdRl;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			srchwdRl = new SrchwdRl();
			srchwdRl.setSrchwdNm(search.w3GetField(name, "SRCHWD_NM", i));
			srchwdRl.setRlKeywdNm(search.w3GetField(name, "RL_KEYWD_NM", i));
			rlList.add(srchwdRl);
		}
		// 기존의 연관 검색어 결과가 없는 경우에만 이 컬렉션의 결과를 사용한다.
		if(result.getSrchwdRlCount() <= 0){
			result.setSrchwdRlList(rlList);
			result.setSrchwdRlCount(search.w3GetResultTotalCount(name));
		}
		return result;
	}
}