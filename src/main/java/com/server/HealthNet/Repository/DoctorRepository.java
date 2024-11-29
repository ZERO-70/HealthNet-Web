package com.server.HealthNet.Repository;

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
import java.util.List;
import java.util.Optional;

@Repository
public class DoctorRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        String sql = "DELETE FROM doctor WHERE doctor_id = ?";
        return jdbcTemplate.update(sql, doctorId);
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
    String personSql = "INSERT INTO person (name, gender, age, birthdate, contact_info, address) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    
    jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(personSql, new String[] {"person_id"});
        ps.setString(1, doctor.getName());
        ps.setString(2, doctor.getGender());
        ps.setInt(3, doctor.getAge());
        ps.setDate(4, java.sql.Date.valueOf(doctor.getBirthdate()));
        ps.setString(5, doctor.getContact_info());
        ps.setString(6, doctor.getAddress());
        return ps;
    }, keyHolder);
    
    // Retrieve the generated person_id to use as doctor_id
    Long generatedDoctorId = keyHolder.getKey().longValue();
    
    // Insert into Doctor table using the generated doctor_id
    String doctorSql = "INSERT INTO doctor (doctor_id, specialization) VALUES (?, ?)";
    if( jdbcTemplate.update(doctorSql,
                               generatedDoctorId,
                               doctor.getSpecialization()) > 0){
                                return generatedDoctorId;
                               }
                               else{
                                return 0L;
                               }
    }
    
}
