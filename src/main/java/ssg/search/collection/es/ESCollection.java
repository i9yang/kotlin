package ssg.search.collection.es;

import QueryAPI510.Search;
import io.searchbox.core.SearchResult;
import ssg.search.base.Collection;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;

import java.util.List;

public abstract class ESCollection implements Collection {
	private String collectionName = "item";

	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result){
		return null;
	}

	public List<String> getQuery(Parameter parameter) {
		return null;
	}

	public List<String> getOrQuery(Parameter parameter) {
		return null;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	@Override
	public String getCollectionName(Parameter parameter) {
		return collectionName;
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESCollection";
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
		return null;
	}
}
