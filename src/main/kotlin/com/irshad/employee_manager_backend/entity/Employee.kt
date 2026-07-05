package com.irshad.employee_manager_backend.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "employees")
class Employee (


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotBlank(message = "Name is required")
    var name: String = "",

    @field:Email(message = "Email should be valid")
    @field:NotBlank(message = "Email is required")
    var email: String = "",

    @field:Positive(message = "Salary must be positive")
    var salary: Double = 0.0,

    @field:NotBlank(message = "Department is required")
    var department: String = "",

    @field:NotBlank(message = "Contact number is required")
    var contactNumber: String = "",

    @Column(nullable = false)
    var deleted: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = null

)