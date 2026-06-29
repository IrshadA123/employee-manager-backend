package com.irshad.employee_manager_backend.dto

data class EmployeeResponseDTO(
    val id: Long?,
    val name: String,
    val email: String,
    val salary: Double,
    val department: String,
    val contactNumber: String
)