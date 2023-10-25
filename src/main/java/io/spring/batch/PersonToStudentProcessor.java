//package io.spring.batch;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.item.ItemProcessor;
//
//import io.spring.batch.domain.Person;
//
//
//public class PersonToStudentProcessor implements ItemProcessor<Person, Student> {
//    private DataSource dataSource;
//
//    public PersonToStudentProcessor(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }
//
//    @Override
//    public Student process(Person person) throws Exception {
//        // Check if the ID exists in the "PERSON" table
//        boolean idExistsInPerson = checkIfIdExistsInPersonTable(person.getId1());
//
//        if (idExistsInPerson) {
//            // Check if the ID exists in the "STUDENT" table
//            boolean idExistsInStudent = checkIfIdExistsInStudentTable(person.getId1());
//
//            if (idExistsInStudent) {
//                // If the ID exists in "STUDENT," update the existing record
//                Student student = new Student();
//                student.setId1(person.getId1());
//                student.setFirstName(person.getFirstName());
//                student.setLastName(person.getLastName());
//                student.setBirthdate(person.getBirthdate());
//
//                // You should implement an update method for updating records in the "STUDENT" table
//                // For example: updateStudentRecord(student);
//
//                return student;
//            } else {
//                // If the ID doesn't exist in "STUDENT," create a new student
//                Student student = new Student();
//                student.setId1(person.getId1());
//                student.setFirstName(person.getFirstName());
//                student.setLastName(person.getLastName());
//                student.setBirthdate(person.getBirthdate());
//
//                // You should implement an insert method for inserting records into the "STUDENT" table
//                // For example: insertStudentRecord(student);
//
//                return student;
//            }
//        } else {
//            // If the ID doesn't exist in "PERSON," return null to skip adding it to "STUDENT"
//            return null;
//        }
//    }
//
//    private boolean checkIfIdExistsInStudentTable(Long id) {
//	    try (Connection connection = dataSource.getConnection()) {
//	        String sql = "SELECT COUNT(*) FROM STUDENT WHERE id = ?";
//	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//	            preparedStatement.setLong(1, id);
//
//	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//	                if (resultSet.next()) {
//	                    int count = resultSet.getInt(1);
//	                    return count > 0; // If count > 0, the ID exists in the "STUDENT" table
//	                }
//	            }
//	        }
//	    } catch (SQLException e) {
//	        // Handle exceptions, e.g., log or throw an exception
//	        e.printStackTrace();
//	    }
//
//	    // If there was an error or the ID was not found, return false
//	    return false;
//	}
//
//    boolean checkIfIdExistsInPersonTable(Long id) {
//	    try (Connection connection = dataSource.getConnection()) {
//	        String sql = "SELECT COUNT(*) FROM PERSON WHERE id = ?";
//	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//	            preparedStatement.setLong(1, id);
//
//	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//	                if (resultSet.next()) {
//	                    int count = resultSet.getInt(1);
//	                    return count > 0; // If count > 0, the ID exists in the "PERSON" table
//	                }
//	            }
//	        }
//	    } catch (SQLException e) {
//	        // Handle exceptions, e.g., log or throw an exception
//	        e.printStackTrace();
//	    }
//
//	    // If there was an error or the ID was not found, return false
//	    return false;
//	}
//}
//
