package ssg.search.collection.es.disp;

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
import ssg.search.constant.Shpp;
import ssg.search.function.Groupable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Prc;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.List;
import java.util.Map;

public class ESSellPrcLstGroup extends ESCollection implements Groupable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESSellPrcLstGroup";
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
				, ESQuery.SIZE
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
				, ESQuery.SCOM_EXPSR_YN
		)) {
			if (StringUtils.isNotEmpty(query.getValue(parameter))) {
				queryMap.put(query.getField(parameter), query.getValue(parameter));
			}
		}

		// cls
		String strCls = StringUtils.defaultIfEmpty(parameter.getCls(), "");
		List<String> clsList = Lists.newArrayList();

		if (strCls.indexOf("emart") > -1) {
			clsList.add("EM");
		}

		if (strCls.indexOf("department") > -1) {
			clsList.add("SD");
		}

		if (clsList.size() > 0) {
			queryMap.put("CLS", Joiner.on(" OR ").join(clsList));
		}

		// benefit
		String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
		List<String> benefitList = Lists.newArrayList();

		if (StringUtils.isNotEmpty(strBenefit)) {
			for (String benefit : strBenefit.split("\\|")) {
				benefitList.add(StringUtils.upperCase(benefit));
			}
			queryMap.put("FILTER", Joiner.on(" OR ").join(benefitList));
		}

		// shpp
		List<String> shppList = Lists.newArrayList();
		String strShpp = StringUtils.defaultIfEmpty(parameter.getShpp(), "");
		String strPickuSalestr = StringUtils.defaultIfEmpty(parameter.getPickuSalestr(), "");
		if (strShpp.indexOf("picku") > -1) {
			if (!strPickuSalestr.equals("")) {
				shppList.add(Shpp.PICKU.getPrefix() + strPickuSalestr);
			} else {
				shppList.add(Shpp.PICKU.getPrefix());
			}
		}

		if (strShpp.indexOf("qshpp") > -1) {
			shppList.add(Shpp.QSHPP.getPrefix());
		}
		if (strShpp.indexOf("con") > -1) {
			shppList.add("CON");
		}
		if (strShpp.indexOf("ssgem") > -1) {
			shppList.add("SSGEM");
		}
		if (strShpp.indexOf("deliem") > -1) {
			shppList.add("DELIEM");
		}
		if (strShpp.indexOf("ssgtr") > -1) {
			shppList.add("SSGTR");
		}

		if (shppList.size() > 0) {
			queryMap.put("SHPP", Joiner.on(" OR ").join(shppList));
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
				return new Info("SELLPRC_LST");
			}
		};
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		int min = 0;
		int max = 0;

		List<String> sellprcLst = Lists.newArrayList();
		List<Prc> prcGroupList = Lists.newArrayList();

		for (TermsAggregation.Entry e : searchResult.getAggregations().getTermsAggregation(getGroup().apply(parameter).getPredicate()).getBuckets()) {
			sellprcLst.add(e.getKey() + "^^" + e.getCount());
		}

		if(!sellprcLst.equals("")){
			prcGroupList = ResultUtils.getSellprcGroupping(Joiner.on("@").join(sellprcLst));

			if (prcGroupList.size() > 0) {
				max = prcGroupList.get(prcGroupList.size() - 1).getMaxPrc();
			}
		}

		result.setMinPrc(min);
		result.setMaxPrc(max);
		result.setPrcGroupList(prcGroupList);
		return result;
	}
}
