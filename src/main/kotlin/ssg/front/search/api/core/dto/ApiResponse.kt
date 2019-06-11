package ssg.front.search.api.core.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

interface ApiResponse {
    var code: String?

    @get:JsonInclude(Include.NON_NULL)
    var detailCode: String?

    var message: String?

    companion object {
        val 성공_코드 = "200"
        val 성공_기본메시지 = "SUCCESS"
        val 실패_코드 = "500"
        val 실패_기본메시지 = "FAILURE"
    }
}