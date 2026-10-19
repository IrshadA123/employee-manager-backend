package com.irshad.employee_manager_backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class EmployeeRequestDTO(

    @field:NotBlank(message = "Name is required")
    val name: String,

    @field:Email(message = "Email should be valid")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @field:Positive(message = "Salary must be positive")
    val salary: Double,

    @field:Positive(message = "Department id must be positive")
    val departmentId: Long,

    @field:NotBlank(message = "Contact number is required")
    val contactNumber: String
)
