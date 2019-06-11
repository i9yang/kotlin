package ssg.search.collection.es.disp;

import com.google.common.collect.Lists;
import io.searchbox.core.SearchResult;
import ssg.search.parameter.Parameter;
import ssg.search.result.Item;
import ssg.search.result.Result;

import java.util.List;

public class ESBookItem extends ESItem {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "book";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESBookItem";
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		List<SearchResult.Hit<Item, Void>> list = searchResult.getHits(Item.class);
		List<Item> searchItemList = Lists.newArrayList();
		for (SearchResult.Hit h : list) {
			searchItemList.add((Item) h.source);
		}

		StringBuilder srchItemIds = new StringBuilder();
		super.processItemResult(parameter, searchItemList, srchItemIds);

		result.setBookItemList(searchItemList);
		result.setBookCount(searchResult.getTotal());
		result.setBookItemIds(srchItemIds.toString());
		return result;
	}
}
