
package com.irshad.employee_manager_backend.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger =
        LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    private fun errorResponse(
        status: HttpStatus,
        message: String,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {

        val body = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to status.value(),
            "error" to status.reasonPhrase,
            "message" to message,
            "path" to request.requestURI
        )

        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(EmployeeNotFoundException::class)
    fun handleEmployeeNotFound(
        ex: EmployeeNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        return errorResponse(
            HttpStatus.NOT_FOUND,
            ex.message ?: "Employee not found",
            request
        )
    }

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFound(
        ex: NoSuchElementException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        return errorResponse(
            HttpStatus.NOT_FOUND,
            ex.message ?: "Resource not found",
            request
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {

        val status = when {
            ex.message?.startsWith("Department not found") == true ->
                HttpStatus.NOT_FOUND

            ex.message?.startsWith("Department already exists") == true ->
                HttpStatus.CONFLICT

            ex.message?.startsWith("Cannot delete department") == true ->
                HttpStatus.CONFLICT

            else -> HttpStatus.BAD_REQUEST
        }

        return errorResponse(
            status,
            ex.message ?: "Invalid request",
            request
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {

        val message = ex.bindingResult.fieldErrors
            .joinToString("; ") {
                "${it.field}: ${it.defaultMessage}"
            }

        return errorResponse(
            HttpStatus.BAD_REQUEST,
            message.ifBlank { "Validation failed" },
            request
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJson(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        return errorResponse(
            HttpStatus.BAD_REQUEST,
            "Invalid JSON request body",
            request
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedError(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {

        logger.error(
            "Unexpected error at ${request.requestURI}",
            ex
        )

        return errorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            request
        )
    }
}
