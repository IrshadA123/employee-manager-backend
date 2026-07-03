package com.irshad.employee_manager_backend.repository.specification

import com.irshad.employee_manager_backend.entity.Employee
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification



object EmployeeSpecification {

    fun filterEmployees(
        department: String?,
        minSalary: Double?,
        maxSalary: Double?
    ): Specification<Employee> {

        return Specification { root, query, criteriaBuilder ->

            val predicates = mutableListOf<Predicate>()

            if (!department.isNullOrBlank()) {
                val departmentPredicate = criteriaBuilder.equal(
                    root.get<String>("department"),
                    department
                )
                predicates.add(departmentPredicate)
            }

            if (minSalary != null) {
                val minSalaryPredicate = criteriaBuilder.greaterThanOrEqualTo(
                    root.get<Double>("salary"),
                    minSalary
                )
                predicates.add(minSalaryPredicate)
            }

            if (maxSalary != null) {
                val maxSalaryPredicate = criteriaBuilder.lessThanOrEqualTo(
                    root.get<Double>("salary"),
                    maxSalary
                )
                predicates.add(maxSalaryPredicate)
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}


