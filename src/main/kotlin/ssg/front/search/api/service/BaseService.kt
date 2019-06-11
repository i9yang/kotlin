package ssg.front.search.api.service

import ssg.front.search.api.core.constants.TargetGroup
import ssg.front.search.api.core.constants.Targets
import ssg.front.search.api.dto.Parameter
import ssg.front.search.api.dto.result.Result
import ssg.front.search.api.util.CollectionUtils

open class BaseService {
    fun iterateBuildersAndExecute(parameter: Parameter) : Result {
        var result = Result()
        var targetGroup = Targets.valueOf(parameter.target.toUpperCase()).targetGroup

        if (parameter.srchVer!! > 1.0) {
            targetGroup = if (CollectionUtils.isAdSearch(parameter)) {
                TargetGroup.AD_RC_WN
            } else {
                TargetGroup.RC_WN
            }
        }

        targetGroup.getQueryBuilders(parameter).forEach {
            try {
                it.execute(parameter, result)
            } catch (e: Exception) {
                e.printStackTrace()
                result.libErr = true
                result.libErrMsg = e.message ?: ""
            }
        }
        return result
    }
}