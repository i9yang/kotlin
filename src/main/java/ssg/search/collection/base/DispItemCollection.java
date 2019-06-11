package ssg.search.collection.base;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Sorts;
import ssg.search.function.Pageable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.List;

public class DispItemCollection implements Collection, Sortable, Pageable{
	public String getCollectionName(Parameter parameter) {
		return "item";
	}

	public String getCollectionAliasName(Parameter parameter) {
        return "book";
	}

	public String[] getDocumentField(Parameter parameter){
		// DISP COLLECTION 은 상품의 META DATA를 리턴
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
			"SHPP_MTHD_CD"
		};
	}
	
	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"ITEM_ID"
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
				} catch(IllegalArgumentException e){
					sorts 			= Sorts.BEST;
				}
				
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
				}else if(sorts.equals(Sorts.SCR)){
					sortList.add(sorts.getSort(parameter));
					sortList.add(Sorts.DISP_BEST.getSort(parameter));
					sortList.add(Sorts.REGDT.getSort(parameter));
					sortList.add(Sorts.PRCASC.getSort(parameter));
				}
				return new Info(sortList);
			}
		};
	}
	
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "40";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		// 상품 Meta Set 결과
		return ResultUtils.getItemResult(name, parameter, result, search);
	}
}