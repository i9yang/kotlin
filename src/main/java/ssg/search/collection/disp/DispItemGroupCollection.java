package ssg.search.collection.disp;

import QueryAPI510.Search;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.collection.base.DispGroupCollection;
import ssg.search.function.Groupable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.Size;
import ssg.search.util.ResultUtils;

import java.util.List;

public class DispItemGroupCollection extends DispGroupCollection implements Groupable{
	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("SIZE_LST");
			}
		};
	}
	
	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		// DISP_GROUP은 전시카테고리 전체 사이즈 그룹핑 결과를 사용
        String strSizeInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "SIZE_LST"),"");
        if(!strSizeInfo.equals("")){
        	List<Size> sizeList =  ResultUtils.getSizeGroup(strSizeInfo);
        	result.setSizeList(sizeList);
        }
		return result;
	}
}
