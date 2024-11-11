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
        return appointmentRepository.save(appointment);
    }

    public int updateAppointment(Appointment appointment) {
        return appointmentRepository.update(appointment);
    }

    public int deleteAppointment(Long id) {
        return appointmentRepository.deleteById(id);
    }
}
