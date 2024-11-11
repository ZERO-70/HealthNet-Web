package com.server.HealthNet.Repository;

import com.server.HealthNet.Model.Person;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonRepository { 
    private final JdbcTemplate jdbcTemplate;

    public PersonRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> findAll() {
        String sql = "SELECT * FROM Person";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Person person = new Person();
            person.setId(rs.getLong("person_id"));
            person.setName(rs.getString("name"));
            person.setGender(rs.getString("gender"));
            person.setAge(rs.getInt("age"));
            person.setBirthdate(rs.getDate("birthdate").toLocalDate());
            person.setContact_info(rs.getString("contact_info"));
            person.setAddress(rs.getString("address"));
            // Handle image and other fields
            return person;
        });
    }

    public Person findById(Long id) {
        String sql = "SELECT * FROM Person WHERE person_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Person person = new Person();
            person.setId(rs.getLong("person_id"));
            person.setName(rs.getString("name"));
            person.setGender(rs.getString("gender"));
            person.setAge(rs.getInt("age"));
            person.setBirthdate(rs.getDate("birthdate").toLocalDate());
            person.setContact_info(rs.getString("contact_info"));
            person.setAddress(rs.getString("address"));
            // Handle image and other fields
            return person;
        });
    }

    public int save(Person person) {
        String sql = "INSERT INTO Person (name, gender, age, birthdate, contact_info, address) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, person.getName(), person.getGender(), person.getAge(), person.getBirthdate(), person.getContact_info(), person.getAddress());
        // Handle image and other fields
    }

    public int update(Person person) {
        String sql = "UPDATE Person SET name = ?, gender = ?, age = ?, birthdate = ?, contact_info = ?, address = ? WHERE person_id = ?";
        return jdbcTemplate.update(sql, person.getName(), person.getGender(), person.getAge(), person.getBirthdate(), person.getContact_info(), person.getAddress(), person.getId());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM Person WHERE person_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}