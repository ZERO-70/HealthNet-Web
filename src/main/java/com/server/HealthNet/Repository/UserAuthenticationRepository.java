package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.Role;
import com.server.HealthNet.Model.UserAuthentication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserAuthenticationRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserAuthenticationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(UserAuthentication userAuthentication) {
        String sql = "INSERT INTO user_authentication (username, password, role, person_id) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                userAuthentication.getUsername(),
                userAuthentication.getPassword(),
                userAuthentication.getRole().name(), // Use .name() for enum to string conversion
                userAuthentication.getPersonId()
        );
    }

    public UserAuthentication findByUsername(String username) {
        String sql = "SELECT * FROM user_authentication WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new UserAuthenticationRowMapper(), username);
    }

    public List<UserAuthentication> findAll() {
        String sql = "SELECT * FROM user_authentication";
        return jdbcTemplate.query(sql, new UserAuthenticationRowMapper());
    }

    public int update(UserAuthentication userAuthentication) {
        String sql = "UPDATE user_authentication SET password = ?, role = ?, person_id = ? WHERE username = ?";
        return jdbcTemplate.update(
                sql,
                userAuthentication.getPassword(),
                userAuthentication.getRole().name(),
                userAuthentication.getPersonId(),
                userAuthentication.getUsername()
        );
    }

    public int deleteByUsername(String username) {
        String sql = "DELETE FROM user_authentication WHERE username = ?";
        return jdbcTemplate.update(sql, username);
    }

    /**
     * Custom RowMapper to map database rows to UserAuthentication objects.
     */
    private static class UserAuthenticationRowMapper implements RowMapper<UserAuthentication> {
        @Override
        public UserAuthentication mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserAuthentication user = new UserAuthentication();
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setPersonId(rs.getLong("person_id"));

            // Convert database string value to Role enum
            String roleStr = rs.getString("role");
            user.setRole(Role.valueOf(roleStr.toUpperCase())); // Assumes Role is an enum

            return user;
        }
    }
}
