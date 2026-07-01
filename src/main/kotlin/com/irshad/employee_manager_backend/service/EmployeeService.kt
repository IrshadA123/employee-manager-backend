package com.irshad.employee_manager_backend.service

import com.irshad.employee_manager_backend.dto.EmployeeRequestDTO
import com.irshad.employee_manager_backend.dto.EmployeeResponseDTO
import com.irshad.employee_manager_backend.repository.EmployeeRepository
import org.springframework.stereotype.Service
import com.irshad.employee_manager_backend.exception.EmployeeNotFoundException
import com.irshad.employee_manager_backend.mapper.EmployeeMapper
import com.irshad.employee_manager_backend.response.pagination.PaginationMetadata
import com.irshad.employee_manager_backend.response.pagination.PaginationResponse
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest

@Service
class EmployeeService (
    private val employeeRepository: EmployeeRepository,
    private val employeeMapper: EmployeeMapper

) {
    private val logger = LoggerFactory.getLogger(EmployeeService::class.java)

    fun getAllEmployees(page: Int, size: Int): PaginationResponse<EmployeeResponseDTO> {

        logger.info("Fetching employees with page: {}, size: {}", page, size)

        val pageable = PageRequest.of(page, size)

        val employeesPage = employeeRepository.findAll(pageable)

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
        logger.info("Employee fetched successfully with id: {}", id)

        return employeeMapper.toResponseDto(employee)
    }

    fun deleteEmployee(id :Long){
        logger.info("Deleting employee with id: {}", id)
        val employee = employeeRepository.findById(id).orElseThrow {
            logger.warn("Employee not found with id: {}", id)
            EmployeeNotFoundException("Employee not found : $id") }
        employeeRepository.delete(employee)
        logger.info("Employee deleted successfully with id: {}", id)
    }

}
