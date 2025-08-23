package com.college.management.service;

import com.college.management.entity.Staff;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StaffService {

    // Basic CRUD operations
    Staff saveStaff(Staff staff);
    Optional<Staff> getStaffById(Long id);
    List<Staff> getAllStaff();
    Staff updateStaff(Long id, Staff staff);
    boolean deleteStaff(Long id);

    // Custom operations
    List<Staff> getStaffByName(String name);
    List<Staff> getStaffByDepartmentId(Long departmentId);
    List<Staff> getStaffByDepartmentName(String departmentName);
    List<Staff> getStaffBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary);
    Optional<Staff> getHighestPaidStaff();
    long getStaffCountByDepartment(Long departmentId);
    boolean existsById(Long id);
}