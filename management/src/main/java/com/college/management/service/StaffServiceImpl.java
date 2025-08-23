package com.college.management.service;

import com.college.management.entity.Staff;
import com.college.management.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffServiceImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Staff> getStaffById(Long id) {
        return staffRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getAllStaff() {
        return staffRepository.findAllByOrderByStaffNameAsc();
    }

    @Override
    public Staff updateStaff(Long id, Staff staff) {
        return staffRepository.findById(id)
                .map(existingStaff -> {
                    existingStaff.setStaffName(staff.getStaffName());
                    existingStaff.setDepartment(staff.getDepartment());
                    existingStaff.setSalary(staff.getSalary());
                    return staffRepository.save(existingStaff);
                })
                .orElseThrow(() -> new RuntimeException("Staff not found with id: " + id));
    }

    @Override
    public boolean deleteStaff(Long id) {
        return staffRepository.findById(id)
                .map(staff -> {
                    staffRepository.delete(staff);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getStaffByName(String name) {
        return staffRepository.findByStaffNameContaining(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getStaffByDepartmentId(Long departmentId) {
        return staffRepository.findByDepartmentDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getStaffByDepartmentName(String departmentName) {
        return staffRepository.findByDepartmentName(departmentName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> getStaffBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return staffRepository.findBySalaryBetween(minSalary, maxSalary);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Staff> getHighestPaidStaff() {
        return staffRepository.findTopByOrderBySalaryDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public long getStaffCountByDepartment(Long departmentId) {
        return staffRepository.countByDepartmentId(departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return staffRepository.existsById(id);
    }
}