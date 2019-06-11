package ssg.search.collection.brand;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.BrandMaster;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

public class BrandMasterCollection implements Collection, Prefixable, Pageable{
	public String getCollectionName(Parameter parameter) {
		return "brandmaster";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "brandmaster";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"BRAND_ID",
			"BRAND_NM",
			"BRAND_SRCHWD_NM",
			"BRAND_DESC",
			"BRAND_CNCEP_CNTT",
			"USE_YN",
			"BRAND_SYNC_YN",
			"BRAND_REG_MAIN_DIV_CD"
		};
	}

	public String[] getSearchField(Parameter parameter){
		return new String[]{
			"BRAND_ID",
			"BRAND_NM",
			"BRAND_SRCHWD_NM"
		};
	}

	public Call<Info> getPrefix(){
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				StringBuilder sb = new StringBuilder();
				for(Iterator <Prefixes> iter = ImmutableSet.of(
					  Prefixes.USE_YN_PREFIX
				).iterator();iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(), 1);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<BrandMaster> brandMasterList = Lists.newArrayList();
		BrandMaster brandMaster;
		int count = search.w3GetResultCount(name);
		//브랜드필터 노출여부 판단을위해 추가(검색어와 같은 브랜드명 있을경우 미노출 처리)
		String brandFilterDispYn = "Y";
		for(int i=0;i<count;i++){
			brandMaster = new BrandMaster();
			brandMaster.setBrandId(search.w3GetField(name, "BRAND_ID", i));
			brandMaster.setBrandNm(search.w3GetField(name, "BRAND_NM", i));
			brandMaster.setBrandSrchwdNm(search.w3GetField(name, "BRAND_SRCHWD_NM", i));
			brandMaster.setBrandCncepCntt(search.w3GetField(name, "BRAND_CNCEP_CNTT", i));
			brandMaster.setBrandDesc(search.w3GetField(name, "BRAND_DESC", i));
			brandMaster.setUseYn(search.w3GetField(name, "USE_YN", i));
			brandMaster.setBrandSyncYn(search.w3GetField(name, "BRAND_SYNC_YN", i));
			brandMaster.setBrandRegMainDivCd(search.w3GetField(name, "BRAND_REG_MAIN_DIV_CD", i));
			brandMasterList.add(brandMaster);
			
			if(StringUtils.defaultIfEmpty(parameter.getQuery(), "").equals(brandMaster.getBrandNm())) {
			    brandFilterDispYn = "N";
			}
		}
		result.setBrandMasterList(brandMasterList);
		result.setBrandMasterCount(search.w3GetResultTotalCount(name));
		result.setBrandFilterDispYn(brandFilterDispYn);
		return result;
	}

	@Override
	public Call<Info> getPage() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0")){
					strCount = "5";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
}
