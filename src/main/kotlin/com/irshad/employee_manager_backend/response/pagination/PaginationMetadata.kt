package com.irshad.employee_manager_backend.response.pagination

class PaginationMetadata (
    val currentPage: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalRecords: Long,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

