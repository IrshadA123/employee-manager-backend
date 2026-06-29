package com.irshad.employee_manager_backend.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException::class)
    fun handleEmployeeNotFoundException(
        ex: EmployeeNotFoundException
    ): ResponseEntity<Map<String, Any>> {

        val response = mapOf(
            "timestamp" to LocalDateTime.now().toString(),
            "status" to HttpStatus.NOT_FOUND.value(),
            "error" to "Not Found",
            "message" to ex.message.orEmpty()
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
    }
}