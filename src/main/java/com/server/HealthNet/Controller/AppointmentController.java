package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.Appointment;
import com.server.HealthNet.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PostMapping
    public ResponseEntity<String> createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment)>0 ? new ResponseEntity<>("Appointment Insertion successfully", HttpStatus.OK):new ResponseEntity<>("Appoitment Insertion failed", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        appointment.setAppointment_id(id);
        return appointmentService.updateAppointment(appointment)>0 ? new ResponseEntity<>("Appointment Updated successfully", HttpStatus.OK):new ResponseEntity<>("Appointment Update failed", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        return appointmentService.deleteAppointment(id)>0 ? new ResponseEntity<>("Appointment Deletion successfully", HttpStatus.OK):new ResponseEntity<>("Appointment Deleted failed", HttpStatus.NOT_FOUND);
    }
}
