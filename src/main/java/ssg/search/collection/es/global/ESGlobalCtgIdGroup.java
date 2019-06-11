package ssg.search.collection.es.global;

import QueryAPI510.Search;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.collection.es.ESCollection;
import ssg.search.collection.es.ESQuery;
import ssg.search.function.Groupable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.List;
import java.util.Map;

public class ESGlobalCtgIdGroup extends ESCollection implements Groupable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESGlobalCtgIdGroup";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();

		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_GLOBAL
				, ESQuery.SALESTR_LST
				, ESQuery.BRAND_ID
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
		)) {
			queryMap.put(query.getField(parameter), query.getValue(parameter));
		}

		List<String> queryList = Lists.newArrayList();
		for (Map.Entry<String, String> e : queryMap.entries()) {
			if (StringUtils.isNotEmpty(e.getValue())) {
				queryList.add(e.getKey() + ":(" + e.getValue() + ")");
			}
		}
		return queryList;
	}

	@Override
	public Call<Info> getGroup() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return new Info("DISP_CTG_CLS_INFO");
			}
		};
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		List<String> ctgLst = Lists.newArrayList();
		for (TermsAggregation.Entry e : searchResult.getAggregations().getTermsAggregation(getGroup().apply(parameter).getPredicate()).getBuckets()) {
			ctgLst.add(e.getKey() + "^" + e.getCount());
		}

		result.setCategoryList(ResultUtils.getGlobalCategoryGroup(parameter.getSiteNo(), parameter.getDispCtgId(), Joiner.on("@").join(ctgLst), "global_brand"));
		return result;
	}
}
