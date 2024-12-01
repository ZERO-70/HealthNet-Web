package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.Appointment;
import com.server.HealthNet.Model.Avalibility;
import com.server.HealthNet.Model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AvalibilityRepository avalibilityRepository;

    private final RowMapper<Doctor> doctorRowMapper = new RowMapper<Doctor>() {
        @Override
        public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Doctor doctor = new Doctor();
            doctor.setId(rs.getLong("person_id"));
            doctor.setName(rs.getString("name"));
            doctor.setGender(rs.getString("gender"));
            doctor.setAge(rs.getInt("age"));
            doctor.setBirthdate(rs.getDate("birthdate").toLocalDate());
            doctor.setContact_info(rs.getString("contact_info"));
            doctor.setAddress(rs.getString("address"));
            doctor.setSpecialization(rs.getString("specialization"));
            return doctor;
        }
    };

    public Optional<Doctor> findDoctorById(Long doctorId) {
        String sql = "SELECT p.person_id, p.name, p.gender, p.age, p.birthdate, p.contact_info, p.address, " +
                "d.specialization " +
                "FROM doctor d " +
                "JOIN person p ON d.doctor_id = p.person_id " +
                "WHERE d.doctor_id = ?";
        return jdbcTemplate.query(sql, doctorRowMapper, doctorId).stream().findFirst();
    }

    public List<Doctor> findAllDoctors() {
        String sql = "SELECT p.person_id, p.name, p.gender, p.age, p.birthdate, p.contact_info, p.address, " +
                "d.specialization " +
                "FROM doctor d " +
                "JOIN person p ON d.doctor_id = p.person_id";
        return jdbcTemplate.query(sql, doctorRowMapper);
    }

    // Method to delete a doctor by their ID
    public int deleteDoctorById(Long doctorId) {
        // First, delete the doctor record from the doctor table
        String deleteDoctorSql = "DELETE FROM doctor WHERE doctor_id = ?";
        int rowsAffected = jdbcTemplate.update(deleteDoctorSql, doctorId);

        // Then, delete the associated record from the person table
        String deletePersonSql = "DELETE FROM person WHERE person_id = ?";
        rowsAffected += jdbcTemplate.update(deletePersonSql, doctorId);

        return rowsAffected; // Return the total number of rows affected
    }

    // Method to update doctor details
    public int updateDoctor(Doctor doctor) {
        String sql = "UPDATE doctor SET specialization = ? WHERE doctor_id = ?";
        int rowsAffected = jdbcTemplate.update(sql,
                doctor.getSpecialization(),
                doctor.getId());

        // Also update fields in the Person table
        String personSql = "UPDATE person SET name = ?, gender = ?, age = ?, birthdate = ?, " +
                "contact_info = ?, address = ? WHERE person_id = ?";
        rowsAffected += jdbcTemplate.update(personSql,
                doctor.getName(),
                doctor.getGender(),
                doctor.getAge(),
                doctor.getBirthdate(),
                doctor.getContact_info(),
                doctor.getAddress(),
                doctor.getId());

        return rowsAffected;
    }

    public Long saveDoctor(Doctor doctor) {
        // Insert into Person table and retrieve the generated person_id
        String personSql = "INSERT INTO person (name, gender, age, birthdate, contact_info, address, image, image_type) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(personSql, new String[] { "person_id" });
            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getGender());
            ps.setInt(3, doctor.getAge());
            ps.setDate(4, java.sql.Date.valueOf(doctor.getBirthdate()));
            ps.setString(5, doctor.getContact_info());
            ps.setString(6, doctor.getAddress());
            if (doctor.getImage() != null) {
                ps.setBytes(7, doctor.getImage());
            } else {
                ps.setNull(7, java.sql.Types.BLOB); // Handle case where no image is provided
            }
            if (doctor.getImage_type() != null) {
                ps.setString(8, doctor.getImage_type());
            } else {
                ps.setNull(8, java.sql.Types.VARCHAR); // Handle case where no image type is provided
            }
            return ps;
        }, keyHolder);

        // Retrieve the generated person_id to use as doctor_id
        Long generatedDoctorId = keyHolder.getKey().longValue();

        // Insert into Doctor table using the generated doctor_id
        String doctorSql = "INSERT INTO doctor (doctor_id, specialization) VALUES (?, ?)";
        if (jdbcTemplate.update(doctorSql, generatedDoctorId, doctor.getSpecialization()) > 0) {
            return generatedDoctorId;
        } else {
            return 0L;
        }
    }

    public List<String> getAvailableAppointmentTimes(Long doctorId, LocalDate date) {
        Optional<Avalibility> availabilityOpt = avalibilityRepository.findById(doctorId);
        if (availabilityOpt.isEmpty()) {
            return List.of();
        }

        Avalibility availability = availabilityOpt.get();

        // Query to fetch appointments
        String appointmentsSql = "SELECT start_time, end_time FROM appointments WHERE doctor_id = ? AND date = ?";
        List<Appointment> appointments = jdbcTemplate.query(appointmentsSql, (rs, rowNum) -> {
            Appointment appointment = new Appointment();
            appointment.setStartTime(rs.getObject("start_time", LocalTime.class));
            appointment.setEndTime(rs.getObject("end_time", LocalTime.class));
            return appointment;
        }, doctorId, date);

        // Determine start and end times based on the day of the week
        LocalTime dayStart, dayEnd;
        switch (date.getDayOfWeek()) {
            case MONDAY -> {
                dayStart = availability.getMon_startTime();
                dayEnd = availability.getMon_endTime();
            }
            case TUESDAY -> {
                dayStart = availability.getTue_startTime();
                dayEnd = availability.getTue_endTime();
            }
            case WEDNESDAY -> {
                dayStart = availability.getWed_startTime();
                dayEnd = availability.getWed_endTime();
            }
            case THURSDAY -> {
                dayStart = availability.getThu_startTime();
                dayEnd = availability.getThu_endTime();
            }
            case FRIDAY -> {
                dayStart = availability.getFri_startTime();
                dayEnd = availability.getFri_endTime();
            }
            case SATURDAY -> {
                dayStart = availability.getSat_startTime();
                dayEnd = availability.getSat_endTime();
            }
            case SUNDAY -> {
                dayStart = availability.getSun_startTime();
                dayEnd = availability.getSun_endTime();
            }
            default -> {
                return List.of();
            }
        }

        // Check for null dayStart and dayEnd
        if (dayStart == null || dayEnd == null) {
            return List.of();
        }

        // Sort appointments by start time
        appointments.sort(Comparator.comparing(Appointment::getStartTime));

        List<String> availableTimes = new ArrayList<>();
        LocalTime currentStart = dayStart;

        for (Appointment appt : appointments) {
            // Check if there is a gap between currentStart and the next appointment's start
            // time
            if (!appt.getStartTime().isBefore(currentStart)) {
                System.out.println("apt ka start :" + appt.getStartTime() + " cur start : " + currentStart);
                if (appt.getStartTime().isAfter(currentStart)) {
                    availableTimes.add(currentStart + " - " + appt.getStartTime());
                    System.out.println(" added    :" + currentStart + " - " + appt.getStartTime());
                }
                // Update currentStart to the end of this appointment
                currentStart = appt.getEndTime();
                System.out.println(" updated cur start time : " + currentStart);
            } else if (appt.getEndTime().isAfter(currentStart)) {
                System.out.println("apt ka start :" + appt.getStartTime() + " cur start : " + currentStart);
                // Handle overlapping appointments
                currentStart = appt.getEndTime();
                System.out.println(" updated cur start time : " + currentStart);
            }
        }

        // Add the last available slot if it exists
        if (currentStart.isBefore(dayEnd)) {
            availableTimes.add(currentStart + " - " + dayEnd);
            System.out.println(" added    :" + currentStart + " - " + dayEnd);
        }

        return availableTimes;
    }

}
