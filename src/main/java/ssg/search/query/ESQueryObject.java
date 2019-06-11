package ssg.search.query;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Sort;

import java.util.List;

public class ESQueryObject {
	private int from;
	private int size;
	private List<Sort> sort;
	private String aggs;
	private List<String> queryList;
	private List<String> orQueryList;
	private String filter;
	private String strQuery;

	private Joiner joiner = Joiner.on(" AND ").skipNulls();
	private Joiner orJoiner = Joiner.on(" OR ").skipNulls();

	private String collectionName;
	private String collectionAliasName;

	public String getCollectionAliasName() {
		return collectionAliasName;
	}

	public void setCollectionAliasName(String collectionAliasName) {
		this.collectionAliasName = collectionAliasName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getStrQuery() {
		return strQuery;
	}

	public void setStrQuery(String strQuery) {
		this.strQuery = strQuery;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public List<Sort> getSort() {
		return sort;
	}

	public void setSort(List<Sort> sort) {
		this.sort = sort;
	}

	public String getAggs() {
		return aggs;
	}

	public void setAggs(String aggs) {
		this.aggs = aggs;
	}

	public List<String> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<String> queryList) {
		this.queryList = queryList;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setOrQueryList(List<String> orQueryList) {
		this.orQueryList = orQueryList;
	}

	public List<String> getOrQueryList() {
		return orQueryList;
	}

	public String getJsonQuery() {
		JsonObject obj = new JsonObject();
		obj.addProperty("from", from);
		obj.addProperty("size", size);

		if (sort != null) {
			JsonArray sortList = new JsonArray();
			for (Sort s : sort) {
				if(s == null) continue;

				if(StringUtils.equals(s.getSortName(), "DISP_CTG_ORDR.DISP_ORDR")) {
					JsonObject sortObj = new JsonObject();
					JsonObject dispOrdrObj = new JsonObject();
					JsonObject nestedFilterObj = new JsonObject();
					JsonObject termObj = new JsonObject();

					List<String> dispCtgLst = FluentIterable.from(queryList).filter(new Predicate<String>() {
						@Override
						public boolean apply(String s) {
							return StringUtils.contains(s, "DISP_CTG_LST");
						}
					}).toList();

					if(dispCtgLst.size() > 0 ) {
						String dispCtgIds = dispCtgLst.get(0).replaceAll(".*DISP_CTG_LST:\\((.*?)\\).*", "$1");
						termObj.addProperty("DISP_CTG_ORDR.DISP_CTG_ID", dispCtgIds);
						nestedFilterObj.add("term", termObj);
						dispOrdrObj.addProperty("order", (s.getOperator() == 1 ? "desc" : "asc"));
						dispOrdrObj.addProperty("nested_path", "DISP_CTG_ORDR");
						dispOrdrObj.add("nested_filter", nestedFilterObj);
						sortObj.add("DISP_CTG_ORDR.DISP_ORDR", dispOrdrObj);
						sortList.add(sortObj);
					}
				} else {
					JsonObject sortObj = new JsonObject();
					sortObj.addProperty(s.getSortName(), (s.getOperator() == 1 ? "desc" : "asc"));
					sortList.add(sortObj);
				}
			}

			obj.add("sortVo", sortList);
		}

		if (queryList != null && queryList.size() > 0) {
			JsonObject queryObj = new JsonObject();
			JsonObject queryStringObj = new JsonObject();
			JsonObject boolObj = new JsonObject();
			JsonObject filterObj = new JsonObject();

			if(orQueryList != null && orQueryList.size() > 0) {
				String orQuery = orJoiner.join(orQueryList);
				queryList.add("(" + orQuery + ")");
			}


			if (StringUtils.isNotEmpty(strQuery)) {
				// 형태소 상품명, 브랜드명 매치
//				JsonObject multiMatchObj = new JsonObject();
//				JsonArray fields = new JsonArray();
//				JsonObject mustObj = new JsonObject();
//				fields.add("ITEM_NM");
//				fields.add("BRAND_NM");
//
//				multiMatchObj.addProperty("query",strQuery);
//				multiMatchObj.addProperty("operator","or");
//				multiMatchObj.add("fields",fields);
//				mustObj.add("multi_match",multiMatchObj);
//				boolObj.add("must",mustObj);

				// 상품 ID 매치
//				JsonObject multiMatchObj = new JsonObject();
//				JsonArray fields = new JsonArray();
//				JsonObject mustObj = new JsonObject();
//				fields.add("ITEM_ID");
//
//				multiMatchObj.addProperty("query",strQuery);
//				multiMatchObj.add("fields",fields);
//				mustObj.add("multi_match",multiMatchObj);
//				boolObj.add("must",mustObj);

				// 상품명 like
//				strQuery = strQuery.replaceAll("[\\?\\*\"()]", "");
//				queryList.add("(ITEM_NM:(*" + strQuery + "*) OR BRAND_NM:(*" + strQuery + "*) OR MDL_NM:(*" + strQuery + "*))");
			}

			queryStringObj.addProperty("query", joiner.join(queryList));
			filterObj.add("query_string", queryStringObj);
			boolObj.add("filterVo",filterObj);

			queryObj.add("bool",boolObj);
			obj.add("query", queryObj);
		}

		if (StringUtils.isNotEmpty(aggs)) {
			if(StringUtils.equals(aggs, "SELLPRC")) {
				JsonObject aggObj = new JsonObject();
				JsonObject maxPrcObj = new JsonObject();
				JsonObject minPrcObj = new JsonObject();
				JsonObject prcObj = new JsonObject();

				prcObj.addProperty("field", aggs);
				maxPrcObj.add("max", prcObj);
				minPrcObj.add("min", prcObj);

				aggObj.add("minPrc", minPrcObj);
				aggObj.add("maxPrc", maxPrcObj);

				obj.add("aggs", aggObj);
			} else if(StringUtils.contains(aggs,"|")) {
				String[] aggArray = aggs.split("\\|");
				List<JsonObject> objList = Lists.newArrayList();
				for (String agg : aggArray) {
					objList.add(getAggsJson(agg));
				}

				for(int i = objList.size()-1 ; i > 0; i--) {
					objList.get(i-1).getAsJsonObject(aggArray[i-1]).add("aggs", objList.get(i));
				}

				obj.add("aggs", objList.get(0));
			} else {
				String[] aggArray = aggs.split(",");
				JsonObject aggObj = new JsonObject();
				for (String agg : aggArray) {
					JsonObject nameObj = new JsonObject();
					JsonObject termsObj = new JsonObject();

					termsObj.addProperty("field", agg);
					termsObj.addProperty("size", 9999);
					nameObj.add("terms", termsObj);

					if (StringUtils.equals(agg, "SIZE_LST")) {
						JsonObject orderObj = new JsonObject();
						orderObj.addProperty("_key", "asc");
						termsObj.add("order", orderObj);
					}

					aggObj.add(agg, nameObj);
				}

				obj.add("aggs", aggObj);
			}
		}

		return new GsonBuilder().create().toJson(obj);
	}

	private JsonObject getAggsJson(String agg){
		JsonObject aggObj = new JsonObject();
		JsonObject nameObj = new JsonObject();
		JsonObject termsObj = new JsonObject();

		termsObj.addProperty("field", agg);
		termsObj.addProperty("size", 9999);
		nameObj.add("terms", termsObj);

		aggObj.add(agg, nameObj);

		return aggObj;
	}
}
