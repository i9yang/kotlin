package ssg.search.query;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.MultiSearch;
import io.searchbox.core.MultiSearchResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.collection.es.ESCollection;
import ssg.search.constant.EsErrCode;
import ssg.search.constant.Targets;
import ssg.search.function.Groupable;
import ssg.search.function.Pageable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Category;
import ssg.search.result.Item;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ESQueryBuilder implements QueryBuilder {
	private Logger logger = LoggerFactory.getLogger(ESQueryBuilder.class);

	private StringBuilder requestQuery = new StringBuilder();
	private Map<ESCollection, String> queryMap = Maps.newLinkedHashMap();
	private List<MultiSearchResult.MultiSearchResponse> searchResultList;
	private Parameter parameter;
	private JestClient client;
	private String errMsg;

	public void set(Parameter parameter) {
		this.parameter = parameter;
		Targets targets = CollectionUtils.getTargets(parameter);
		for (Iterator<Collection> iter = targets.getCollectionSet(parameter).iterator(); iter.hasNext(); ) {
			Collection collection = iter.next();
			ESQueryObject queryObj = new ESQueryObject();

			queryObj.setCollectionName(collection.getCollectionName(null));
			queryObj.setCollectionAliasName(collection.getCollectionAliasName(null));

			if (collection instanceof Pageable) {
				Info pageInfo = ((Pageable) collection).getPage().apply(parameter);
				queryObj.setFrom(pageInfo.getStart());
				queryObj.setSize(pageInfo.getCount());
			}

			if (collection instanceof Sortable) {
				queryObj.setSort(((Sortable) collection).getSort().apply(parameter).getSortList());
			}

			if (collection instanceof Groupable) {
				Info groupInfo = ((Groupable) collection).getGroup().apply(parameter);
				queryObj.setAggs(groupInfo.getPredicate());
				queryObj.setSize(0);
			}

			if (StringUtils.isNotEmpty(parameter.getQuery())) {
				queryObj.setStrQuery(parameter.getQuery().replaceAll("([]\\[*?])",""));
			}

			List<String> queryList = ((ESCollection) collection).getQuery(parameter);
			queryObj.setQueryList(queryList);

			List<String> orQueryList = ((ESCollection) collection).getOrQuery(parameter);
			queryObj.setOrQueryList(orQueryList);


			String query = queryObj.getJsonQuery();
			queryMap.put((ESCollection) collection, query);

			requestQuery.append(System.getProperty("line.separator"));
			requestQuery.append(CollectionUtils.getDivPipe());
			requestQuery.append(System.getProperty("line.separator"));
			requestQuery.append("collection : " + collection.getCollectionName(parameter) + ", " + collection.getCollectionAliasName(parameter));
			requestQuery.append(System.getProperty("line.separator"));
			requestQuery.append(query);
			requestQuery.append(System.getProperty("line.separator"));
			requestQuery.append(CollectionUtils.getDivPipe());
		}
	}

	public void execute() {
		try {
			JestClientFactory factory = new JestClientFactory();
			factory.setHttpClientConfig(new HttpClientConfig.Builder(parameter.getESUrl())
					.connTimeout(3000)
					.readTimeout(5000)
					.build());

			client = factory.getObject();

			List<Search> searchList = Lists.newArrayList();
			for (Map.Entry<ESCollection, String> entry : queryMap.entrySet()) {
				Search search = new Search.Builder(entry.getValue())
						.addIndex(entry.getKey().getCollectionName(parameter))
						.build();

				searchList.add(search);
		}

		MultiSearch multiSearch = new MultiSearch.Builder(searchList).build();
		searchResultList = client.execute(multiSearch).getResponses();

	} catch (Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			errMsg = sw.toString();
		}
	}

	public void close() {
		client.shutdownClient();
		client = null;
	}

	public void debug() {
		if(StringUtils.isNotEmpty(errMsg)) {
			logger.info(errMsg);
		} else {
			logger.info(requestQuery.toString());
		}
	}

	public Result result(Result result) {
		ESCollection collection;
		try {
			if(StringUtils.isNotEmpty(errMsg)) {
				throw new RuntimeException(errMsg);
			}

			int i = 0;
			SearchResult searchResult;
			for (Map.Entry<ESCollection, String> entry : queryMap.entrySet()) {
				collection = entry.getKey();
				MultiSearchResult.MultiSearchResponse response = searchResultList.get(i++);

				if(response.isError) {
					if(StringUtils.contains(response.errorMessage , "max_result_window")) {
						result.setEsErrCode(EsErrCode.PAGE_SIZE);
					}
					throw new RuntimeException(response.errorMessage);
				}

				searchResult = response.searchResult;

				if(searchResult != null) {
					collection.getResult(searchResult, collection.getCollectionName(parameter), parameter, result);
					result.setElapsedTime(result.getElapsedTime() + searchResult.getJsonObject().get("took").getAsInt());
				}
			}

			int itemTotalCount = result.getItemCount();
			itemTotalCount = itemTotalCount >= 500000 ? 500000 : itemTotalCount;

			int bookTotalCount = result.getBookCount();
			bookTotalCount = bookTotalCount >= 500000 ? 500000 : bookTotalCount;

			List<Item> bookItemList = result.getBookItemList();
			Map<String, String> bookMallCountMap = result.getBookMallCountMap();
			String bookItemIds = result.getBookItemIds();

			if (bookTotalCount > 0 && itemTotalCount < bookTotalCount && bookItemList != null && bookItemList.size() > 0 && bookItemIds != null) {
				result.setItemCount(bookTotalCount);
				result.setItemList(bookItemList);
				result.setSrchItemIds(bookItemIds);
				result.setMallCountMap(bookMallCountMap);
			} else if (itemTotalCount == 0 && bookTotalCount == 0) {
				Map<String, String> itemMallCountMap = result.getMallCountMap();
				if (itemMallCountMap == null && bookMallCountMap != null) {
					result.setItemCount(bookTotalCount);
					result.setItemList(bookItemList);
					result.setSrchItemIds(bookItemIds);
					result.setMallCountMap(bookMallCountMap);
				} else if (itemMallCountMap != null && bookMallCountMap != null) {
					String ssgBookCount = bookMallCountMap.get("6005");
					String ssgItemCount = itemMallCountMap.get("6005");
					int bookCount = 0;
					int itemCount = 0;
					if (ssgItemCount != null) {
						itemCount = Integer.parseInt(ssgItemCount);
					}
					if (ssgBookCount != null) {
						bookCount = Integer.parseInt(ssgBookCount);
					}
					if (bookCount > itemCount) {
						result.setItemCount(bookTotalCount);
						result.setItemList(bookItemList);
						result.setSrchItemIds(bookItemIds);
						result.setMallCountMap(bookMallCountMap);
					}
				}
			}

			Targets targets = CollectionUtils.getTargets(parameter);
			if(targets.equals(Targets.ES_BRAND_DISP) || targets.equals(Targets.ES_VIRTUAL) || targets.equals(Targets.ES_BUNDLE)){
				List<Category> bookCategoryList = result.getBookCategoryList();
				if(bookTotalCount > 0 && itemTotalCount<bookTotalCount && bookItemList!=null && bookItemList.size()>0 && bookItemIds!=null){
					result.setCategoryList(bookCategoryList);
				}
			}

			result.setResultYn(true);
		} catch (Exception e) {
			result.setLibErr(true);

			if(StringUtils.contains(errMsg , "connect timed out")) {
				result.setEsErrCode(EsErrCode.TIMEOUT);
			}

			if(StringUtils.isEmpty(errMsg)) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				errMsg = sw.toString();
			}
		}

		return result;
	}
}
