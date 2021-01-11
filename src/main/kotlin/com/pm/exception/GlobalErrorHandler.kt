package com.pm.exception

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice

class ApiErrorResponse {
    var detail = "Internal server error"
    var status = HttpStatus.INTERNAL_SERVER_ERROR.value()
    var title = "Unknown Error"
}

@RestControllerAdvice
class GlobalErrorHandler {

    @ExceptionHandler(FeignException::class)
    @ResponseBody
    fun handleAllExceptions(ex: FeignException): ResponseEntity<ApiErrorResponse> {
        var apiErrorResponse = ApiErrorResponse()
        if (ex.contentUTF8() != null) {
            apiErrorResponse = jacksonObjectMapper()
                .readValue(ex.contentUTF8(), ApiErrorResponse::class.java)
        }
        return ResponseEntity.status(apiErrorResponse.status)
            .body(apiErrorResponse)
    }

    @ExceptionHandler(ProjectManagerException::class)
    @ResponseBody
    fun handleAllExceptions(ex: ProjectManagerException): ResponseEntity<ApiErrorResponse> {
        val apiErrorResponse = ApiErrorResponse()
        apiErrorResponse.status = ex.httpStatus.value()
        apiErrorResponse.detail = ex.message.toString()
        apiErrorResponse.title = ex.httpStatus.reasonPhrase

        return ResponseEntity.status(apiErrorResponse.status)
            .body(apiErrorResponse)
    }

}
