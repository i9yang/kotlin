package ssg.front.search.api.core.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import ssg.front.search.api.core.dto.ApiResponse.Companion.성공_기본메시지
import ssg.front.search.api.core.dto.ApiResponse.Companion.성공_코드
import ssg.front.search.api.core.dto.ApiResponse.Companion.실패_기본메시지
import ssg.front.search.api.core.dto.ApiResponse.Companion.실패_코드


class DataApiResDto<T> private constructor(override var code: String?, detailCode: String?, message: String?,
                                           val data: T?) : ApiResponse {

    @JsonInclude(Include.NON_NULL)
    override var detailCode: String? = "200"
    override var message: String? = null

    init {
        if (detailCode!!.isNotBlank()) {
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
    }

    /* 빌더를 이용해야만 인스턴스를 생성할 수 있다. */
    class DataApiResponseDtoBuilder<T> {
        private var code: String? = null
        private var detailCode: String? = "200"
        private var message: String? = null
        private var data: T? = null

        fun success(): DataApiResponseDtoBuilder<T> {
            code = 성공_코드
            return this
        }

        fun failure(): DataApiResponseDtoBuilder<T> {
            code = 실패_코드
            return this
        }

        fun detailCode(detailCode: String?): DataApiResponseDtoBuilder<T> {
            this.detailCode = detailCode
            return this
        }

        fun message(message: String?): DataApiResponseDtoBuilder<T> {
            this.message = message
            return this
        }

        fun data(data: T): DataApiResponseDtoBuilder<T> {
            this.data = data
            return this
        }

        fun of(response: ApiResponse): DataApiResponseDtoBuilder<T> {
            this.code = response.code
            this.message = response.message
            return this
        }

        fun build(): DataApiResDto<T> {
            return DataApiResDto<T>(code, detailCode, message, this!!.data)
        }
    }

    companion object {


        fun <T> builder(): DataApiResponseDtoBuilder<T> {
            return DataApiResponseDtoBuilder()
        }


        /* 자주 사용하는 형태의 응답값을 메서드로 만들어둔다 */

        /** 성공 응답을 생성합니다.  */
        fun <T> newSuccess(data: T): DataApiResDto<T> {
            return DataApiResDto.builder<T>().data(data).success().build()
        }

        /** 실패 응답을 생성합니다.  */
        fun <T> newFailure(): DataApiResDto<T> {
            return DataApiResDto.builder<T>().failure().build()
        }

        /** 실패 응답을 생성합니다.  */
        fun <T> newFailure(message: String): DataApiResDto<T> {
            return DataApiResDto.builder<T>().failure().message(message).build()
        }

        /** 실패 응답을 생성합니다.  */
        fun <T> newFailure(detailCode: String, message: String): DataApiResDto<T> {
            return DataApiResDto.builder<T>().failure().detailCode(detailCode).message(message).build()
        }
    }
}