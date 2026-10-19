
package com.irshad.employee_manager_backend.controller

import com.irshad.employee_manager_backend.dto.DepartmentRequestDTO
import com.irshad.employee_manager_backend.entity.Department
import com.irshad.employee_manager_backend.service.DepartmentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(
    name = "Department Management",
    description = "APIs for creating, retrieving, updating and deleting departments"
)
@RestController
@RequestMapping("/api/departments")
class DepartmentController(
    private val departmentService: DepartmentService
) {

    @Operation(
        summary = "Create department",
        description = "Creates a new department. Department names must be unique."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createDepartment(
        @Valid @RequestBody request: DepartmentRequestDTO
    ): Department {
        return departmentService.createDepartment(request)
    }

    @Operation(
        summary = "Get all departments",
        description = "Retrieves all departments available for employee assignment."
    )
    @GetMapping
    fun getAllDepartments(): List<Department> {
        return departmentService.getAllDepartments()
    }

    @Operation(
        summary = "Get department by ID",
        description = "Retrieves a specific department using its database ID."
    )
    @GetMapping("/{id}")
    fun getDepartmentById(
        @PathVariable id: Long
    ): Department {
        return departmentService.getDepartmentById(id)
    }

    @Operation(
        summary = "Update department",
        description = "Updates the name of an existing department. Duplicate names are not allowed."
    )
    @PutMapping("/{id}")
    fun updateDepartment(
        @PathVariable id: Long,
        @Valid @RequestBody request: DepartmentRequestDTO
    ): Department {
        return departmentService.updateDepartment(id, request)
    }

    @Operation(
        summary = "Delete department",
        description = "Deletes a department only if no employees are assigned to it."
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDepartment(
        @PathVariable id: Long
    ) {
        departmentService.deleteDepartment(id)
    }
}
