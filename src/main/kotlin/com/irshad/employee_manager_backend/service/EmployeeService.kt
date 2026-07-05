package com.irshad.employee_manager_backend.service

import com.irshad.employee_manager_backend.dto.EmployeeRequestDTO
import com.irshad.employee_manager_backend.dto.EmployeeResponseDTO
import com.irshad.employee_manager_backend.entity.Employee
import com.irshad.employee_manager_backend.repository.EmployeeRepository
import org.springframework.stereotype.Service
import com.irshad.employee_manager_backend.exception.EmployeeNotFoundException
import com.irshad.employee_manager_backend.mapper.EmployeeMapper
import com.irshad.employee_manager_backend.repository.specification.EmployeeSpecification
import com.irshad.employee_manager_backend.response.pagination.PaginationMetadata
import com.irshad.employee_manager_backend.response.pagination.PaginationResponse
import org.apache.commons.lang3.StringUtils.contains
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@Service
class EmployeeService (
    private val employeeRepository: EmployeeRepository,
    private val employeeMapper: EmployeeMapper

) {
    private val logger = LoggerFactory.getLogger(EmployeeService::class.java)

    fun getAllEmployees(page: Int,
                        size: Int,
                        sortBy:String,
                        direction:String
    ): PaginationResponse<EmployeeResponseDTO> {

        logger.info("Fetching employees page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction)


        val allowedSortFields= listOf("id","name","email","salary","department","contactNumber")
        val normalizedSortBy = sortBy.lowercase()

        if (!allowedSortFields.contains(normalizedSortBy)) {
            logger.warn("Invalid sort field: {}", sortBy)

            throw IllegalArgumentException(
                "Invalid sort field: $sortBy. Allowed fields are: ${
                    allowedSortFields.joinToString(", ")
                }"
            )
        }

        val sortDirection = when (direction.lowercase()) {
            "asc" -> Sort.Direction.ASC
            "desc" -> Sort.Direction.DESC
            else ->throw IllegalArgumentException("Invalid sort direction: $direction. Allowed values are: asc, desc")
        }

        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, normalizedSortBy))

        val employeesPage = employeeRepository.findByDeletedFalse(pageable)

        val employeeResponseList = employeesPage.content.map {
            employeeMapper.toResponseDto(it)
        }

        val paginationMetadata = PaginationMetadata(
            currentPage = employeesPage.number,
            pageSize = employeesPage.size,
            totalPages = employeesPage.totalPages,
            totalRecords = employeesPage.totalElements,
            hasNext = employeesPage.hasNext(),
            hasPrevious = employeesPage.hasPrevious() ,

        )

        logger.info("Fetched {} employees from page {}", employeesPage.numberOfElements, page)

        return PaginationResponse(
            items = employeeResponseList,
            pagination = paginationMetadata
        )
    }

    fun createEmployee(requestDTO: EmployeeRequestDTO): EmployeeResponseDTO {
        logger.info("Creating employee with email: {}", requestDTO.email)
        val employee = employeeMapper.toEntity(requestDTO)
        val savedEmployee = employeeRepository.save(employee)
        logger.info("Employee created successfully with id: {}", savedEmployee.id)
        return employeeMapper.toResponseDto(savedEmployee)
    }


    fun updateEmployee(id:Long, request: EmployeeRequestDTO ):EmployeeResponseDTO{
        logger.info("Updating employee with id: {}", id)
        val employee = employeeRepository.findById(id).orElseThrow {
            logger.warn("Employee not found with id: {}", id)
            EmployeeNotFoundException("Employee not found with id: $id")}

        validateEmployeeIsActive(employee, id)
        employee.name = request.name
        employee.email = request.email
        employee.salary = request.salary
        employee.department = request.department
        employee.contactNumber = request.contactNumber
        val updatedEmployee = employeeRepository.save(employee)
        logger.info("Employee updated successfully with id: {}", updatedEmployee.id)
        return employeeMapper.toResponseDto(updatedEmployee)
    }

    fun getEmployeeById(id: Long): EmployeeResponseDTO {
        logger.info("Fetching employee with id: {}", id)
        val employee = employeeRepository.findById(id)
            .orElseThrow {
                logger.warn("Employee not found with id: {}", id)
                EmployeeNotFoundException("Employee not found with id: $id")

            }
        validateEmployeeIsActive(employee, id)
        logger.info("Employee fetched successfully with id: {}", id)

        return employeeMapper.toResponseDto(employee)
    }

    fun deleteEmployee(id :Long){
        logger.info("Deleting employee with id: {}", id)
        val employee = employeeRepository.findById(id).orElseThrow {
            logger.warn("Employee not found with id: {}", id)
            EmployeeNotFoundException("Employee not found: $id")
        }
        if (employee.deleted) {
            logger.warn("Employee already deleted with id: {}", id)
            throw IllegalStateException("Employee with id $id is already deleted")
        }
        employee.deleted = true
        employeeRepository.save(employee)
        logger.info("Employee soft-deleted successfully with id: {}", id)
    }


    fun searchEmployees(keyword: String): List<EmployeeResponseDTO> {
        logger.info("Searching employees with keyword: {}", keyword)

        val normalizedKeyword = keyword.trim()

        if (normalizedKeyword.isBlank()) {
            logger.warn("Search keyword is blank")
            throw IllegalArgumentException("Keyword cannot be blank")
        }
        val specification = EmployeeSpecification.searchEmployees(normalizedKeyword)
        val employees = employeeRepository.findAll(specification)
        val employeeResponseList = employees.map {
            employeeMapper.toResponseDto(it)
        }
        logger.info("Found {} employees matching keyword: {}", employeeResponseList.size, normalizedKeyword)
        return employeeResponseList
    }


    fun filterEmployees(
        department: String?,
        minSalary: Double?,
        maxSalary: Double?
    ): List<EmployeeResponseDTO> {
        logger.info("Filtering employees with department: {}, minSalary: {}, maxSalary: {}", department, minSalary, maxSalary)
        val specification = EmployeeSpecification.filterEmployees(
            department,
            minSalary,
            maxSalary
        )
        val employees = employeeRepository.findAll(specification)
        val employeeResponseList = employees.map { employeeMapper.toResponseDto(it) }
        logger.info("Found {} employees matching filter criteria", employeeResponseList.size)
        return employeeResponseList
    }

    private fun validateEmployeeIsActive(employee: Employee, id: Long) {
        if (employee.deleted) {
            logger.warn("Employee is deleted with id: {}", id)
            throw EmployeeNotFoundException("Employee not found with id: $id")
        }
    }
}
