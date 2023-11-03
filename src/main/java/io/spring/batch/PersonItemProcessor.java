package io.spring.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonItemProcessor implements ItemProcessor<Person, Person> {
	 private static final Logger logger = LoggerFactory.getLogger(PersonItemProcessor.class);
	 
	    private DataSource dataSource;
//	    private Set<Long> flatFileIds;
	    
	    public PersonItemProcessor(DataSource dataSource) {
	        this.dataSource = dataSource;
//	        this.flatFileIds = flatFileIds;
	    }

    @Override
    public Person process(Person item) throws Exception {
//    	logger.info("Processing item with ID: {}", item.getId1());

//        String upperCaseFirstName = item.getFirstName().toUpperCase();
//        item.setFirstName(upperCaseFirstName);
//        return item;
    	
    	boolean personExistsInPerson = checkIfPersonExistsInPersonTable(item);
//    	logger.info("personExistsInPerson for ID {}: {}", item.getId1(), personExistsInPerson);
    	
        if (personExistsInPerson) {
            boolean personExistsInStudent = checkIfPersonExistsInStudentTable(item);


            if (personExistsInStudent) {
//            	logger.info("checkIfPersonExistsInStudentTable for ID {}: {}", item.getId1(), personExistsInStudent);
                return item;
            } else {
            	
                return null;
            }
        } else {
            logger.info("Deleting record with ID: " , item.getId1());
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
            logger.info("Error while checking STUDENT table: " , e.getMessage());
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

        	logger.info("Error while checking PERSON table: " , e.getMessage());
        }

        return false;
    }
    }

