package com.college.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    @NotBlank(message = "Staff name is required")
    @Column(name = "staff_name", nullable = false)
    private String staffName;

    @NotNull(message = "Department ID is required")
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    @Column(name = "salary", nullable = false)
    private Double salary;

    // Default constructor
    public Staff() {}

    // Constructor with parameters
    public Staff(String staffName, Long departmentId, Double salary) {
        this.staffName = staffName;
        this.departmentId = departmentId;
        this.salary = salary;
    }

    // Getters and Setters
    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", staffName='" + staffName + '\'' +
                ", departmentId=" + departmentId +
                ", salary=" + salary +
                '}';
    }
}