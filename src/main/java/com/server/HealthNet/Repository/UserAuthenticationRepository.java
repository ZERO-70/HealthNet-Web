package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.UserAuthentication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAuthenticationRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserAuthenticationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(UserAuthentication userAuthentication) {
        String sql = "INSERT INTO user_authentication (username, password, role, person_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, userAuthentication.getUsername(), userAuthentication.getPassword(),
                userAuthentication.getRole().name(), userAuthentication.getPersonId());
    }

    public UserAuthentication findByUsername(String username) {
        String sql = "SELECT * FROM user_authentication WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(UserAuthentication.class), username);
    }

    public List<UserAuthentication> findAll() {
        String sql = "SELECT * FROM user_authentication";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserAuthentication.class));
    }

    public int update(UserAuthentication userAuthentication) {
        String sql = "UPDATE user_authentication SET password = ?, role = ?, person_id = ? WHERE username = ?";
        return jdbcTemplate.update(sql, userAuthentication.getPassword(), userAuthentication.getRole().name(),
                userAuthentication.getPersonId(), userAuthentication.getUsername());
    }

    public int deleteByUsername(String username) {
        String sql = "DELETE FROM user_authentication WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }
}
