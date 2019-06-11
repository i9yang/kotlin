package ssg.search.collection.es.disp;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.searchbox.core.SearchResult;
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

public class ESSellPrcGroup extends ESCollection implements Groupable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESSellPrcGroup";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();
		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_DISP
				, ESQuery.DISP_CTG_LST
                , ESQuery.DISP_CTG_SUB_LST
				, ESQuery.BRAND_ID
				, ESQuery.SALESTR_LST
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
				, ESQuery.SCOM_EXPSR_YN
				, ESQuery.SPL_VEN_ID
				, ESQuery.GRP_ADDR_ID
				, ESQuery.SHPPCST_ID
				, ESQuery.ITEM_SITE_NO
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
				return new Info("SELLPRC");
			}
		};
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		Double minPrc = searchResult.getAggregations().getMinAggregation("minPrc").getMin();
		Double maxPrc = searchResult.getAggregations().getMinAggregation("maxPrc").getMin();

		result.setMinPrc(minPrc == null ? 0 : minPrc.intValue());
		result.setMaxPrc(maxPrc == null ? 0 : maxPrc.intValue());

		return result;
	}
}
