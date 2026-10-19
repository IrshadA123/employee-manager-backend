
package com.irshad.employee_manager_backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class DepartmentRequestDTO(

    @field:NotBlank(message = "Department name is required")
    val name: String,

    @field:Positive(message = "Department ID must be positive")
    val departmentId: Long
)
