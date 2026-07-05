package com.irshad.employee_manager_backend.repository.specification

import com.irshad.employee_manager_backend.entity.Employee
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification



object EmployeeSpecification {

    fun filterEmployees(
        department: String?,
        minSalary: Double?,
        maxSalary: Double?
    ): Specification<Employee> {

        return Specification { root, query, criteriaBuilder ->

            val predicates = mutableListOf<Predicate>()
            predicates.add(isNotDeleted(root, criteriaBuilder))

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
    private fun isNotDeleted(
        root: Root<Employee>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        return criteriaBuilder.equal(
            root.get<Boolean>("deleted"),
            false
        )
    }
    fun searchEmployees(keyword: String): Specification<Employee> {

        return Specification { root, query, criteriaBuilder ->

            val searchPattern = "%${keyword.lowercase()}%"

            val predicates = mutableListOf<Predicate>()

            predicates.add(isNotDeleted(root, criteriaBuilder))

            val searchPredicate = criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("department")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("contactNumber")), searchPattern)
            )

            predicates.add(searchPredicate)

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}


