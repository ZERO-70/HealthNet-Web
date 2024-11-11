package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.Avalibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class AvalibilityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Avalibility avalibility) {
        String sql = "INSERT INTO Avalibility (doctor_id ,Mon_startTime, Mon_endTime, Tue_startTime, Tue_endTime, Wed_startTime, " +
                     "Wed_endTime, Thu_startTime, Thu_endTime, Fri_startTime, Fri_endTime, Sat_startTime, Sat_endTime, " +
                     "Sun_startTime, Sun_endTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,avalibility.getDoctor_id(), avalibility.getMon_startTime(), avalibility.getMon_endTime(), 
                                    avalibility.getTue_startTime(), avalibility.getTue_endTime(),
                                    avalibility.getWed_startTime(), avalibility.getWed_endTime(),
                                    avalibility.getThu_startTime(), avalibility.getThu_endTime(),
                                    avalibility.getFri_startTime(), avalibility.getFri_endTime(),
                                    avalibility.getSat_startTime(), avalibility.getSat_endTime(),
                                    avalibility.getSun_startTime(), avalibility.getSun_endTime());
    }

    public List<Avalibility> findAll() {
        String sql = "SELECT * FROM Avalibility";
        return jdbcTemplate.query(sql, new AvalibilityRowMapper());
    }

    public Optional<Avalibility> findById(Long id) {
        String sql = "SELECT * FROM Avalibility WHERE doctor_id = ?";
        return jdbcTemplate.query(sql, new AvalibilityRowMapper(), id).stream().findFirst();
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM Avalibility WHERE doctor_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int update(Long id, Avalibility avalibility) {
        String sql = "UPDATE Avalibility SET Mon_startTime = ?, Mon_endTime = ?, Tue_startTime = ?, Tue_endTime = ?, " +
                     "Wed_startTime = ?, Wed_endTime = ?, Thu_startTime = ?, Thu_endTime = ?, Fri_startTime = ?, " +
                     "Fri_endTime = ?, Sat_startTime = ?, Sat_endTime = ?, Sun_startTime = ?, Sun_endTime = ? " +
                     "WHERE doctor_id = ?";
        return jdbcTemplate.update(sql, avalibility.getMon_startTime(), avalibility.getMon_endTime(), 
                                   avalibility.getTue_startTime(), avalibility.getTue_endTime(),
                                   avalibility.getWed_startTime(), avalibility.getWed_endTime(),
                                   avalibility.getThu_startTime(), avalibility.getThu_endTime(),
                                   avalibility.getFri_startTime(), avalibility.getFri_endTime(),
                                   avalibility.getSat_startTime(), avalibility.getSat_endTime(),
                                   avalibility.getSun_startTime(), avalibility.getSun_endTime(), id);
    }

    private static class AvalibilityRowMapper implements RowMapper<Avalibility> {
        @Override
        public Avalibility mapRow(ResultSet rs, int rowNum) throws SQLException {
            Avalibility avalibility = new Avalibility();
            avalibility.setDoctor_id(rs.getLong("doctor_id"));
            avalibility.setMon_startTime(rs.getTime("Mon_startTime").toLocalTime());
            avalibility.setMon_endTime(rs.getTime("Mon_endTime").toLocalTime());
            avalibility.setTue_startTime(rs.getTime("Tue_startTime").toLocalTime());
            avalibility.setTue_endTime(rs.getTime("Tue_endTime").toLocalTime());
            avalibility.setWed_startTime(rs.getTime("Wed_startTime").toLocalTime());
            avalibility.setWed_endTime(rs.getTime("Wed_endTime").toLocalTime());
            avalibility.setThu_startTime(rs.getTime("Thu_startTime").toLocalTime());
            avalibility.setThu_endTime(rs.getTime("Thu_endTime").toLocalTime());
            avalibility.setFri_startTime(rs.getTime("Fri_startTime").toLocalTime());
            avalibility.setFri_endTime(rs.getTime("Fri_endTime").toLocalTime());
            avalibility.setSat_startTime(rs.getTime("Sat_startTime").toLocalTime());
            avalibility.setSat_endTime(rs.getTime("Sat_endTime").toLocalTime());
            avalibility.setSun_startTime(rs.getTime("Sun_startTime") != null ? rs.getTime("Sun_startTime").toLocalTime() : null);
            avalibility.setSun_endTime(rs.getTime("Sun_endTime") != null ? rs.getTime("Sun_endTime").toLocalTime() : null);
            return avalibility;
        }
    }
}
