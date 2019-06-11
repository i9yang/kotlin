package ssg.search.collection.es.disp;

import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import ssg.framework.domain.FrontUserInfo;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.collection.es.ESCollection;
import ssg.search.collection.es.ESQuery;
import ssg.search.constant.Shpp;
import ssg.search.constant.Sorts;
import ssg.search.function.Pageable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Item;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ESItem extends ESCollection implements Sortable, Pageable {
	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "ESItem";
	}

	@Override
	public Call<Info> getPage() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				String strPage = StringUtils.defaultIfEmpty(parameter.getPage(), "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				if (strCount.equals("") || strCount.equals("0")) {
					strCount = "40";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	@Override
	public Call<Info> getSort() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				String strSort = StringUtils.defaultIfEmpty(parameter.getSort(), "best");
				List<Sort> sortList = Lists.newArrayList();
				Sorts sorts;

				try {
					sorts = Sorts.valueOf(strSort.toUpperCase());
				} catch (IllegalArgumentException e) {
					sorts = Sorts.BEST;
				}

				if (sorts.equals(Sorts.BEST)) {
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				} else if (sorts.equals(Sorts.SALE)) {
					sortList.add(Sorts.SALE.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				} else if (sorts.equals(Sorts.PRCASC) || sorts.equals(Sorts.PRCDSC)) {
					sortList.add(sorts.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
				} else if (sorts.equals(Sorts.CNT)) {
					sortList.add(sorts.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				} else if (sorts.equals(Sorts.REGDT)) {
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				} else if (sorts.equals(Sorts.SCR)) {
					sortList.add(sorts.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				} else if (sorts.equals(Sorts.CTGORDR)) {
					sortList.add(sorts.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				}
				return new Info(sortList);
			}
		};
	}

	@Override
	public Result getResult(SearchResult searchResult, String name, Parameter parameter, Result result) {
		List<SearchResult.Hit<Item, Void>> list = searchResult.getHits(Item.class);
		List<Item> searchItemList = Lists.newArrayList();
		for (SearchResult.Hit h : list) {
			searchItemList.add((Item) h.source);
		}

		StringBuilder srchItemIds = new StringBuilder();
		processItemResult(parameter, searchItemList, srchItemIds);

		result.setItemList(searchItemList);
		result.setItemCount(searchResult.getTotal());
		result.setSrchItemIds(srchItemIds.toString());
		return result;
	}

	public void processItemResult(Parameter parameter, List<Item> itemList, StringBuilder srchItemIds) {
		for (Item item : itemList) {
			FrontUserInfo userInfo = parameter.getUserInfo();
			String strSiteNo = item.getSiteNo();
			String itemRegDivCd = item.getItemRegDivCd();
			String salestrLst = item.getSalestrLst();
			String strSalestrNo = "";

			if (strSiteNo.equals("6009") && itemRegDivCd.equals("30")) {
				if (salestrLst.indexOf("0001") > -1) {
					int idx = 0;
					salestrLst = salestrLst.replace("0001,", "").trim();
					salestrLst = salestrLst.replace("0001", "").trim();
					for (StringTokenizer st = new StringTokenizer(salestrLst, " "); st.hasMoreTokens(); ) {
						if (idx > 1) break;
						strSalestrNo = st.nextToken().replace("D", "");
						idx++;
					}
					item.setSellSalestrCnt(1);
				} else {
					strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("D", "");
					strSalestrNo = salestrLst.replaceAll("\\p{Space}", "").replace("Y", "");	//백화점쓱배송점포처리
					item.setSellSalestrCnt(1);
				}
			}

			// 그외 이/트/분 상품의 경우에는 거점점포의 데이터를 넣어준다.
			else if (strSiteNo.equals("6001") && itemRegDivCd.equals("20")) {
				strSalestrNo = userInfo.getEmSaleStrNo();
				item.setSellSalestrCnt(1);
			} else if (strSiteNo.equals("6002") && itemRegDivCd.equals("20")) {
				strSalestrNo = userInfo.getTrSaleStrNo();
				item.setSellSalestrCnt(1);
			} else if (strSiteNo.equals("6003") && itemRegDivCd.equals("20")) {
				strSalestrNo = userInfo.getBnSaleStrNo();
				item.setSellSalestrCnt(1);
			} else {
				strSalestrNo = "6005";
				item.setSellSalestrCnt(1);
			}

			item.setSalestrNo(strSalestrNo);

			StringBuilder ids = new StringBuilder().append(strSiteNo).append(":").append(item.getItemId()).append(":").append(strSalestrNo).append(",");
			if (srchItemIds.indexOf(ids.toString()) < 0) {
				srchItemIds.append(ids);
			}
		}
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
				, ESQuery.COLOR
				, ESQuery.DEVICE_CD
				, ESQuery.MBR_CO_TYPE
				, ESQuery.SCOM_EXPSR_YN
				, ESQuery.PRC_FILTER
				, ESQuery.SPL_VEN_ID
				, ESQuery.GRP_ADDR_ID
				, ESQuery.SHPPCST_ID
				, ESQuery.ITEM_SITE_NO
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
				queryMap.put("SHPP", "PICKU" + strPickuSalestr);
			} else {
				queryMap.put("SHPP", "PICKU");
			}
		}

		if (strShpp.indexOf("qshpp") > -1) {
			queryMap.put("SHPP", "QSHPP");
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
}
