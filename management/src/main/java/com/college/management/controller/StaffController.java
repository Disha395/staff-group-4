package com.college.management.controller;

import com.college.management.entity.Staff;
import com.college.management.service.StaffService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {

    @Autowired
    private StaffService staffService;

    // GET all staff
    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    // GET staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST create new staff
    @PostMapping
    public ResponseEntity<Staff> createStaff(@Valid @RequestBody Staff staff) {
        Staff createdStaff = staffService.createStaff(staff);
        return new ResponseEntity<>(createdStaff, HttpStatus.CREATED);
    }

    // PUT update staff
    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @Valid @RequestBody Staff staff) {
        try {
            Staff updatedStaff = staffService.updateStaff(id, staff);
            return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // DELETE staff
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        try {
            staffService.deleteStaff(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GET staff by department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Staff>> getStaffByDepartment(@PathVariable Long departmentId) {
        List<Staff> staffList = staffService.getStaffByDepartment(departmentId);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    // GET staff by minimum salary
    @GetMapping("/salary/{minSalary}")
    public ResponseEntity<List<Staff>> getStaffByMinimumSalary(@PathVariable Double minSalary) {
        List<Staff> staffList = staffService.getStaffByMinimumSalary(minSalary);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    // GET search staff by name
    @GetMapping("/search")
    public ResponseEntity<List<Staff>> searchStaffByName(@RequestParam String name) {
        List<Staff> staffList = staffService.searchStaffByName(name);
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }
}