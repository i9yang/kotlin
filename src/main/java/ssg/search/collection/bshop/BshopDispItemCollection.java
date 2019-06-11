package ssg.search.collection.bshop;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.collection.base.DispItemCollection;
import ssg.search.constant.BenefitFilters;
import ssg.search.constant.Prefixes;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;
import ssg.search.util.ResultUtils;

import java.util.Iterator;

public class BshopDispItemCollection extends DispItemCollection implements Prefixable{
	@Override
	public String getCollectionAliasName(Parameter parameter){
		return "bshop";
	}
	
	public Call<Info> getPrefix(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX_BSHOP
					, Prefixes.FILTER_SITE_NO
					, Prefixes.DISP_CTG_LST
					, Prefixes.SALESTR_LST
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.BSHOP_ID
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				String strCls = StringUtils.defaultIfEmpty(parameter.getCls(), "");
				String strBenefit = StringUtils.defaultIfEmpty(parameter.getBenefit(), "");
				String strFilter = StringUtils.defaultIfEmpty(parameter.getFilter(), "");
				if(strCls.equalsIgnoreCase("emart")){
					sb.append("<").append(BenefitFilters.EMART.getField()).append(":contains:").append(BenefitFilters.EMART.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("emartonline")){
					sb.append("<").append(BenefitFilters.EMARTONLINE.getField()).append(":contains:").append(BenefitFilters.EMARTONLINE.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("department")){
					sb.append("<").append(BenefitFilters.DEPARTMENT.getField()).append(":contains:").append(BenefitFilters.DEPARTMENT.getPrefix()).append(">");
				}
				if(strCls.equalsIgnoreCase("shinsegae")){
					sb.append("<").append(BenefitFilters.SHINSEGAE.getField()).append(":contains:").append(BenefitFilters.SHINSEGAE.getPrefix()).append(">");
				}
				if(strBenefit.equalsIgnoreCase("free")){
					sb.append("<").append(BenefitFilters.FREE.getField()).append(":contains:").append(BenefitFilters.FREE.getPrefix()).append(">");
				}
				if(strBenefit.equalsIgnoreCase("prize")){
					sb.append("<").append(BenefitFilters.PRIZE.getField()).append(":contains:").append(BenefitFilters.PRIZE.getPrefix()).append(">");
				}
				if(strFilter.equalsIgnoreCase("free")){
					sb.append("<").append(BenefitFilters.FREE.getField()).append(":contains:").append(BenefitFilters.FREE.getPrefix()).append(">");
				}
				if(strFilter.equalsIgnoreCase("news")){
					sb.append("<").append(BenefitFilters.NEWS.getField()).append(":contains:").append(BenefitFilters.NEWS.getPrefix()).append(">");
				}
				if(strFilter.equalsIgnoreCase("obanjang|spprice")){
					sb.append("<")
						.append(BenefitFilters.OBANJANG.getField())
						.append(":contains:")
						.append(BenefitFilters.OBANJANG.getPrefix())
						.append("|")
						.append(BenefitFilters.SP_PRICE.getPrefix())
						.append(">");
				}
				String shppPrefix = CollectionUtils.getShppPrefix(parameter);
				if(shppPrefix!=null && !shppPrefix.equals("")){
					sb.append(shppPrefix);
				}
				
				return new Info(sb.toString(), 1);
			}
		};
	}
	
	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		return ResultUtils.getItemResult(name, parameter, result, search);
	}
}
