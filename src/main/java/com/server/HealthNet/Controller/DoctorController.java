package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.Doctor;
import com.server.HealthNet.Model.Role;
import com.server.HealthNet.Model.UserAuthentication;
import com.server.HealthNet.Service.DoctorService;
import com.server.HealthNet.Service.UserAuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<String> addDoctor(@RequestBody Doctor doctor) {
        int result = doctorService.saveDoctor(doctor);
        if (result > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Doctor added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add doctor");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN') or hasRole('PATIENT') or hasRole('STAFF')")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAuthentication userAuthentication = userAuthenticationService.getUserByUsername(username);
        if (userAuthentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // i hope this works
        if (!userAuthentication.getPersonId().equals(id) && userAuthentication.getRole() != Role.ADMIN
        && userAuthentication.getRole() != Role.PATIENT && userAuthentication.getRole() != Role.STAFF) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Doctor doctor = doctorService.getDoctorById(id);
        return doctor != null ? ResponseEntity.ok(doctor) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @PutMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> updateDoctor(@RequestBody Doctor doctor) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals(doctor.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        int result = doctorService.updateDoctor(doctor);
        if (result > 0) {
            return ResponseEntity.ok("Doctor updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update doctor");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long id) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserAuthentication userAuthentication = userAuthenticationService.getUserByUsername(username);
        if (userAuthentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // i hope this works
        if (!userAuthentication.getPersonId().equals(id) && userAuthentication.getRole() != Role.ADMIN
        && userAuthentication.getRole() != Role.PATIENT && userAuthentication.getRole() != Role.STAFF) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        int result = doctorService.deleteDoctorById(id);
        if (result > 0) {
            return ResponseEntity.ok("Doctor deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete doctor");
        }
    }
}
