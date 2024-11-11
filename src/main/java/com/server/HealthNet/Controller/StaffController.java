package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.Staff;
import com.server.HealthNet.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/staffs")
public class StaffController {

    @Autowired
    private StaffService staffService;

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        List<Staff> staffList = staffService.getAllStaff();
        return new ResponseEntity<>(staffList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addStaff(@RequestBody Staff staff) {
        staffService.addStaff(staff);
        return new ResponseEntity<>("Staff added successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateStaff(@RequestBody Staff staff) {
        int rowsAffected = staffService.updateStaff(staff);
        if (rowsAffected > 0) {
            return new ResponseEntity<>("Staff updated successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Staff update failed", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaffById(@PathVariable Long id) {
        int rowsAffected = staffService.deleteStaffById(id);
        if (rowsAffected > 0) {
            return new ResponseEntity<>("Staff deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Staff deletion failed", HttpStatus.NOT_FOUND);
        }
    }
}
