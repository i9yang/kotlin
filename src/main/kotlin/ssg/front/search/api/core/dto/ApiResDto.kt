package ssg.front.search.api.core.dto

import ssg.front.search.api.core.dto.ApiResponse.Companion.성공_기본메시지
import ssg.front.search.api.core.dto.ApiResponse.Companion.성공_코드
import ssg.front.search.api.core.dto.ApiResponse.Companion.실패_기본메시지
import ssg.front.search.api.core.dto.ApiResponse.Companion.실패_코드


class ApiResDto private constructor(override var code: String?, detailCode: String?, message: String?) : ApiResponse {
    override var detailCode: String? = null
    override var message: String? = null

    init {
        if (detailCode != null) {
            this.detailCode = detailCode
    }
    this.message = message
    if (code != null && message == null) {
        if (code === 성공_코드) {
            this.message = 성공_기본메시지
        } else if (code === 실패_코드) {
            this.message = 실패_기본메시지
        }
    }
}/* 빌더를 이용해야만 인스턴스를 생성할 수 있다. */

class NoDataApiResponseDtoBuilder {
    private var code: String? = null
    private var detailCode: String? = null
        private var message: String? = null

        fun success(): NoDataApiResponseDtoBuilder {
            code = 성공_코드
            return this
        }

        fun failure(): NoDataApiResponseDtoBuilder {
            code = 실패_코드
            return this
        }

        fun message(message: String): NoDataApiResponseDtoBuilder {
            this.message = message
            return this
        }

        fun detailCode(detailCode: String): NoDataApiResponseDtoBuilder {
            this.detailCode = detailCode
            return this
        }

        fun of(response: ApiResponse): NoDataApiResponseDtoBuilder {
            this.code = response.code
            this.message = response.message
            return this
        }

        fun build(): ApiResDto {
            return ApiResDto(code, detailCode, message)
        }
    }

    companion object {
        fun builder(): NoDataApiResponseDtoBuilder {
            return NoDataApiResponseDtoBuilder()
        }

        /** 성공 응답을 생성합니다.  */
        fun newSuccess(): ApiResDto {
            return ApiResDto.builder().success().build()
        }

        /** 성공 응답을 생성합니다.  */
        fun newSuccess(message: String): ApiResDto {
            return ApiResDto.builder().success().message(message).build()
        }

        /** 실패 응답을 생성합니다.  */
        fun newFailure(): ApiResDto {
            return ApiResDto.builder().failure().build()
        }

        /** 실패 응답을 생성합니다.  */
        fun newFailure(message: String): ApiResDto {
            return ApiResDto.builder().failure().message(message).build()
        }

        /** 실패 응답을 생성합니다.  */
        fun newFailure(detailCode: String, message: String): ApiResDto {
            return ApiResDto.builder().failure().detailCode(detailCode).message(message).build()
        }
    }
}
