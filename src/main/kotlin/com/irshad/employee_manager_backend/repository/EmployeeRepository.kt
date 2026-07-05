package com.irshad.employee_manager_backend.repository

import com.irshad.employee_manager_backend.entity.Employee
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface EmployeeRepository : JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    fun findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentContainingIgnoreCaseOrContactNumberContainingIgnoreCase(
        name: String,
        email: String,
        department: String,
        contactNumber: String
    ): List<Employee>

    fun findByDeletedFalse(pageable: Pageable): Page<Employee>
}