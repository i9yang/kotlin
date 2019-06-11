package ssg.search.collection.bshop;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Collection;
import ssg.search.parameter.Parameter;
import ssg.search.result.Bshop;
import ssg.search.result.Result;

import java.util.List;

public class BshopCollection implements Collection{
	
	public String getCollectionName(Parameter parameter){
		return "bshop";
	}
	
	public String getCollectionAliasName(Parameter parameter){
		return "bshop";
	}
	
	public String getCollectionRealName(Parameter parameter) {
		return "bshop";
	}
	
	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"BSHOP_ID",
			"REP_BRAND_ID",
			"BSHOP_TITLE_NM",
			"BSHOP_ENG_TITLE_NM1",
			"BSHOP_ENG_TITLE_NM2",
			"IMG_FILE_NM"
		};
	}
	
	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"BSHOP_TITLE_NM",
			"BSHOP_ENG_TITLE_NM1",
			"BSHOP_ENG_TITLE_NM2"
		};
	}
	
	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result){

		int count = search.w3GetResultCount(name);
		List<Bshop> bshopList = Lists.newArrayList();
		Bshop bshop;
		
		for(int i=0;i<count;i++){
			bshop = new Bshop();
			bshop.setBshopId(search.w3GetField(name, "BSHOP_ID", i));
			bshop.setBshopTitleNm(search.w3GetField(name, "BSHOP_TITLE_NM", i));
			bshop.setBshopEngTitleNm1(search.w3GetField(name, "BSHOP_ENG_TITLE_NM1", i));
			bshop.setBshopEngTitleNm2(search.w3GetField(name, "BSHOP_ENG_TITLE_NM2", i));
			bshop.setRepBrandId(search.w3GetField(name, "REP_BRAND_ID", i));
			
			String imgFileNm = StringUtils.defaultIfEmpty(search.w3GetField(name, "IMG_FILE_NM", i), "");
            if(!imgFileNm.equals("")) imgFileNm = "http://static.ssgcdn.com"+ imgFileNm;
            bshop.setImgFileNm(imgFileNm);
            
			bshopList.add(bshop);
		}
		result.setBshopList(bshopList);
		result.setBshopCount(search.w3GetResultTotalCount(name));
		
		return result;
		
	}
}
