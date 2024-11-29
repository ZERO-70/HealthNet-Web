package com.server.HealthNet.Service;

import com.server.HealthNet.Model.Appointment;
import com.server.HealthNet.Repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public int createAppointment(Appointment appointment) {
        long durationMinutes = java.time.Duration.between(appointment.getStartTime(), appointment.getEndTime()).toMinutes();
        if (durationMinutes < 5 || durationMinutes > 120) {
            return 3; 
        }
    
        if (appointmentRepository.isAppointmentOverlapping(
                appointment.getDoctor_id(), appointment.getDate(), appointment.getStartTime(), appointment.getEndTime())) {
            return 2;
        }
    
        appointment.setIs_approved(false);
        return appointmentRepository.save(appointment);
    }
    

    public int updateAppointment(Appointment appointment) {
        return appointmentRepository.update(appointment);
    }

    public int deleteAppointment(Long id) {
        return appointmentRepository.deleteById(id);
    }

    public List<Appointment> getallbydoctorid(Long id) {
        return appointmentRepository.findAllByDoctorId(id);
    }

    public List<Appointment> getallbypatientid(Long id) {
        return appointmentRepository.findAllByPatientId(id);
    }

    // New method to approve an appointment
    public int approveAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id);
        if (appointment != null) {
            appointment.setIs_approved(true); // Approve the appointment
            return appointmentRepository.update(appointment);
        }
        return 0; // Return 0 if appointment not found
    }
}
