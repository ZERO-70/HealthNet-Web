package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class AppointmentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Appointment mapRowToAppointment(ResultSet rs, int rowNum) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointment_id(rs.getLong("appointment_id"));
        appointment.setPatient_id(rs.getLong("patient_id"));
        appointment.setDoctor_id(rs.getLong("doctor_id"));
        appointment.setDate(rs.getObject("date", LocalDate.class));
        appointment.setTime(rs.getObject("time", LocalTime.class));
        appointment.setIs_pending(rs.getBoolean("is_pending"));
        appointment.setIs_approved(rs.getBoolean("is_approved")); // Added mapping for is_approved
        return appointment;
    }

    public List<Appointment> findAll() {
        String sql = "SELECT * FROM appointments";
        return jdbcTemplate.query(sql, this::mapRowToAppointment);
    }

    public Appointment findById(Long id) {
        String sql = "SELECT * FROM appointments WHERE appointment_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToAppointment, id);
    }

    public int save(Appointment appointment) {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, date, time, is_pending, is_approved) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, appointment.getPatient_id(), appointment.getDoctor_id(),
                appointment.getDate(), appointment.getTime(), appointment.isIs_pending(), appointment.isIs_approved()); // Added is_approved
    }

    public int update(Appointment appointment) {
        String sql = "UPDATE appointments SET patient_id = ?, doctor_id = ?, date = ?, time = ?, is_pending = ?, is_approved = ? WHERE appointment_id = ?";
        return jdbcTemplate.update(sql, appointment.getPatient_id(), appointment.getDoctor_id(),
                appointment.getDate(), appointment.getTime(), appointment.isIs_pending(), appointment.isIs_approved(), appointment.getAppointment_id()); // Added is_approved
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public List<Appointment> findAllByPatientId(Long patientId) {
        String sql = "SELECT * FROM appointments WHERE patient_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToAppointment, patientId);
    }

    public List<Appointment> findAllByDoctorId(Long doctorId) {
        String sql = "SELECT * FROM appointments WHERE doctor_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToAppointment, doctorId);
    }
}
