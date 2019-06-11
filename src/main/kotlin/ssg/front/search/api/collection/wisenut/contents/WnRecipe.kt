package ssg.front.search.api.collection.wisenut.contents

import QueryAPI510.Search
import ssg.front.search.api.collection.wisenut.WnCollection
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.dto.vo.PageVo
import ssg.front.search.api.function.Pageable
import ssg.front.search.api.util.CollectionUtils
import ssg.search.result.Recipe

class WnRecipe : WnCollection(), Pageable {
    override fun getName(parameter: Parameter): String {
        return "recipe"
    }

    override fun getAliasName(parameter: Parameter): String {
        return "recipe"
    }

    override fun getDocumentField(parameter: Parameter): Array<String> {
        return arrayOf("RECIPE_NO", "RECIPE_NM", "COOK_CLS_CD", "IMG_PATH_NM", "IMG_FILE_NM", "IMG_RPLC_WORD_NM", "COOK_KEYWD_NM", "READY_TIME_CD", "RQRM_TIME_CD", "COOK_DIFCLVL_CD", "RECIPE_WRTPE_ID", "RECIPE_BRWS_CNT", "RECIPE_RCMD_CNT", "ETC_INGRD_NM", "COOK_TGT_AGEGRP_CD")
    }

    override fun getSearchField(parameter: Parameter): Array<String> {
        return arrayOf("RECIPE_NO", "RECIPE_NM", "COOK_KEYWD_NM", "ETC_INGRD_NM")
    }

    override fun pageVo(parameter: Parameter): PageVo {
        var strPage = parameter.page ?: "1"
        var strCount = parameter.count ?: ""
        val strSiteNo = parameter.siteNo ?: "6005"
        val strTarget = (parameter.target ?: "").toLowerCase()

        if (strTarget.equals("all", ignoreCase = true)) {
            strPage = "1"
            strCount = "100"
        }

        // Count에 값이 없는 경우 디폴트 사이즈를 SET 해준다.
        if (strCount == "" || strCount == "0") {
            if (strSiteNo == "6005") {
                strCount = "6"
            } else {
                strCount = "5"
            }
        }

        return CollectionUtils.getPageInfo(strPage, strCount)
    }

    override fun getResult(search: Search, name: String, parameter: Parameter, result: Result): Result {
        val recipeList = arrayListOf<Recipe>()
        var recipe: Recipe
        val count = search.w3GetResultCount(name)
        for (i in 0 until count) {
            recipe = Recipe()
            recipe.setRecipeNo(search.w3GetField(name, "RECIPE_NO", i))
            recipe.setRecipeNm(search.w3GetField(name, "RECIPE_NM", i))
            recipe.setCookClsCd(search.w3GetField(name, "COOK_CLS_CD", i))
            recipe.setImgPathNm(search.w3GetField(name, "IMG_PATH_NM", i))
            recipe.setImgFileNm(search.w3GetField(name, "IMG_FILE_NM", i))
            recipe.setImgRplcWordNm(search.w3GetField(name, "IMG_RPLC_WORD_NM", i))
            recipe.setReadyTimeCd(search.w3GetField(name, "READY_TIME_CD", i))
            recipe.setRqrmTimeCd(search.w3GetField(name, "RQRM_TIME_CD", i))
            recipe.setCookDifclvlCd(search.w3GetField(name, "COOK_DIFCLVL_CD", i))
            recipe.setRecipeWrtpeId(search.w3GetField(name, "RECIPE_WRTPE_ID", i))
            recipe.setRecipeBrwsCnt(search.w3GetField(name, "RECIPE_BRWS_CNT", i))
            recipe.setRecipeRcmdCnt(search.w3GetField(name, "RECIPE_RCMD_CNT", i))
            recipe.setEtcIngrdNm(search.w3GetField(name, "ETC_INGRD_NM", i))
            recipe.setCookTgtAgegrpCd(search.w3GetField(name, "COOK_TGT_AGEGRP_CD", i))
            recipeList.add(recipe)
        }
        result.recipeList = recipeList
        result.recipeCount = search.w3GetResultTotalCount(name)
        return result
    }
}
