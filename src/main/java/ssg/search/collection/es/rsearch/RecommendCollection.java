package ssg.search.collection.es.rsearch;

import java.util.List;

import QueryAPI510.Search;
import io.searchbox.core.SearchResult;
import ssg.search.base.Collection;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;

public abstract class RecommendCollection implements Collection {
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result){
		return null;
	}

	public String getQuery(Parameter parameter) {
		return null;
	}

	@Override
	public String getCollectionName(Parameter parameter) {
		return "";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "";
	}

	@Override
	public String[] getDocumentField(Parameter parameter) {
		return null;
	}

	@Override
	public String[] getSearchField(Parameter parameter) {
		return null;
	}

	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		return result;
	}
}
