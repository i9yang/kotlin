package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Groupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Brand;
import ssg.search.result.Prc;
import ssg.search.result.Result;
import ssg.search.result.Size;
import ssg.search.util.ResultUtils;

import java.util.Iterator;
import java.util.List;

public class ItemGroupCollection implements Collection, Prefixable, Groupable{
	Logger logger = LoggerFactory.getLogger(ssg.search.collection.search.ItemGroupCollection.class);

	public String getCollectionName(Parameter parameter) {
		return "item";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "groupVo";
	}

	public String[] getDocumentField(Parameter parameter) {
		return new String[]{
			"ITEM_ID", "SITE_NO"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"ITEM_ID",
			"ITEM_NM",
			"ITEM_SRCHWD_NM",
			"MDL_NM",
			"BRAND_NM",
			"TAG_NM",
			"GNRL_STD_DISP_CTG"
		};
	}
	
	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("BRAND_ID,SIZE_LST,SELLPRC_LST");
			}
		};
	}

	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.SRCH_PREFIX
					, Prefixes.FILTER_SITE_NO
					, Prefixes.SRCH_CTG_ITEM_PREFIX
					, Prefixes.SALESTR_LST_GROUP
					, Prefixes.DEVICE_CD
					, Prefixes.MBR_CO_TYPE
					, Prefixes.SPL_VEN_ID
					, Prefixes.LRNK_SPL_VEN_ID
					, Prefixes.SRCH_PSBL_YN
					, Prefixes.SCOM_EXPSR_YN
				).iterator();iter.hasNext();){
					String next = iter.next().getPrefix(parameter);
					if(StringUtils.isNotBlank(next)){
						sb.append(next);
					}
				}
				return new Info(sb.toString(),1);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter,Result result) {
		// 가격 그루핑 결과
		int min = 0;
		int max = 0;
		List<Prc> prcGroupList = Lists.newArrayList();
		
        String sellprcLst = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SELLPRC_LST"),"");
        
        logger.info("################### {}", sellprcLst);
        
        
        if(!sellprcLst.equals("")){
        	prcGroupList = ResultUtils.getSellprcGroupping(sellprcLst);
        	
        	if (prcGroupList.size() > 0) {
        		max = prcGroupList.get(prcGroupList.size() - 1).getMaxPrc();
        	}
        }
        
        result.setMinPrc(min);
		result.setMaxPrc(max);
		result.setPrcGroupList(prcGroupList);
		
		// 브랜드 그룹핑
		String strBrandInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "BRAND_ID"),"");
		if(!strBrandInfo.equals("")){
			List<Brand> brandList = ResultUtils.getBrandGroup(strBrandInfo);
			result.setBrandList(brandList);
		}
		// 전체 사이즈 그룹핑
        String strSizeInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SIZE_LST"),"");
        if(!strSizeInfo.equals("")){
        	List<Size> sizeList =  ResultUtils.getSizeGroup(strSizeInfo);
        	result.setSizeList(sizeList);
        }
		return result;
	}
	
}
