package com.irshad.employee_manager_backend.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "departments")
class Department(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String = "",

    @field:Positive(message = "Department id must be positive")
    val departmentId: Long,
)