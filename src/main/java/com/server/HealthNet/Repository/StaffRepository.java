package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.Staff;
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
public class StaffRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Staff> staffRowMapper = new RowMapper<Staff>() {
        @Override
        public Staff mapRow(ResultSet rs, int rowNum) throws SQLException {
            Staff staff = new Staff();
            staff.setId(rs.getLong("person_id"));
            staff.setName(rs.getString("name"));
            staff.setGender(rs.getString("gender"));
            staff.setAge(rs.getInt("age"));
            staff.setBirthdate(rs.getDate("birthdate").toLocalDate());
            staff.setContact_info(rs.getString("contact_info"));
            staff.setAddress(rs.getString("address"));
            staff.setProffession(rs.getString("proffession"));
            return staff;
        }
    };

    public Optional<Staff> findStaffById(Long staffId) {
        String sql = "SELECT p.person_id, p.name, p.gender, p.age, p.birthdate, p.contact_info, p.address, " +
                     "s.proffession " +
                     "FROM staff s " +
                     "JOIN person p ON s.staff_id = p.person_id " +
                     "WHERE s.staff_id = ?";
        return jdbcTemplate.query(sql, staffRowMapper, staffId).stream().findFirst();
    }

    public List<Staff> findAllStaff() {
        String sql = "SELECT p.person_id, p.name, p.gender, p.age, p.birthdate, p.contact_info, p.address, " +
                     "s.proffession " +
                     "FROM staff s " +
                     "JOIN person p ON s.staff_id = p.person_id";
        return jdbcTemplate.query(sql, staffRowMapper);
    }

    public int deleteStaffById(Long staffId) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";
        return jdbcTemplate.update(sql, staffId);
    }

    public int updateStaff(Staff staff) {
        String sql = "UPDATE staff SET proffession = ? WHERE staff_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, staff.getProffession(), staff.getId());

        String personSql = "UPDATE person SET name = ?, gender = ?, age = ?, birthdate = ?, " +
                           "contact_info = ?, address = ? WHERE person_id = ?";
        rowsAffected += jdbcTemplate.update(personSql,
                                            staff.getName(),
                                            staff.getGender(),
                                            staff.getAge(),
                                            staff.getBirthdate(),
                                            staff.getContact_info(),
                                            staff.getAddress(),
                                            staff.getId());

        return rowsAffected;
    }

    public Long saveStaff(Staff staff) {
        String personSql = "INSERT INTO person (name, gender, age, birthdate, contact_info, address) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(personSql, new String[] {"person_id"});
            ps.setString(1, staff.getName());
            ps.setString(2, staff.getGender());
            ps.setInt(3, staff.getAge());
            ps.setDate(4, java.sql.Date.valueOf(staff.getBirthdate()));
            ps.setString(5, staff.getContact_info());
            ps.setString(6, staff.getAddress());
            return ps;
        }, keyHolder);
        
        Long generatedStaffId = keyHolder.getKey().longValue();
        
        String staffSql = "INSERT INTO staff (staff_id, proffession) VALUES (?, ?)";
        if (jdbcTemplate.update(staffSql, generatedStaffId, staff.getProffession())>0){
            return generatedStaffId;
        }
        else{
            return 0L;
        }
    }
}
