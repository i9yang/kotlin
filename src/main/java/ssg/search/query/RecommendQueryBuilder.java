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
import io.searchbox.params.Parameters;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Collection;
import ssg.search.collection.es.rsearch.MyTasteCollection;
import ssg.search.collection.es.rsearch.RecommendCollection;
import ssg.search.constant.Targets;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RecommendQueryBuilder implements QueryBuilder{
	private Logger logger = LoggerFactory.getLogger(ESQueryBuilder.class);

	private StringBuilder requestQuery = new StringBuilder();
	private Map<RecommendCollection, String> queryMap = Maps.newHashMap();
	private List<MultiSearchResult.MultiSearchResponse> searchResultList;
	private Parameter parameter;
	private JestClient client;
	private String errMsg;

	@Override
	public void set(Parameter parameter) {
		this.parameter = parameter;
		Targets targets = CollectionUtils.getTargets(parameter);
		for (Iterator<Collection> iter = targets.getCollectionSet(parameter).iterator(); iter.hasNext(); ) {
			Collection collection = iter.next();
			if(collection instanceof RecommendCollection){
				String queryString = ((RecommendCollection) collection).getQuery(parameter);
				queryMap.put((RecommendCollection)collection, queryString);
				requestQuery.append(System.getProperty("line.separator"));
				requestQuery.append(CollectionUtils.getDivPipe());
				requestQuery.append(System.getProperty("line.separator"));
				requestQuery.append("collection : " + collection.getCollectionName(parameter) + ", " + collection.getCollectionAliasName(parameter));
				requestQuery.append(System.getProperty("line.separator"));
				requestQuery.append(queryString);
				requestQuery.append(System.getProperty("line.separator"));
				requestQuery.append(CollectionUtils.getDivPipe());
			}
		}
	}

	@Override
	public void execute() {
		if (queryMap != null && queryMap.size() > 0) {
			try {
				JestClientFactory factory = new JestClientFactory();
				factory.setHttpClientConfig(new HttpClientConfig.Builder(parameter.getrUrl())
						.connTimeout(3000)
						.readTimeout(5000)
						.build());
	
				client = factory.getObject();
	
				List<Search> searchList = Lists.newArrayList();
				for (Map.Entry<RecommendCollection, String> entry : queryMap.entrySet()) {
					Search.Builder builder = new Search.Builder(entry.getValue())
							.addIndex(entry.getKey().getCollectionName(parameter));
					if(entry.getKey() instanceof MyTasteCollection){
						builder = builder.setParameter(Parameters.ROUTING, parameter.getUserInfo().getMbrId());
					}
					builder.build();
	
					searchList.add(builder.build());
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
	}

	@Override
	public void close() {
		if (client != null) {
			client.shutdownClient();
			client = null;
		}
	}

	@Override
	public void debug() {
		if(StringUtils.isNotEmpty(errMsg)) {
			logger.info(errMsg);
		} else {
			logger.info(requestQuery.toString());
		}
	}

	@Override
	public Result result(Result result) {
//		if(StringUtils.isNotEmpty(errMsg)) {
//			//throw new RuntimeException(errMsg);
//			MonitorInformation.logging(Level.ERROR, "DP001_SMS_GROUP001 Rsearch ERROR : {}"+errMsg);
//		}
		int i = 0;
		SearchResult searchResult;
		RecommendCollection collection;
		if (searchResultList != null) {
			for (Map.Entry<RecommendCollection, String> entry : queryMap.entrySet()) {
				collection = entry.getKey();
				MultiSearchResult.MultiSearchResponse response = searchResultList.get(i++);
//				if(response.isError) {
//					//throw new RuntimeException(response.errorMessage);
//					MonitorInformation.logging(Level.ERROR, "DP001_SMS_GROUP001 Rsearch ERROR : {}"+response.errorMessage);
//				}
				searchResult = response.searchResult;
				if(searchResult != null) {
					collection.getResult(searchResult, collection.getCollectionName(parameter), parameter, result);
					result.setElapsedTime(result.getElapsedTime() + searchResult.getJsonObject().get("took").getAsInt());
				}
			}
		}
		return result;
	}
}
