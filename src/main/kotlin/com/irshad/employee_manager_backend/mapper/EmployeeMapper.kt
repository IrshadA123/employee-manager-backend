package com.irshad.employee_manager_backend.mapper

import com.irshad.employee_manager_backend.dto.EmployeeRequestDTO
import com.irshad.employee_manager_backend.dto.EmployeeResponseDTO
import com.irshad.employee_manager_backend.entity.Employee
import org.springframework.stereotype.Component

@Component
class EmployeeMapper {
    fun toEntity(dto: EmployeeRequestDTO): Employee {

        return Employee(
            name = dto.name,
            email = dto.email,
            salary = dto.salary,
            department = dto.department,
            contactNumber = dto.contactNumber
        )

    }
    fun toResponseDto(employee: Employee): EmployeeResponseDTO {

        return EmployeeResponseDTO(
            id = employee.id,
            name = employee.name,
            email = employee.email,
            salary = employee.salary,
            department = employee.department,
            contactNumber = employee.contactNumber
        )

    }

}