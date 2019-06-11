package ssg.search.collection.es.global;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import ssg.search.collection.es.ESQuery;
import ssg.search.collection.es.disp.ESItem;
import ssg.search.parameter.Parameter;

import java.util.List;
import java.util.Map;

public class ESGlobalItem extends ESItem {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESGlobalItem";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();

		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_GLOBAL
				, ESQuery.DISP_CTG_LST
                , ESQuery.DISP_CTG_SUB_LST
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
}
