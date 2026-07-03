package com.irshad.employee_manager_backend.repository

import com.irshad.employee_manager_backend.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository

interface EmployeeRepository : JpaRepository<Employee, Long> {

    fun findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrContactNumberContainingIgnoreCase(
        name: String,
        email: String,
        department: String,
        contactNumber: String
    ): List<Employee>
}