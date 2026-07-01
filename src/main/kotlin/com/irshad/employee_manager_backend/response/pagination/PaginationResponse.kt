package com.irshad.employee_manager_backend.response.pagination

data class PaginationResponse<T>(
    val items: List<T>,
    val pagination: PaginationMetadata
)
