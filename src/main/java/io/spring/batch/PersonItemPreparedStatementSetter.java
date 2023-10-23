package io.spring.batch;
//
//import org.springframework.batch.item.database.ItemPreparedStatementSetter;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import io.spring.batch.domain.Person;
//
//public class PersonItemPreparedStatementSetter implements ItemPreparedStatementSetter<Person> {
//    @Override
//    public void setValues(Person person, PreparedStatement ps) throws SQLException {
//        ps.setInt(1, (int) person.getId1());
//        ps.setString(2, person.getFirstName());
//        ps.setString(3, person.getLastName());
//        ps.setDate(4, (Date) person.getBirthdate());
//    }
//}

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import io.spring.batch.domain.Person;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class PersonItemPreparedStatementSetter implements ItemPreparedStatementSetter<Person> {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PersonItemPreparedStatementSetter(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void setValues(Person person, PreparedStatement preparedStatement) throws SQLException {
        // Set the values directly in the PreparedStatement
        preparedStatement.setLong(1, person.getId1());
        preparedStatement.setString(2, person.getFirstName());
        preparedStatement.setString(3, person.getLastName());
        preparedStatement.setDate(4, new java.sql.Date(person.getBirthdate().getTime()));
    }
}





