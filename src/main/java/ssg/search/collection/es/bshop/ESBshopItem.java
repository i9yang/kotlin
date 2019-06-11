package ssg.search.collection.es.bshop;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import ssg.search.collection.es.ESQuery;
import ssg.search.collection.es.disp.ESItem;
import ssg.search.constant.Shpp;
import ssg.search.parameter.Parameter;

import java.util.List;
import java.util.Map;

public class ESBshopItem extends ESItem {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESBshopItem";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();

		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_BSHOP
				, ESQuery.DISP_CTG_LST
                , ESQuery.DISP_CTG_SUB_LST
				, ESQuery.SALESTR_LST
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
				, ESQuery.BSHOP_ID
		)) {
			if (StringUtils.isNotEmpty(query.getValue(parameter))) {
				queryMap.put(query.getField(parameter), query.getValue(parameter));
			}
		}

		String strShpp = StringUtils.defaultIfEmpty(parameter.getShpp(), "");
		String strPickuSalestr = StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");
		if (strShpp.indexOf("picku") > -1) {
			if (!strPickuSalestr.equals("")) {
				queryMap.put("SHPP", Shpp.PICKU.getPrefix() + strPickuSalestr);
			} else {
				queryMap.put("SHPP", Shpp.PICKU.getPrefix());
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

}
