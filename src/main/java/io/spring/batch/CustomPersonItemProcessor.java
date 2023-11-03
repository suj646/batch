package io.spring.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

@Component
public class CustomPersonItemProcessor implements ItemProcessor<Person, Person> {
    private static final Logger logger = Logger.getLogger(CustomPersonItemProcessor.class.getName());
    private DataSource dataSource;
    private Set<Long> flatFileIds; 

    public CustomPersonItemProcessor(DataSource dataSource, Set<Long> flatFileIds) {
        this.dataSource = dataSource;
        this.flatFileIds = flatFileIds;
    }

    @Override
    public Person process(Person item) throws Exception {
        boolean personExistsInPerson = checkIfPersonExistsInPersonTable(item);

        if (personExistsInPerson) {
            boolean personExistsInStudent = checkIfPersonExistsInStudentTable(item);

            if (personExistsInStudent) {
                return item;
            } else {
            	
                return null;
            }
        } else {   
           
            logger.log(Level.INFO, "Deleting record with ID: " + item.getId1());
            return null;
        }
    }

    private boolean checkIfPersonExistsInStudentTable(Person person) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) FROM STUDENT WHERE id = ? AND first_name = ? AND last_name = ? AND birthdate = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, person.getId1());
                preparedStatement.setString(2, person.getFirstName());
                preparedStatement.setString(3, person.getLastName());
                preparedStatement.setDate(4, new java.sql.Date(person.getBirthdate().getTime()));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while checking STUDENT table: " + e.getMessage());
        }

        return false;
    }

    private boolean checkIfPersonExistsInPersonTable(Person person) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) FROM PERSON WHERE id = ? AND first_name = ? AND last_name = ? AND birthdate = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setLong(1, person.getId1());
                preparedStatement.setString(2, person.getFirstName());
                preparedStatement.setString(3, person.getLastName());
                preparedStatement.setDate(4, new java.sql.Date(person.getBirthdate().getTime()));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error while checking PERSON table: " + e.getMessage());
        }

        return false;
    }
}
