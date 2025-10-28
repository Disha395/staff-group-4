package com.college.management.controller;

import com.college.management.entity.Staff;
import com.college.management.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
@Tag(name = "Staff Management", description = "APIs for managing staff members in the college system")
public class StaffController {

    // Logger instance for this controller
    private static final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @Autowired
    private StaffService staffService;

    // ==================== GET ALL STAFF ====================
    @Operation(
            summary = "Get all staff members",
            description = "Retrieves a list of all staff members in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all staff members",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Staff.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        logger.info("Received request to get all staff members");

        try {
            List<Staff> staffList = staffService.getAllStaff();
            logger.info("Successfully retrieved {} staff members", staffList.size());
            return new ResponseEntity<>(staffList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving all staff members: {}", e.getMessage(), e);
            throw e;
        }
    }

    // ==================== GET STAFF BY ID ====================
    @Operation(
            summary = "Get staff by ID",
            description = "Retrieves a specific staff member by their unique ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Staff member found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Staff.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(
            @Parameter(description = "ID of the staff member to retrieve", required = true)
            @PathVariable Long id) {

        logger.info("Received request to get staff with ID: {}", id);

        try {
            Optional<Staff> staff = staffService.getStaffById(id);

            if (staff.isPresent()) {
                logger.info("Successfully retrieved staff with ID: {}", id);
                return new ResponseEntity<>(staff.get(), HttpStatus.OK);
            } else {
                logger.warn("Staff with ID: {} not found", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error retrieving staff with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ==================== CREATE NEW STAFF ====================
    @Operation(
            summary = "Create new staff member",
            description = "Creates a new staff member in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Staff member created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Staff.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    @PostMapping
    public ResponseEntity<Staff> createStaff(
            @Parameter(description = "Staff member details", required = true)
            @Valid @RequestBody Staff staff) {

        logger.info("Received request to create new staff: {}", staff.getStaffName());
        logger.debug("Staff details - Name: {}, Department: {}, Salary: {}",
                staff.getStaffName(), staff.getDepartmentId(), staff.getSalary());

        try {
            Staff createdStaff = staffService.createStaff(staff);
            logger.info("Successfully created staff with ID: {} and name: {}",
                    createdStaff.getStaffId(), createdStaff.getStaffName());
            return new ResponseEntity<>(createdStaff, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating staff {}: {}", staff.getStaffName(), e.getMessage(), e);
            throw e;
        }
    }

    // ==================== UPDATE STAFF ====================
    @Operation(
            summary = "Update staff member",
            description = "Updates an existing staff member's information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Staff member updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Staff.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(
            @Parameter(description = "ID of the staff member to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated staff member details", required = true)
            @Valid @RequestBody Staff staff) {

        logger.info("Received request to update staff with ID: {}", id);
        logger.debug("Updated staff details - Name: {}, Department: {}, Salary: {}",
                staff.getStaffName(), staff.getDepartmentId(), staff.getSalary());

        try {
            Staff updatedStaff = staffService.updateStaff(id, staff);
            logger.info("Successfully updated staff with ID: {}", id);
            return new ResponseEntity<>(updatedStaff, HttpStatus.OK);
        } catch (RuntimeException e) {
            logger.error("Error updating staff with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error updating staff with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ==================== DELETE STAFF ====================
    @Operation(
            summary = "Delete staff member",
            description = "Deletes a staff member from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Staff member deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Staff member not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(
            @Parameter(description = "ID of the staff member to delete", required = true)
            @PathVariable Long id) {

        logger.info("Received request to delete staff with ID: {}", id);

        try {
            staffService.deleteStaff(id);
            logger.info("Successfully deleted staff with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Error deleting staff with ID {}: Staff not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Unexpected error deleting staff with ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // ==================== GET STAFF BY DEPARTMENT ====================
    @Operation(
            summary = "Get staff by department",
            description = "Retrieves all staff members belonging to a specific department"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved staff by department",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Staff.class)
                    )
            )
    })
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<Staff>> getStaffByDepartment(
            @Parameter(description = "Department ID to filter staff", required = true)
            @PathVariable Long departmentId) {

        logger.info("Received request to get staff by department ID: {}", departmentId);

        try {
            List<Staff> staffList = staffService.getStaffByDepartment(departmentId);
            logger.info("Successfully retrieved {} staff members from department ID: {}",
                    staffList.size(), departmentId);
            return new ResponseEntity<>(staffList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving staff by department {}: {}", departmentId, e.getMessage(), e);
            throw e;
        }
    }

    // ==================== GET STAFF BY MINIMUM SALARY ====================
    @Operation(
            summary = "Get staff by minimum salary",
            description = "Retrieves all staff members with salary greater than or equal to specified amount"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved staff by salary",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Staff.class)
                    )
            )
    })
    @GetMapping("/salary/{minSalary}")
    public ResponseEntity<List<Staff>> getStaffByMinimumSalary(
            @Parameter(description = "Minimum salary threshold", required = true)
            @PathVariable Double minSalary) {

        logger.info("Received request to get staff with minimum salary: {}", minSalary);

        try {
            List<Staff> staffList = staffService.getStaffByMinimumSalary(minSalary);
            logger.info("Successfully retrieved {} staff members with salary >= {}",
                    staffList.size(), minSalary);
            return new ResponseEntity<>(staffList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving staff by minimum salary {}: {}", minSalary, e.getMessage(), e);
            throw e;
        }
    }

    // ==================== SEARCH STAFF BY NAME ====================
    @Operation(
            summary = "Search staff by name",
            description = "Searches for staff members by name (case-insensitive partial match)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved matching staff members",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Staff.class)
                    )
            )
    })
    @GetMapping("/search")
    public ResponseEntity<List<Staff>> searchStaffByName(
            @Parameter(description = "Name to search for (partial match supported)", required = true)
            @RequestParam String name) {

        logger.info("Received request to search staff by name: {}", name);

        try {
            List<Staff> staffList = staffService.searchStaffByName(name);
            logger.info("Successfully found {} staff members matching name: {}",
                    staffList.size(), name);
            return new ResponseEntity<>(staffList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error searching staff by name {}: {}", name, e.getMessage(), e);
            throw e;
        }
    }
}