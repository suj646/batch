package io.spring.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

import io.spring.batch.domain.Person;

public class CustomItemProcessor implements ItemProcessor<Person, Student> {
    private JdbcTemplate jdbcTemplate;

    public CustomItemProcessor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Student process(Person person) throws Exception {
        // Check if a record with the same ID exists in the PERSON table
        int countPerson = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM PERSON WHERE id = ?",
            Integer.class, person.getId());

        if (countPerson > 0) {
            // Update the record in the PERSON table if it exists
            jdbcTemplate.update(
                "UPDATE PERSON SET firstName = ?, lastName = ?, birthdate = ? WHERE id = ?",
                person.getFirstName(), person.getLastName(), person.getBirthdate(), person.getId());
        } else {
            // Insert the record into the PERSON table if it doesn't exist
            jdbcTemplate.update(
                "INSERT INTO PERSON (id, firstName, lastName, birthdate) VALUES (?, ?, ?, ?)",
                person.getId(), person.getFirstName(), person.getLastName(), person.getBirthdate());
        }

        // Check if a record with the same ID exists in the STUDENT table
        int countStudent = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM STUDENT WHERE id = ?",
            Integer.class, person.getId());

        Student student = new Student();
        student.setId(person.getId());
        student.setFirstName(person.getFirstName());
        student.setLastName(person.getLastName());
        student.setBirthdate(person.getBirthdate());

        if (countStudent > 0) {
            // Update the record in the STUDENT table if it exists
            jdbcTemplate.update(
                "UPDATE STUDENT SET firstName = ?, lastName = ?, birthdate = ? WHERE id = ?",
                student.getFirstName(), student.getLastName(), student.getBirthdate(), student.getId());
        } else {
            // Insert the record into the STUDENT table if it doesn't exist
            jdbcTemplate.update(
                "INSERT INTO STUDENT (id, firstName, lastName, birthdate) VALUES (?, ?, ?, ?)",
                student.getId(), student.getFirstName(), student.getLastName(), student.getBirthdate());
        }

        // Delete records from STUDENT that are not in PERSON
//        deleteRecordsNotInPersonTable();

        // Delete records from PERSON that are not in STUDENT
        deleteRecordsNotInStudentTable();

        // Always return the Student record for writing
        return student;
    }

//    private void deleteRecordsNotInPersonTable() {
//        String deleteSql = "DELETE FROM PERSON WHERE id NOT IN (SELECT id FROM STUDENT)";
//        jdbcTemplate.update(deleteSql);
//    }

    private void deleteRecordsNotInStudentTable() {
        String deleteSql = "DELETE FROM STUDENT WHERE id NOT IN (SELECT id FROM PERSON)";
        jdbcTemplate.update(deleteSql);
    }
}
