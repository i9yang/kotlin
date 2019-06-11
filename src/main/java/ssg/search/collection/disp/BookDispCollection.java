package ssg.search.collection.disp;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Cls;
import ssg.search.constant.Filters;
import ssg.search.constant.Prefixes;
import ssg.search.constant.Sorts;
import ssg.search.function.Filterable;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.Iterator;
import java.util.List;

public class BookDispCollection implements Collection, Prefixable, Filterable, Pageable, Sortable{

	public String getCollectionName(Parameter parameter) {
		return "book";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "book_disp";
	}

	public String[] getDocumentField(Parameter parameter) {
		return new String[]{
			"SITE_NO",
			"ITEM_ID",
			"ITEM_NM",
			"SELLPRC",
			"ITEM_REG_DIV_CD",
			"SHPP_TYPE_CD",
			"SHPP_TYPE_DTL_CD",
			"SALESTR_LST",
			"EXUS_ITEM_DIV_CD",
			"EXUS_ITEM_DTL_CD",
			"SHPP_MAIN_CD",
			"SHPP_MTHD_CD",
			"AUTHOR_NM",
			"TRLTPE_NM",
			"PUBSCO_NM",
			"FXPRC"
		};
	}

	public String[] getSearchField(Parameter parameter) {
		return new String[]{"ITEM_ID"};
	}
	
	public Call<Info> getPrefix(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					Prefixes.SRCH_PREFIX, Prefixes.FILTER_SITE_NO, Prefixes.DISP_CTG_LST, Prefixes.BRAND_ID,
					Prefixes.SALESTR_LST, Prefixes.DEVICE_CD, Prefixes.SCOM_EXPSR_YN, Prefixes.MBR_CO_TYPE
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				// 혜택 필터 ( 이마트 점포상품만 보기, 이마트 온라인상품 )
				String strCls = StringUtils.defaultIfEmpty(parameter.getCls(), "");
				if(strCls.equalsIgnoreCase("emart")){
					sb.append("<CLS:contains:").append(Cls.EMART.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("emartonline")){
					sb.append("<CLS:contains:").append(Cls.EMARTONLINE.getPrefix()).append(">");
				}
				// 상품 관련 필터 ( 매직 픽업, 퀵배송 )
				String shppPrefix = CollectionUtils.getShppPrefix(parameter);
				if(shppPrefix!=null && !shppPrefix.equals("")){
					sb.append(shppPrefix);
				}
				return new Info(sb.toString(), 1);
			}
		};
	}
	
	public Call<Info> getFilter(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info(Filters.PRC_FILTER.getFilter(parameter));
			}
		};
	}
	
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0")){
					if(strSiteNo.equals("6005")){
						strCount = "40";
					}else if(strSiteNo.equals("6004")){
						strCount = "40";
					}else if(strSiteNo.equals("6009")){
						strCount = "40";
					}else if(strSiteNo.equals("6001")){
						strCount = "40";
					}else if(strSiteNo.equals("6002")){
						strCount = "15";
					}
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
	
	public Call<Info> getSort() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				Sorts  	 	sorts 			= Sorts.BEST;
				String	 	strSort 		= StringUtils.defaultIfEmpty(parameter.getSort(), "best");
				List<Sort> 	sortList 		= Lists.newArrayList();
				try{
					sorts = Sorts.valueOf(strSort.toUpperCase());
				} catch(IllegalArgumentException e){}
				
				// 동점이 많기때문에 1~4 차 까지 정렬한다.
				if(sorts.equals(Sorts.BEST)){
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				}else if(sorts.equals(Sorts.PRCASC) || sorts.equals(Sorts.PRCDSC)){
					sortList.add(sorts.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
				}else if(sorts.equals(Sorts.CNT)){
					sortList.add(sorts.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				}else if(sorts.equals(Sorts.REGDT)){
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				}
				return new Info(sortList);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		// 상품 Meta Set 결과
		return ResultUtils.getItemResult(name, parameter, result, search);
	}
}
