package ssg.search.collection.search;

import QueryAPI510.Search;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.constant.Prefixes;
import ssg.search.function.CategoryGroupable;
import ssg.search.function.Prefixable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Category;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.*;

/**
 * Created by 131544 on 2016-12-21.
 */
public class BookGroupCategoryCollection extends BookCategoryCollection implements Collection, CategoryGroupable, Prefixable {
	private String lctgId = "";
	private String lctgNm = "";

	private String mctgId = "";
	private String mctgNm = "";

	private String sctgId = "";
	private String sctgNm = "";

	private String dctgId = "";
	private String dctgNm = "";

	private final Logger logger = LoggerFactory.getLogger(ItemGroupCategoryCollection.class);
	private final Comparator<Category> itemCountComparator = new Comparator<Category>() {
		@Override
		public int compare(Category c1, Category c2) {
			if (c1.getRecomYn().equals("Y")){
				return -1;
			} else if(c2.getRecomYn().equals("Y")){
				return 1;
			} else if (c1.getCtgItemCount() > c2.getCtgItemCount()) {
				return -1;
			} else if (c1.getCtgItemCount() < c2.getCtgItemCount()) {
				return 1;
			} else {
				return 0;
			}
		}
	};

	@Override
	public Call<Info> getPrefix() {
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder sb = new StringBuilder();
				for(Iterator<Prefixes> iter = ImmutableSet.of(
						Prefixes.SRCH_PREFIX
						, Prefixes.SALESTR_LST_GROUP
						, Prefixes.MBR_CO_TYPE
						, Prefixes.DEVICE_CD
						, Prefixes.SCOM_EXPSR_YN
						, Prefixes.SRCH_CTG_PREFIX
				).iterator(); iter.hasNext();){
					sb.append(iter.next().getPrefix(parameter));
				}
				return new Info(sb.toString(),1);
			}
		};
	}

	@Override
	public Call<Info> getCategoryGroup() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				return CollectionUtils.getCategoryGroupByLevel(parameter);
			}
		};
	}
	/**
	 * 카테고리 정보 SET
	 * @param level
	 * @param next
	 * @param count
	 * @param name
	 * @param strCtgIdxNm
	 * @param search
	 * @return
	 */
	private List<Category> getCategory(int level, int next, int count, String name, String strCtgIdxNm, Search search){
		Set<String> childCtgSet     = Sets.newHashSet();
		List<Category> ctgList      = Lists.newArrayList();
		if(next>0){
			int nextCount = search.w3GetCategoryCount(name, strCtgIdxNm, next);
			for(int i=0;i<nextCount;i++){
				String tgtCtgInfo = search.w3GetCategoryName(name, strCtgIdxNm, next, i);
				childCtgSet.add(tgtCtgInfo.split(":")[level-1]);
			}
		}
		for(int i=0;i<count;i++){
			String ctgInfo      = search.w3GetCategoryName(name, strCtgIdxNm, level, i);
			String[] ctgToken   = ctgInfo.split(":");
			Category category = new Category();
			if(level>0){
				String[] ctgs = ctgToken[level-1].split("\\^");
				category.setSiteNo(ctgs[0]);
				category.setCtgId(ctgs[1]);
				// 부모가 존재하는 경우 부모 ctgId를 set
				if(level>1){
					category.setPriorCtgId(ctgToken[level-2].split("\\^")[1]);
				}
				category.setCtgNm(ctgs[2]);
				category.setCtgLevel(level);
				category.setCtgItemCount(search.w3GetDocumentCountInCategory(name, strCtgIdxNm, level, i));
				if(childCtgSet!=null && childCtgSet.contains(ctgToken[level-1])){
					category.setHasChild(true);
				}
				ctgList.add(category);
			}
		}
		return ctgList;
	}
	@Override
	public Result getResult(Search search, String name, Parameter parameter, Result result) {
		// result 에서 추천 카테고리 리스트를 가져온다.
		List<Map<String,String>> recomList = result.getRecomDispCategoryList();
		Set<String> recomLctgIdSet = Sets.newHashSet();
		Set<String> recomMctgIdSet = Sets.newHashSet();
		if(recomList!=null && recomList.size()>0){
			for(Map<String,String> rMap : recomList){
				recomLctgIdSet.add(rMap.get("DctgId"));
				recomMctgIdSet.add(rMap.get("MctgId"));
			}
		}
		String strSiteNo            = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
		String strCtgIdxNm          = strSiteNo.equals("6005")?"SCOM_DISP_CTG_IDX":"DISP_CTG_IDX";
		// 통합검색의 경우 2개 뎁스로 작업을 진행한다. ( 타겟이 ALL 이고 CTG_ID, CTG_LV 값이 없는 경우 )
		if( ( parameter.getTarget().equalsIgnoreCase("all") || parameter.getTarget().equalsIgnoreCase("book") || parameter.getTarget().equalsIgnoreCase("category")) && StringUtils.isEmpty(parameter.getCtgId()) && StringUtils.isEmpty(parameter.getCtgLv())){
			List<Category> ctgList = Lists.newArrayList();
			// LEVEL 1 을 먼저 선행하고,
			int level = 1;
			int next  = 2;
			int count = search.w3GetCategoryCount(name, strCtgIdxNm, level);
			List<Category> ctgLv1List = this.getCategory(level, next, count, name, strCtgIdxNm, search);
			// LEVEL 2 를 실행
			level = 2;
			next  = 3;
			count = search.w3GetCategoryCount(name, strCtgIdxNm, level);
			List<Category> ctgLv2List = this.getCategory(level, next, count, name, strCtgIdxNm, search);
			// 결과가 정상이라면
			if(ctgLv1List!=null && ctgLv1List.size()>0){
				for(int i=0;i<ctgLv1List.size();i++){
					List<Category> childCtgList = Lists.newArrayList();
					Category c1 = ctgLv1List.get(i);
					String strCtgId = c1.getCtgId();
					for(int j=0;j<ctgLv2List.size();j++){
						Category c2 = ctgLv2List.get(j);
						if(strCtgId.equals(c2.getPriorCtgId())){
							if(recomMctgIdSet!=null && recomMctgIdSet.contains(c2.getCtgId())){
								c2.setRecomYn("Y");
							}
							childCtgList.add(c2);
						}
					}
					// childCtgList Sort
					if(childCtgList!=null && childCtgList.size()>0)Collections.sort(childCtgList, itemCountComparator);
					c1.setChildCategoryList(childCtgList);
					if(recomLctgIdSet!=null && recomLctgIdSet.contains(c1.getCtgId())){
						c1.setRecomYn("Y");
					}
					ctgList.add(c1);
				}
				// CtgList Sort
				if(ctgList!=null && ctgList.size()>0)Collections.sort(ctgList, itemCountComparator);
				result.setCategoryList(ctgList);
			}
		}
		// 카테고리 타겟으로 실행하는 경우
		else{
			int level = CollectionUtils.getCategoryCurrentLevel(parameter);
			int next  = CollectionUtils.getCategoryNextLevel(parameter);
			int count = search.w3GetCategoryCount(name, strCtgIdxNm, level);
			// 다다음 뎁스의 카테고리 정보를 이용해서 자식여부를 판별하기 위한 다음 뎁스 카테고리 아이디를 미리 매핑해둔다 ( 1, 2, 3 (자식여부Y) 레벨 클릭시에만 사용 )
			List<Category> ctgResultList = this.getCategory(level, next, count, name, strCtgIdxNm, search);
			List<Category> ctgList = Lists.newArrayList();
			if(ctgList!=null){
				// 중카의 경우 추천 카테고리 여부 매핑
				if(level == 2){
					for(Category c : ctgResultList){
						if(recomMctgIdSet!=null && recomMctgIdSet.contains(c.getCtgId())){
							c.setRecomYn("Y");
						}
						ctgList.add(c);
					}
				}else{
					ctgList = ctgResultList;
				}
				// CtgList Sort
				if(ctgList!=null && ctgList.size()>0)Collections.sort(ctgList, itemCountComparator);
				result.setCategoryList(ctgList);
				// 현재 뎁스가져오기
				String ctgLv = parameter.getCtgLv();
				String ctgId = parameter.getCtgId();
				for(int i=0;i<search.w3GetCategoryCount(name, strCtgIdxNm, level);i++){
					String ctgInfo      = search.w3GetCategoryName(name, strCtgIdxNm, level, i);
					String[] ctgInfos = ctgInfo.split(":");
					switch(ctgInfos.length){
						case 4:
							String[] cTokens = ctgInfos[3].split("\\^");
							if(ctgLv.equals("4")){
								if(ctgId.equals(cTokens[1])){
									dctgId = cTokens[1];
									dctgNm = cTokens[2];
								}
							}
						case 3:
							cTokens = ctgInfos[2].split("\\^");
							if(ctgLv.equals("3")){
								if(ctgId.equals(cTokens[1])){
									sctgId = cTokens[1];
									sctgNm = cTokens[2];
								}
							}else{
								sctgId = cTokens[1];
								sctgNm = cTokens[2];
							}
						case 2:
							cTokens = ctgInfos[1].split("\\^");
							mctgId = cTokens[1];
							mctgNm = cTokens[2];
						case 1:
							cTokens = ctgInfos[0].split("\\^");
							lctgId = cTokens[1];
							lctgNm = cTokens[2];
					}
				}
				if(ctgLv.equals("1")){
					result.setLctgId(lctgId);
					result.setLctgNm(lctgNm);
				}else if(ctgLv.equals("2")){
					result.setLctgId(lctgId);
					result.setLctgNm(lctgNm);
					result.setMctgId(mctgId);
					result.setMctgNm(mctgNm);
				}else if(ctgLv.equals("3")){
					result.setLctgId(lctgId);
					result.setLctgNm(lctgNm);
					result.setMctgId(mctgId);
					result.setMctgNm(mctgNm);
					result.setSctgId(sctgId);
					result.setSctgNm(sctgNm);
				}else if(ctgLv.equals("4")){
					result.setLctgId(lctgId);
					result.setLctgNm(lctgNm);
					result.setLctgId(lctgId);
					result.setLctgNm(lctgNm);
					result.setMctgId(mctgId);
					result.setMctgNm(mctgNm);
					result.setSctgId(sctgId);
					result.setSctgNm(sctgNm);
					result.setDctgId(dctgId);
					result.setDctgNm(dctgNm);
				}
			}
		}
		return result;
	}
}