package ssg.search.collection.es.brand;

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

public class ESBrandBookCtgIdGroup extends ESCollection implements Groupable {
	@Override
	public String getCollectionName(Parameter parameter) {
		return "book";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESBrandBookCtgIdGroup";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();

		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_DISP
				, ESQuery.BRAND_ID
				, ESQuery.SALESTR_LST
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
				, ESQuery.SCOM_EXPSR_YN
		)) {
			if (StringUtils.isNotEmpty(query.getValue(parameter))) {
				queryMap.put(query.getField(parameter), query.getValue(parameter));
			}
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

		if(StringUtils.equals(parameter.getUserInfo().getDeviceDivCd(), "20")) {
			result.setBookCategoryList(ResultUtils.getNewBrandCategoryGroup(parameter.getSiteNo(), parameter.getDispCtgId(), Joiner.on("@").join(ctgLst)));
		} else {
			result.setBookCategoryList(ResultUtils.getBrandCategoryGroup(parameter.getSiteNo(), parameter.getDispCtgId(), Joiner.on("@").join(ctgLst)));
		}

		return result;
	}
}
