package ssg.front.search.config

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import ssg.front.search.api.core.dto.DataApiResDto

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(Exception::class)
    @ResponseBody
    fun exceptionHandler(e: Exception): DataApiResDto<String> {
        e.printStackTrace()
        return DataApiResDto.newFailure(e.message!!)
    }
}