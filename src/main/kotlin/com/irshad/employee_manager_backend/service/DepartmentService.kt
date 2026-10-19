
package com.irshad.employee_manager_backend.service

import com.irshad.employee_manager_backend.dto.DepartmentRequestDTO
import com.irshad.employee_manager_backend.entity.Department
import com.irshad.employee_manager_backend.repository.DepartmentRepository
import com.irshad.employee_manager_backend.repository.EmployeeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository,
    private val employeeRepository: EmployeeRepository
) {

    @Transactional
    fun createDepartment(request: DepartmentRequestDTO): Department {
        val name = request.name.trim()

        require(!departmentRepository.existsByNameIgnoreCase(name)) {
            "Department already exists: $name"
        }

        return departmentRepository.save(
            Department(
                name = name,
                departmentId = request.departmentId
            )
        )
    }

    @Transactional(readOnly = true)
    fun getAllDepartments(): List<Department> {
        return departmentRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getDepartmentById(id: Long): Department {
        return departmentRepository.findById(id)
            .orElseThrow {
                NoSuchElementException("Department not found with id: $id")
            }
    }

    @Transactional
    fun updateDepartment(
        id: Long,
        request: DepartmentRequestDTO
    ): Department {
        val department = getDepartmentById(id)
        val name = request.name.trim()

        require(
            !departmentRepository.existsByNameIgnoreCaseAndIdNot(name, id)
        ) {
            "Department already exists: $name"
        }

        department.name = name
        return departmentRepository.save(department)
    }

    @Transactional
    fun deleteDepartment(id: Long) {
        val department = getDepartmentById(id)

        val hasEmployees = employeeRepository
            .findAll()
            .any { it.department?.id == id }

        require(!hasEmployees) {
            "Cannot delete department because employees are assigned to it"
        }

        departmentRepository.delete(department)
    }
}
