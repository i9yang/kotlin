package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.base.Sort;
import ssg.search.constant.Sorts;
import ssg.search.function.Pageable;
import ssg.search.function.Prefixable;
import ssg.search.function.Sortable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Result;
import ssg.search.result.Trecipe;
import ssg.search.util.CollectionUtils;

import java.util.List;

/**
 * #Use-on Collections.TRECIPE 
 * @author 123697
 *
 */
public class TrecipeCollection implements Collection, Prefixable, Sortable, Pageable{
	public String getCollectionName(Parameter parameter) {
		return "trecipe";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "trecipe";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"RECIPE_NO",
			"RECIPE_NM",
			"VOD_YN",
			"VOD_URL",
			"RECIPE_TAG",
			"TRECIPE_BEST_SCR",
			"TRECIPE_BRWS_CNT",
			"COOK_QNTY_CD",
			"COOK_DIFCLVL_CD",
			"COOK_KIND_CD",
			"RQRM_TIME_CD",
			"IMG_FILE_NM",
			"COOK_INGRD_NM",
			"COOK_DESC",
			"REG_DTS"
		};
	}
	public String[] getSearchField(Parameter parameter) {
		return new String[]{
			"RECIPE_NO",
			"RECIPE_NM",
			"COOK_KIND_CD",
			"RECIPE_TAG"
		};
	}
	
	public Call<Info> getPrefix(){
		return new Call<Info>(){
			public Info apply(Parameter parameter){
				StringBuilder prefix = new StringBuilder();
				String vodYn = StringUtils.defaultIfEmpty(parameter.getVodYn(), "");
				if(!vodYn.equals("")){
					prefix.append("<VOD_YN:contains:").append(vodYn).append(">");
				}
	            return new Info(prefix.toString(),1);
			}
		};
	}
	
	public Call<Info> getSort() {
		return new Call<Info>(){
			public Info apply(Parameter parameter) {
				Sorts 	sorts = Sorts.BEST;
				String strSort = StringUtils.defaultIfEmpty(parameter.getSort(), "best");
				List<Sort> sortList = Lists.newArrayList();
				try{
					sorts = Sorts.valueOf(strSort.toUpperCase());
				} catch(IllegalArgumentException e){}
				
				if(sorts.equals(Sorts.BEST)){
					sortList.add(Sorts.TRECIPE_BEST_SCR.getSort(parameter));
				}else if(sorts.equals(Sorts.REGDT)){
					sortList.add(Sorts.REG_DTS.getSort(parameter));
				}else{
					return new Info();
				}
				
				return new Info(sortList);
			}
		};
	}
	
	public Call<Info> getPage() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				String strTcount =  StringUtils.defaultIfEmpty(parameter.getCount(),  "0");
				// 더보기 없으니, 100개까지 한번에 내려줌(검색만)
				String strCount = "100";
				//검색 결과에서는 페이징 없이 무조건 100개지만, 전시에서는 40개씩
				if(strTarget.startsWith("trecipe") && !strTcount.equals("0")){
					strCount = strTcount;
				} else if (strTarget.equalsIgnoreCase("all")) {
					strPage = "1";
				}
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}

	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<Trecipe> trecipeList = Lists.newArrayList();
		Trecipe trecipe;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			trecipe = new Trecipe();
			trecipe.setRecipeNo(search.w3GetField(name, "RECIPE_NO", i));
			trecipe.setRecipeNm(search.w3GetField(name, "RECIPE_NM", i));
			trecipe.setVodYn(search.w3GetField(name, "VOD_YN", i));
			trecipe.setVodUrl(search.w3GetField(name, "VOD_URL", i));
			trecipe.setRecipeTag(search.w3GetField(name, "RECIPE_TAG", i));
			trecipe.setTrecipeBestScr(search.w3GetField(name, "TRECIPE_BEST_SCR", i));
			trecipe.setTrecipeBrwsCnt(search.w3GetField(name, "TRECIPE_BRWS_CNT", i));
			trecipe.setCookQntyCd(search.w3GetField(name, "COOK_QNTY_CD", i));
			trecipe.setCookDifclvlCd(search.w3GetField(name, "COOK_DIFCLVL_CD", i));
			trecipe.setCookKindCd(search.w3GetField(name, "COOK_KIND_CD", i));
			trecipe.setRqrmTimeCd(search.w3GetField(name, "RQRM_TIME_CD", i));
			trecipe.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
			trecipe.setCookIngrdNm(search.w3GetField(name, "COOK_INGRD_NM", i));
			trecipe.setCookDesc(search.w3GetField(name, "COOK_DESC", i));
			trecipeList.add(trecipe);
		}
		result.setTrecipeList(trecipeList);
		result.setTrecipeCount(search.w3GetResultTotalCount(name));
		return result;
	}
}
