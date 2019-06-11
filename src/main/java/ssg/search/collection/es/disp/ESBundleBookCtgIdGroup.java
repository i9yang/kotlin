package ssg.search.collection.es.disp;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.apache.commons.lang3.StringUtils;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.List;

public class ESBundleBookCtgIdGroup extends ESBundleCtgIdGroup{
	@Override
	public String getCollectionName(Parameter parameter) {
		return "book";
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
