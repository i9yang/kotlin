package ssg.front.search.api.collection.es.brand

import com.google.common.base.Joiner
import org.elasticsearch.action.search.SearchResponse
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.util.ResultUtils

class EsBrandBookCtgIdGroup : EsBrandCtgIdGroup() {
    override var indexName = "book"

    override fun getResult(searchResponse: SearchResponse, name: String, parameter: Parameter, result: Result) {
        var categoryList = super.getCategoryList(searchResponse)

        if (parameter.userInfo.deviceDivCd == "20") {
            result.bookCategoryList = ResultUtils.getNewBrandCategoryGroup(parameter.siteNo, parameter.dispCtgId, Joiner.on("@").join(categoryList))
        } else {
            result.bookCategoryList = ResultUtils.getBrandCategoryGroup(parameter.siteNo, parameter.dispCtgId, Joiner.on("@").join(categoryList))
        }
    }
}