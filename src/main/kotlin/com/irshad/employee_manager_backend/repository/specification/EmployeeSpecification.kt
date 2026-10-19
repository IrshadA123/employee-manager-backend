
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

        return Specification { root, _, criteriaBuilder ->

            val predicates = mutableListOf<Predicate>()
            predicates.add(isNotDeleted(root, criteriaBuilder))

            if (!department.isNullOrBlank()) {
                predicates.add(
                    criteriaBuilder.equal(
                        criteriaBuilder.lower(
                            root.join<Employee, Any>("department")
                                .get<String>("name")
                        ),
                        department.trim().lowercase()
                    )
                )
            }

            if (minSalary != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("salary"),
                        minSalary
                    )
                )
            }

            if (maxSalary != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("salary"),
                        maxSalary
                    )
                )
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }

    fun searchEmployees(keyword: String): Specification<Employee> {

        return Specification { root, _, criteriaBuilder ->

            val searchPattern = "%${keyword.trim().lowercase()}%"

            val departmentName = root.join<Employee, Any>("department")
                .get<String>("name")

            criteriaBuilder.and(
                isNotDeleted(root, criteriaBuilder),
                criteriaBuilder.or(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        searchPattern
                    ),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        searchPattern
                    ),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(departmentName),
                        searchPattern
                    ),
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("contactNumber")),
                        searchPattern
                    )
                )
            )
        }
    }

    private fun isNotDeleted(
        root: Root<Employee>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        return criteriaBuilder.isFalse(root.get("deleted"))
    }
}
