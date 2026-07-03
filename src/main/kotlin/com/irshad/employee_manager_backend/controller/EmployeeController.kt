package com.irshad.employee_manager_backend.controller

import com.irshad.employee_manager_backend.dto.EmployeeRequestDTO
import com.irshad.employee_manager_backend.dto.EmployeeResponseDTO
import com.irshad.employee_manager_backend.response.ApiResponse
import com.irshad.employee_manager_backend.response.pagination.PaginationResponse
import com.irshad.employee_manager_backend.service.EmployeeService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page

// @TAg for swagger
@Tag(
    name = "Employee Management",
    description = "APIs for managing employees"
)
@RestController
@RequestMapping("/api/employees")
class EmployeeController (
    private val employeeService: EmployeeService
) {

    @Operation(
        summary = "Get employees",
        description = "Returns employees with pagination. Default page is 0 and size is 10."
    )
    @GetMapping
    fun getAllEmployees(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "asc") direction: String
    ): ApiResponse<PaginationResponse<EmployeeResponseDTO>> {

        return ApiResponse(
            success = true,
            message = "Employees fetched successfully",
            data = employeeService.getAllEmployees(page, size, sortBy, direction)
        )
    }

    @Operation(
        summary = "Create employee",
        description = "Creates a new employee."
    )
    @PostMapping()
    fun createEmployee(@Valid @RequestBody request: EmployeeRequestDTO): ApiResponse<EmployeeResponseDTO> {
        return ApiResponse(
            success = true,
            message = "Employees created successfully",
            data = employeeService.createEmployee(request)
        )

    }

    @Operation(
        summary = "update employee by ID",
        description = "update employee details using employee ID."
    )
    @PutMapping("/{id}")
    fun updateEmployee(@Valid @RequestBody request: EmployeeRequestDTO, @PathVariable id: Long): ApiResponse<EmployeeResponseDTO> {
        return ApiResponse(
            success = true,
            message = "Employees updated successfully",
            data = employeeService.updateEmployee(id, request)
        )
    }

    @Operation(
        summary = "Get employee by ID",
        description = "Get employee details using employee ID."
    )
    @GetMapping("/{id}")
    fun getEmployeeById(@PathVariable id: Long): ApiResponse< EmployeeResponseDTO>{
        return ApiResponse(
            success = true,
            message = "Employee fetched successfully",
            data = employeeService.getEmployeeById(id)
        )
    }

    @Operation(
        summary = "Delete employee",
        description = "Deletes an employee by ID."
    )
    @DeleteMapping("/{id}")
    fun deleteEmployee(@PathVariable id: Long) :ApiResponse<String> {
        employeeService.deleteEmployee(id)
        return ApiResponse(
            success = true,
            message = "Employee deleted successfully",
            data = "Employee with id $id deleted successfully"
        )
    }
}