package ssg.search.collection.es.disp;

import io.searchbox.core.SearchResult;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;

public class ESBookMallCount extends ESMallCount {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "book";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESBookMallCount";
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		result.setBookMallCountMap(getMallCountMap(searchResult, parameter));
		return result;
	}
}
