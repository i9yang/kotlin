package ssg.search.collection.advertising;

import QueryAPI510.Search;
import org.apache.http.NameValuePair;
import ssg.search.base.Collection;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;

import java.util.List;

public abstract class AdvertisingCollection implements Collection {
	public Result getResult(String searchResult, String name, Parameter parameter, Result result){
		return null;
	}

	public String getUrlPath(Parameter parameter) {
		return null;
	}
	
	public List<NameValuePair> getQuery(Parameter parameter) {
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
