package com.irshad.employee_manager_backend.response

    data class ApiResponse<T>(
        val success: Boolean,
        val message: String,
        val data: T?
    )
