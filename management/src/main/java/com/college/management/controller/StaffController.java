package com.college.management.controller;

import com.college.management.entity.Staff;
import com.college.management.entity.Department;
import com.college.management.repository.DepartmentRepository;
import com.college.management.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    private final StaffService staffService;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public StaffController(StaffService staffService, DepartmentRepository departmentRepository) {
        this.staffService = staffService;
        this.departmentRepository = departmentRepository;
    }

    // GET all staff
    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    // GET staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        return staffService.getStaffById(id)
                .map(staff -> ResponseEntity.ok(staff))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Create new staff
    @PostMapping
    public ResponseEntity<?> createStaff(@Valid @RequestBody Map<String, Object> staffData) {
        try {
            Staff staff = new Staff();
            staff.setStaffName((String) staffData.get("staffName"));

            // Handle department
            Object deptObj = staffData.get("departmentId");
            Long departmentId;
            if (deptObj instanceof Integer) {
                departmentId = ((Integer) deptObj).longValue();
            } else {
                departmentId = (Long) deptObj;
            }

            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            staff.setDepartment(department);

            // Handle salary
            Object salaryObj = staffData.get("salary");
            BigDecimal salary;
            if (salaryObj instanceof Number) {
                salary = new BigDecimal(salaryObj.toString());
            } else {
                salary = new BigDecimal((String) salaryObj);
            }
            staff.setSalary(salary);

            Staff savedStaff = staffService.saveStaff(staff);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedStaff);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PUT - Update staff
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable Long id, @Valid @RequestBody Map<String, Object> staffData) {
        try {
            if (!staffService.existsById(id)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Staff not found with id: " + id);
                return ResponseEntity.notFound().build();
            }

            Staff staff = new Staff();
            staff.setStaffName((String) staffData.get("staffName"));

            // Handle department
            Object deptObj = staffData.get("departmentId");
            Long departmentId;
            if (deptObj instanceof Integer) {
                departmentId = ((Integer) deptObj).longValue();
            } else {
                departmentId = (Long) deptObj;
            }

            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));
            staff.setDepartment(department);

            // Handle salary
            Object salaryObj = staffData.get("salary");
            BigDecimal salary;
            if (salaryObj instanceof Number) {
                salary = new BigDecimal(salaryObj.toString());
            } else {
                salary = new BigDecimal((String) salaryObj);
            }
            staff.setSalary(salary);

            Staff updatedStaff = staffService.updateStaff(id, staff);
            return ResponseEntity.ok(updatedStaff);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // DELETE staff
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Long id) {
        if (staffService.deleteStaff(id)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Staff deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Staff not found with id: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    // GET staff by name (search)
    @GetMapping("/search")
    public ResponseEntity<List<Staff>> searchStaffByName(@RequestParam String name) {
        List<Staff> staffList = staffService.getStaffByName(name);
        return ResponseEntity.ok(staffList);
    }

    // GET staff by department ID
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Staff>> getStaffByDepartment(@PathVariable Long departmentId) {
        List<Staff> staffList = staffService.getStaffByDepartmentId(departmentId);
        return ResponseEntity.ok(staffList);
    }

    // GET staff by salary range
    @GetMapping("/salary-range")
    public ResponseEntity<List<Staff>> getStaffBySalaryRange(
            @RequestParam BigDecimal minSalary,
            @RequestParam BigDecimal maxSalary) {
        List<Staff> staffList = staffService.getStaffBySalaryRange(minSalary, maxSalary);
        return ResponseEntity.ok(staffList);
    }

    // GET highest paid staff
    @GetMapping("/highest-paid")
    public ResponseEntity<Staff> getHighestPaidStaff() {
        return staffService.getHighestPaidStaff()
                .map(staff -> ResponseEntity.ok(staff))
                .orElse(ResponseEntity.notFound().build());
    }

    // GET all departments (helper endpoint)
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return ResponseEntity.ok(departments);
    }
}