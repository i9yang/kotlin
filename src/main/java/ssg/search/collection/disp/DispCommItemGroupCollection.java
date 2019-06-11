package ssg.search.collection.disp;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.collection.base.DispGroupCollection;
import ssg.search.function.Groupable;
import ssg.search.function.PropertyGroupable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Brand;
import ssg.search.result.Prc;
import ssg.search.result.Result;
import ssg.search.result.Size;
import ssg.search.util.ResultUtils;

import java.util.List;

public class DispCommItemGroupCollection extends DispGroupCollection implements Groupable, PropertyGroupable{
	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("BRAND_ID,SIZE_LST");
			}
		};
	}
	
	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		// 가격 그루핑 결과
		int propct = search.w3GetCountPropertyGroup(name);
		int min = search.w3GetMinValuePropertyGroup(name);
		int max = search.w3GetMaxValuePropertyGroup(name);
		List<Prc> prcGroupList = Lists.newArrayList();
		Prc prc;
		
		for(int i=0;i<propct;i++){
		    prc = new Prc();
		    prc.setMinPrc(search.w3GetMinValueInPropertyGroup(name,i));
		    prc.setMaxPrc(search.w3GetMaxValueInPropertyGroup(name,i));
		    prcGroupList.add(prc);
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
		// DISP_GROUP은 전시카테고리 전체 사이즈 그룹핑 결과를 사용
        String strSizeInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SIZE_LST"),"");
        if(!strSizeInfo.equals("")){
        	List<Size> sizeList =  ResultUtils.getSizeGroup(strSizeInfo);
        	result.setSizeList(sizeList);
        }
		return result;
	}
	
	public Call<Info> getPropertyGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("SELLPRC");
			}
		};
	}
}
