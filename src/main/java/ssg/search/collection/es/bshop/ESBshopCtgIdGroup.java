package ssg.search.collection.es.bshop;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.collection.es.ESQuery;
import ssg.search.collection.es.disp.ESItem;
import ssg.search.constant.Shpp;
import ssg.search.function.Groupable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Category;
import ssg.search.result.Result;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ESBshopCtgIdGroup extends ESItem implements Groupable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESBshopCtgIdGroup";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();

		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_BSHOP
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

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		List<String> ctgLst = Lists.newArrayList();
		for (TermsAggregation.Entry e : searchResult.getAggregations().getTermsAggregation(getGroup().apply(parameter).getPredicate()).getBuckets()) {
			ctgLst.add(e.getKey() + "^" + e.getCount());
		}

		List<Category> categoryList = Lists.newArrayList();

		for (String ctg : ctgLst) {
			String ctgId = ctg.split("\\^")[0];
			String[] ctgs = ctg.split("\\^")[1].split("\\__");

			if (StringUtils.equals(ctgs[1], "1") && StringUtils.equals(ctgs[2], parameter.getSiteNo()) && StringUtils.equals(ctgs[3], "10") && StringUtils.equals(ctgs[4], "10")) {
				Category category = new Category();
				category.setSiteNo(ctgs[2]);
				category.setCtgId(ctgId);
				category.setCtgNm(ctgs[0].replaceAll("\\++", " "));
				category.setCtgItemCount(Integer.parseInt(ctg.split("\\^")[2]));
				if (StringUtils.equals(parameter.getCtgId(), ctgs[1])) category.setSelectedArea("selected");
				categoryList.add(category);
			}
		}

		if (categoryList != null && categoryList.size() > 0) {
			// Category Result Sort ( 상품 많은 순 )
			Collections.sort(categoryList, new Comparator<Category>() {
				public int compare(Category c1, Category c2) {
					if (c1.getCtgItemCount() > c2.getCtgItemCount()) {
						return -1;
					} else if (c1.getCtgItemCount() < c2.getCtgItemCount()) {
						return 1;
					} else {
						return 0;
					}
				}
			});
		}
		result.setCategoryList(categoryList);
		return result;
	}

	@Override
	public Call<Info> getGroup() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return new Info("DISP_CTG_CLS_INFO");
			}
		};
	}
}
