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
import ssg.search.result.Brand;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.List;
import java.util.Map;

public class ESGlobalBrandIdGroup extends ESCollection implements Groupable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESGlobalBrandIdGroup";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();

		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_GLOBAL
				, ESQuery.DISP_CTG_LST
                , ESQuery.DISP_CTG_SUB_LST
				, ESQuery.SALESTR_LST
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
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
				return new Info("BRAND_ID|BRAND_NM.raw");
			}
		};
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		String[] fields = getGroup().apply(parameter).getPredicate().split("\\|");
		List<TermsAggregation.Entry> entryList = searchResult.getAggregations().getTermsAggregation(fields[0]).getBuckets();
		List<String> brandInfoList = Lists.newArrayList();
		for (TermsAggregation.Entry entry : entryList) {
			String brandId = entry.getKey();
			String brandNm = entry.getTermsAggregation(fields[1]).getBuckets().get(0).getKey();

			if(StringUtils.isNotEmpty(brandNm)) {
				String itemCount = String.valueOf(entry.getCount());
				brandInfoList.add(brandId + "^" + brandNm + "^" + itemCount);
			}
		}
		List<Brand> brandList = ResultUtils.getBrandGroup(Joiner.on("@").join(brandInfoList));
		result.setBrandList(brandList);

		return result;
	}
}
