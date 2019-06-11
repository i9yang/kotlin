package ssg.search.collection.contents;

import QueryAPI510.Search;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import ssg.search.base.Call;
import ssg.search.base.Collection;
import ssg.search.base.Info;
import ssg.search.function.Pageable;
import ssg.search.parameter.Parameter;
import ssg.search.result.Recipe;
import ssg.search.result.Result;
import ssg.search.util.CollectionUtils;

import java.util.List;

/**
 * #Use-on Collections.RECIPE 
 * @author 131544
 *
 */
public class RecipeCollection implements Collection, Pageable{
	public String getCollectionName(Parameter parameter) {
		return "recipe";
	}

	public String getCollectionAliasName(Parameter parameter) {
		return "recipe";
	}

	public String[] getDocumentField(Parameter parameter){
		return new String[]{
			"RECIPE_NO",
			"RECIPE_NM",
			"COOK_CLS_CD",
			"IMG_PATH_NM",
			"IMG_FILE_NM",
			"IMG_RPLC_WORD_NM",
			"COOK_KEYWD_NM",
			"READY_TIME_CD",
			"RQRM_TIME_CD",
			"COOK_DIFCLVL_CD",
			"RECIPE_WRTPE_ID",
			"RECIPE_BRWS_CNT",
			"RECIPE_RCMD_CNT",
			"ETC_INGRD_NM",
			"COOK_TGT_AGEGRP_CD"
		};
	}
	public String[] getSearchField(Parameter parameter) {
		return new String[]{
			"RECIPE_NO",
			"RECIPE_NM",
			"COOK_KEYWD_NM",
			"ETC_INGRD_NM"
		};
	}
	
	public Call<Info> getPage() {
		return new Call<Info>() {
			public Info apply(Parameter parameter) {
				String strPage  = StringUtils.defaultIfEmpty(parameter.getPage(),  "1");
				String strCount = StringUtils.defaultIfEmpty(parameter.getCount(), "");
				String strSiteNo = StringUtils.defaultIfEmpty(parameter.getSiteNo(), "6005");
				String strTarget = StringUtils.defaultIfEmpty(parameter.getTarget(), "").toLowerCase();
				
				if (strTarget.equalsIgnoreCase("all")) {
					strPage  = "1";
					strCount = "100";
				}
				
				// Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
				if(strCount.equals("") || strCount.equals("0")){
					if(strSiteNo.equals("6005")){
						strCount = "6";
					}else{
						strCount = "5";
					}
				}
				
				return CollectionUtils.getPageInfo(strPage, strCount);
			}
		};
	}
	
	public Result getResult(Search search, String name, Parameter parameter, Result result){
		List<Recipe> recipeList = Lists.newArrayList();
		Recipe recipe;
		int count = search.w3GetResultCount(name);
		for(int i=0;i<count;i++){
			recipe = new Recipe();
			recipe.setRecipeNo(search.w3GetField(name, "RECIPE_NO", i));
			recipe.setRecipeNm(search.w3GetField(name, "RECIPE_NM", i));
			recipe.setCookClsCd(search.w3GetField(name, "COOK_CLS_CD", i));
			recipe.setImgPathNm(search.w3GetField(name, "IMG_PATH_NM", i));
			recipe.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i));
			recipe.setImgRplcWordNm(search.w3GetField(name, "IMG_RPLC_WORD_NM", i));
			recipe.setReadyTimeCd(search.w3GetField(name, "READY_TIME_CD", i));
			recipe.setRqrmTimeCd(search.w3GetField(name, "RQRM_TIME_CD", i));
			recipe.setCookDifclvlCd(search.w3GetField(name, "COOK_DIFCLVL_CD", i));
			recipe.setRecipeWrtpeId(search.w3GetField(name, "RECIPE_WRTPE_ID", i));
			recipe.setRecipeBrwsCnt(search.w3GetField(name, "RECIPE_BRWS_CNT", i));
			recipe.setRecipeRcmdCnt(search.w3GetField(name, "RECIPE_RCMD_CNT", i));
			recipe.setEtcIngrdNm(search.w3GetField(name, "ETC_INGRD_NM", i));
			recipe.setCookTgtAgegrpCd(search.w3GetField(name, "COOK_TGT_AGEGRP_CD", i));
			recipeList.add(recipe);
		}
		result.setRecipeList(recipeList);
		result.setRecipeCount(search.w3GetResultTotalCount(name));
		return result;
	}
}
