package ssg.search.collection.es.disp;

import com.google.common.collect.*;
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

import java.util.List;
import java.util.Map;

public class ESMallCount extends ESCollection implements Groupable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESMallCount";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();
		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_MALL
				, ESQuery.DISP_CTG_LST
                , ESQuery.DISP_CTG_SUB_LST
				, ESQuery.BRAND_ID
				, ESQuery.SALESTR_LST_MALL
				, ESQuery.SIZE
				, ESQuery.COLOR
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
				, ESQuery.PRC_FILTER
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
				return new Info("SITE_NO,SCOM_EXPSR_YN");
			}
		};
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		result.setMallCountMap(getMallCountMap(searchResult, parameter));
		return result;
	}

	public Map<String, String> getMallCountMap(SearchResult searchResult, Parameter parameter) {
		Map<String, String> mallCountMap = Maps.newHashMap();
		String[] fields = getGroup().apply(parameter).getPredicate().split(",");

		for (String f : fields) {
			List<TermsAggregation.Entry> entryList = searchResult.getAggregations().getTermsAggregation(f).getBuckets();
			if (StringUtils.equals(f, "SITE_NO")) {
				for (TermsAggregation.Entry entry : entryList) {
					mallCountMap.put(entry.getKey(), String.valueOf(entry.getCount()));
				}
			} else if (StringUtils.equals(f, "SCOM_EXPSR_YN")) {
				for (TermsAggregation.Entry entry : entryList) {
					if (StringUtils.equals(entry.getKey(), "Y")) {
						mallCountMap.put("6005", String.valueOf(entry.getCount()));
					}

					mallCountMap.put(entry.getKey(), String.valueOf(entry.getCount()));
				}
			}
		}

		return mallCountMap;
	}
}
