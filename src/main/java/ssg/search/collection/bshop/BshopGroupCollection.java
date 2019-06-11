package ssg.search.collection.bshop;

import QueryAPI510.Search;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Info;
import ssg.search.function.Groupable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Bshop;
import ssg.search.result.Result;
import ssg.search.util.ResultUtils;

import java.util.List;

public class BshopGroupCollection extends BshopItemCollection implements Groupable{
	
	public String getCollectionAliasName(Parameter parameter){
		return "groupVo";
	}
	
	public Call<Info> getGroup(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				return new Info("BSHOPID_LST");
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
        String strBshopInfo = StringUtils.defaultIfEmpty(search.w3GetMultiGroupByResult(name, "BSHOPID_LST"),"");
        if(!strBshopInfo.equals("")){
        	List<Bshop> bshopRstList =  ResultUtils.getBshopGroup(strBshopInfo);
        	result.setBshopRstList(bshopRstList);
        }
		return result;
	}
}
