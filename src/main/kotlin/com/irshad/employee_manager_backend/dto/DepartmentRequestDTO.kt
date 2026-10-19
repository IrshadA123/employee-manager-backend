package com.irshad.employee_manager_backend.dto

import jakarta.validation.constraints.NotBlank

data class DepartmentRequestDTO(
    @field:NotBlank(message = "Department name is required")
    val name: String
)