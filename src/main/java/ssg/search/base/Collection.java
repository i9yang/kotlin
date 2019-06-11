package ssg.search.base;

import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import QueryAPI510.Search;

public interface Collection {
	public String getCollectionName(Parameter parameter);
	public String getCollectionAliasName(Parameter parameter);
	public String[] getDocumentField(Parameter parameter);
	public String[] getSearchField(Parameter parameter);
	public Result getResult(Search search, String name, Parameter parameter, Result result);
}