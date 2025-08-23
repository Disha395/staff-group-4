package com.college.management.repository;

import com.college.management.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Find staff by name (case-insensitive)
    @Query("SELECT s FROM Staff s WHERE LOWER(s.staffName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Staff> findByStaffNameContaining(@Param("name") String name);

    // Find staff by department ID
    List<Staff> findByDepartmentDepartmentId(Long departmentId);

    // Find staff by department name
    @Query("SELECT s FROM Staff s WHERE s.department.departmentName = :departmentName")
    List<Staff> findByDepartmentName(@Param("departmentName") String departmentName);

    // Find staff with salary greater than specified amount
    List<Staff> findBySalaryGreaterThan(BigDecimal salary);

    // Find staff with salary between range
    List<Staff> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary);

    // Count staff in a department
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.department.departmentId = :departmentId")
    long countByDepartmentId(@Param("departmentId") Long departmentId);

    // Find highest paid staff
    Optional<Staff> findTopByOrderBySalaryDesc();

    // Find staff ordered by name
    List<Staff> findAllByOrderByStaffNameAsc();
}