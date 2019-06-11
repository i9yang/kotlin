package ssg.search.collection.es.virtual;

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
import ssg.search.result.Category;
import ssg.search.result.Result;

import java.util.*;

public class ESVirtual extends ESCollection implements Groupable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESVirtual";
	}

	@Override
	public List<String> getQuery(Parameter parameter) {
		Multimap<String, String> queryMap = HashMultimap.create();

		for (ESQuery query : ImmutableSet.of(
				ESQuery.SRCH_PREFIX_VIRTUAL
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

		result.setCategoryList(getVirtualCategoryGroup(parameter.getDispCtgId(), Joiner.on("@").join(ctgLst)));
		return result;
	}

	private List<Category> getVirtualCategoryGroup(String selectedCtgId, String ctgLst) {
		// Token 형태 : ctgId^ctgNms^ctgCount@ctgId^ctgNms^ctgCount
		List<Category> ctgList = new ArrayList<Category>();
		List<Category> ctgList1 = new ArrayList<Category>();
		List<Category> ctgList2 = new ArrayList<Category>();
		for (StringTokenizer grpToken = new StringTokenizer(ctgLst, "@"); grpToken.hasMoreTokens(); ) {
			String ctgElement = grpToken.nextToken();
			if (ctgElement != null && !ctgElement.equals("")) {
				int i = 0;
				Category c = new Category();
				String ctgType = "";
				String ctgCls = "";
				String ctgLv = "";
				for (StringTokenizer ctgToken = new StringTokenizer(ctgElement, "\\^"); ctgToken.hasMoreTokens(); ) {
					String ctg = ctgToken.nextToken();
					if (ctg != null && !ctg.equals("")) {
						// CTG_ID
						if (i == 0) {
							c.setCtgId(ctg);
						}
						// CTGNM__CTGLV__SITE__CLS__TYPE__PRIOR_DISP_CTG_ID
						else if (i == 1) {
							String[] ctgNms = ctg.split("\\__");
							if (ctgNms != null && ctgNms.length > 4) {
								ctgLv = ctgNms[1];
								c.setCtgNm(ctgNms[0].replaceAll("\\++", " "));
								c.setCtgLevel(Integer.parseInt(ctgLv));
								c.setSiteNo(ctgNms[2]);
								c.setPriorCtgId(ctgNms[5]);
								ctgCls = ctgNms[3];
								ctgType = ctgNms[4];
							} else {
								System.out.println("null founded" + ctg);
							}
						}
						// ITEM_COUNT
						else if (i == 2) {
							c.setCtgItemCount(Integer.parseInt(ctg));
						}
					}
					if (i > 0 && i % 2 == 0) {
						i = 0;
					} else i++;
				}
				// 현재의 사이트와 동일할 경우에만 add
				if (ctgLv.equals("3") && selectedCtgId.equals(c.getCtgId())) {
					ctgList1.add(c);
				} else if (ctgLv.equals("4") && selectedCtgId.equals(c.getPriorCtgId())) {
					ctgList2.add(c);
				}
			}
		}
		if (ctgList1 != null && ctgList1.size() > 0 && ctgList2 != null && ctgList2.size() > 0) {
			// CATEGORY LEVEL2 ID 순 SORT
			Collections.sort(ctgList2, new Comparator<Category>() {
				public int compare(Category c1, Category c2) {
					String p1 = c1.getCtgId();
					String p2 = c2.getCtgId();
					return (p1).compareTo(p2);
				}
			});

			ctgList.addAll(ctgList1);
			ctgList.addAll(ctgList2);

		}
		return ctgList;
	}
}
