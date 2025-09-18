package com.college.management.service;

import com.college.management.entity.Staff;
import com.college.management.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Optional<Staff> getStaffById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    public Staff createStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public Staff updateStaff(Long id, Staff staff) {
        if (staffRepository.existsById(id)) {
            staff.setStaffId(id);
            return staffRepository.save(staff);
        }
        throw new RuntimeException("Staff not found with id: " + id);
    }

    @Override
    public void deleteStaff(Long id) {
        if (staffRepository.existsById(id)) {
            staffRepository.deleteById(id);
        } else {
            throw new RuntimeException("Staff not found with id: " + id);
        }
    }

    @Override
    public List<Staff> getStaffByDepartment(Long departmentId) {
        return staffRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Staff> getStaffByMinimumSalary(Double minSalary) {
        return staffRepository.findByMinimumSalary(minSalary);
    }

    @Override
    public List<Staff> searchStaffByName(String name) {
        return staffRepository.findByStaffNameContainingIgnoreCase(name);
    }
}