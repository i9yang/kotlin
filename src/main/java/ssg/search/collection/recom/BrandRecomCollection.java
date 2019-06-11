package ssg.search.collection.recom;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.function.Pageable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Brand;
import ssg.search.result.Result;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by 131544 on 2016-11-03.
 */
public class BrandRecomCollection implements Collection, Pageable{
	private final Logger logger = LoggerFactory.getLogger(BrandRecomCollection.class);

	@Override
	public String getCollectionName(Parameter parameter) {
		return "brandrecom";
	}

	@Override
	public String getCollectionAliasName(Parameter parameter) {
		return "brandrecom";
	}

	@Override
	public String[] getDocumentField(Parameter parameter) {
		return new String[]{"BRAND_IDS", "BRAND_NMS"};
	}

	@Override
	public String[] getSearchField(Parameter parameter) {
		return new String[]{"QUERY"};
	}

	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		String target						= parameter.getTarget();
		String sort							= StringUtils.defaultIfEmpty(parameter.getSort(), "best");
		List<Brand> brandResultList 		= result.getBrandList();
		// 가나다순 정렬 요청시
		// 한글 -> 영문 -> 특수문자 순으로 정렬요청
		if((target.equalsIgnoreCase("mobile_dtl_brand") || target.equalsIgnoreCase("mobile_brand_omni_dtl")) && sort.equalsIgnoreCase("word")){
			if(brandResultList!=null && brandResultList.size()>0){
				List<Brand> engList 	= Lists.newArrayList();
				List<Brand> korList 	= Lists.newArrayList();
				List<Brand> spList 		= Lists.newArrayList();
				List<Brand> resultList 	= Lists.newArrayList();
				// 한글/영문/그외 별로 따로 담는다.
				for (Brand b : brandResultList){
					if(StringUtils.isNotBlank(b.getBrandNm())){
						String brandNm1 = b.getBrandNm().substring(0,1);
						if(Pattern.matches("^[ㄱ-ㅎ가-힣]*$", brandNm1)){
							korList.add(b);
						}else if(Pattern.matches("^[a-zA-Z]*$", brandNm1)){
							engList.add(b);
						}else{
							spList.add(b);
						}
					}
				}
				// Comparator
				Comparator<Brand> brandComparator = new Comparator<Brand>() {
					@Override
					public int compare(Brand o1, Brand o2) {
						return o1.getBrandNm().compareTo(o2.getBrandNm());
					}
				};

				if(korList!=null && korList.size()>0){
					Collections.sort(korList, brandComparator);
					resultList.addAll(korList);
				}
				if(engList!=null && engList.size()>0){
					Collections.sort(engList, brandComparator);
					resultList.addAll(engList);
				}
				if(spList!=null && spList.size()>0){
					Collections.sort(spList, brandComparator);
					resultList.addAll(spList);
				}
				result.setBrandList(resultList);
			}
		}else if(search.w3GetResultTotalCount(name)>0){
			// Brand Result 를 추천결과와 비교하기 위해 Map에 결과를 담는다.
			List<Brand> newBrandList 			= new ArrayList<Brand>();
			Map<String,Brand> brandIdMap 		= new HashMap<String, Brand>();
			if(brandResultList!=null && brandResultList.size()>0){
				for (Brand brand : brandResultList){
					brandIdMap.put(brand.getBrandId(), brand);
				}
			}
			String brandIds = search.w3GetField(name, "BRAND_IDS", 0);
			// 브랜드가 1개 이상
			if(brandIds.indexOf(",")>-1){
				String[] arrBrandId = brandIds.split(",");
				for (int i=0;i<arrBrandId.length;i++){
					// 추천결과에 나온 브랜드가 Brand 그룹핑 결과에 존재하는 경우
					// 새로운 브랜드 그룹핑을 시작
					if(brandIdMap.containsKey(arrBrandId[i])){
						Brand brand = brandIdMap.get(arrBrandId[i]);
						if(i<3){
							brand.setRecomYn("Y");
						}
						newBrandList.add(brand);
						// 나중에 Merge 를 위해 신규로 추가한 브랜드는 원본에서 삭제한다.
						for(int j=0;j<brandResultList.size();j++){
							if(arrBrandId[i].equals(brandResultList.get(j).getBrandId())){
								brandResultList.remove(j);
							}
						}
					}
				}
			}else{
				if(brandIdMap.containsKey(brandIds)){
					Brand brand = brandIdMap.get(brandIds);
					brand.setRecomYn("Y");
					newBrandList.add(brand);
					// 나중에 Merge 를 위해 신규로 추가한 브랜드는 원본에서 삭제한다.
					for(int j=0;j<brandResultList.size();j++){
						if(brandIds.equals(brandResultList.get(j).getBrandId())){
							brandResultList.remove(j);
						}
					}
				}
			}
			if(newBrandList.size()>0){
				newBrandList.addAll(newBrandList.size(), brandResultList);
				result.setBrandList(newBrandList);
			}
		}
		return result;
	}

	@Override
	public Call<Info> getPage() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return new Info(0, 1);
			}
		};
	}
}
