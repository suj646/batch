package io.spring.batch;



import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import io.spring.batch.domain.Person;

import java.util.List;

public class CustomItemWriter implements ItemWriter<Object> {
    private JdbcTemplate jdbcTemplate;

    public CustomItemWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    

    @Override
    public void write(List<? extends Object> items) throws Exception {
        for (Object item : items) {
            if (item instanceof Person) {
                Person person = (Person) item;
                // Check if a record with the same ID exists in the PERSON table
                int countPerson = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM PERSON WHERE id = ?",
                    Integer.class, person.getId1());

                if (countPerson > 0) {
                    // Update the record in the PERSON table if it exists
                    jdbcTemplate.update(
                        "UPDATE PERSON SET first_Name = ?, last_Name = ?, birthdate = ? WHERE id = ?",
                        person.getFirstName(), person.getLastName(), person.getBirthdate(), person.getId1());
                } else {
                    // Insert the record into the PERSON table if it doesn't exist
                    jdbcTemplate.update(
                        "INSERT INTO PERSON (id, first_Name, last_Name, birthdate) VALUES (?, ?, ?, ?)",
                        person.getId1(), person.getFirstName(), person.getLastName(), person.getBirthdate());
                }
            } else if (item instanceof Student) {
                Student student = (Student) item;
                // Check if a record with the same ID exists in the STUDENT table
                int countStudent = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM STUDENT WHERE id = ?",
                    Integer.class, student.getId1());

                if (countStudent > 0) {
                    // Update the record in the STUDENT table if it exists
                    jdbcTemplate.update(
                        "UPDATE STUDENT SET first_Name = ?, last_Name = ?, birthdate = ? WHERE id = ?",
                        student.getFirstName(), student.getLastName(), student.getBirthdate(), student.getId1());
                } else {
                    // Insert the record into the STUDENT table if it doesn't exist
                    jdbcTemplate.update(
                        "INSERT INTO STUDENT (id, first_Name, last_Name, birthdate) VALUES (?, ?, ?, ?)",
                        student.getId1(), student.getFirstName(), student.getLastName(), student.getBirthdate());
                }
            }

            // Delete records from STUDENT that are not in PERSON
//            deleteRecordsNotInPersonTable();

            // Delete records from PERSON that are not in STUDENT
//            deleteRecordsNotInStudentTable();
        }
    }

    private void deleteRecordsNotInPersonTable() {
        String deleteSql = "DELETE FROM PERSON WHERE id NOT IN (SELECT id FROM STUDENT)";
        jdbcTemplate.update(deleteSql);
    }

    private void deleteRecordsNotInStudentTable() {
        String deleteSql = "DELETE FROM STUDENT WHERE id NOT IN (SELECT id FROM PERSON)";
        jdbcTemplate.update(deleteSql);
    }
}


