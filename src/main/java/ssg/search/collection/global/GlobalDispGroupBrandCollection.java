package ssg.search.collection.global;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.collection.base.DispGroupCollection;
import ssg.search.constant.Prefixes;
import ssg.search.function.Groupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.Iterator;

public class GlobalDispGroupBrandCollection extends DispGroupCollection implements Collection, Prefixable, Groupable{
	@Override
	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_GLOBAL
					, Prefixes.SALESTR_LST_GROUP
					, Prefixes.BRAND_ID
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(),1);
			}
		};
	}
	@Override
	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("DISP_CTG_LST");
			}
		};
	}
	
	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		String ctgLst = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "DISP_CTG_LST"), "");
		if(!ctgLst.equals("")){
	        result.setCategoryList(ResultUtils.getGlobalCategoryGroup(parameter.getSiteNo(), parameter.getDispCtgId(), ctgLst, parameter.getTarget()));
		}
		return result;
	}
}
