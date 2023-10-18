package io.spring.batch;

import org.springframework.jdbc.core.RowMapper;

import io.spring.batch.domain.Person;
import io.spring.batch.domain.Person2;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person>{
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setId(rs.getInt("id"));
        person.setFirstName(rs.getString("firstName"));
        // Set other properties as needed
        return person;
    }
}

