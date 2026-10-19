
package com.irshad.employee_manager_backend.repository

import com.irshad.employee_manager_backend.entity.Department
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepartmentRepository : JpaRepository<Department, Long> {

    fun existsByNameIgnoreCase(name: String): Boolean

    fun existsByNameIgnoreCaseAndIdNot(
        name: String,
        id: Long
    ): Boolean
}
