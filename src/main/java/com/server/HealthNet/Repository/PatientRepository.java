package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.Patient;
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
public class PatientRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Patient> patientRowMapper = new RowMapper<Patient>() {
        @Override
        public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
            Patient patient = new Patient();
            patient.setId(rs.getLong("person_id"));
            patient.setName(rs.getString("name"));
            patient.setGender(rs.getString("gender"));
            patient.setAge(rs.getInt("age"));
            patient.setBirthdate(rs.getDate("birthdate").toLocalDate());
            patient.setContact_info(rs.getString("contact_info"));
            patient.setAddress(rs.getString("address"));
            patient.setWeight(rs.getString("weight"));
            patient.setHeight(rs.getString("height"));
            return patient;
        }
    };

    public Optional<Patient> findPatientById(Long patientId) {
        String sql = "SELECT p.person_id, p.name, p.gender, p.age, p.birthdate, p.contact_info, p.address, " +
                     "pat.weight, pat.height " +
                     "FROM patient pat " +
                     "JOIN person p ON pat.patient_id = p.person_id " +
                     "WHERE pat.patient_id = ?";
        return jdbcTemplate.query(sql, patientRowMapper, patientId).stream().findFirst();
    }

    public List<Patient> findAllPatients() {
        String sql = "SELECT p.person_id, p.name, p.gender, p.age, p.birthdate, p.contact_info, p.address, " +
                     "pat.weight, pat.height " +
                     "FROM patient pat " +
                     "JOIN person p ON pat.patient_id = p.person_id";
        return jdbcTemplate.query(sql, patientRowMapper);
    }

    public int deletePatientById(Long patientId) {
        String sql = "DELETE FROM patient WHERE patient_id = ?";
        return jdbcTemplate.update(sql, patientId);
    }

    public int updatePatient(Patient patient) {
        String sql = "UPDATE patient SET weight = ?, height = ? WHERE patient_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, patient.getWeight(), patient.getHeight(), patient.getId());

        String personSql = "UPDATE person SET name = ?, gender = ?, age = ?, birthdate = ?, " +
                           "contact_info = ?, address = ? WHERE person_id = ?";
        rowsAffected += jdbcTemplate.update(personSql,
                                            patient.getName(),
                                            patient.getGender(),
                                            patient.getAge(),
                                            patient.getBirthdate(),
                                            patient.getContact_info(),
                                            patient.getAddress(),
                                            patient.getId());

        return rowsAffected;
    }

    public int savePatient(Patient patient) {
        String personSql = "INSERT INTO person (name, gender, age, birthdate, contact_info, address) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(personSql, new String[] {"person_id"});
            ps.setString(1, patient.getName());
            ps.setString(2, patient.getGender());
            ps.setInt(3, patient.getAge());
            ps.setDate(4, java.sql.Date.valueOf(patient.getBirthdate()));
            ps.setString(5, patient.getContact_info());
            ps.setString(6, patient.getAddress());
            return ps;
        }, keyHolder);
        
        Long generatedPatientId = keyHolder.getKey().longValue();
        
        String patientSql = "INSERT INTO patient (patient_id, weight, height) VALUES (?, ?, ?)";
        return jdbcTemplate.update(patientSql, generatedPatientId, patient.getWeight(), patient.getHeight());
    }
}
