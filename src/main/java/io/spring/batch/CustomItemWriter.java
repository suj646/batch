//package io.spring.batch;
//
//
//
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import io.spring.batch.domain.Person;
//
//import java.util.List;
//
//public class CustomItemWriter implements ItemWriter<Object> {
//    private JdbcTemplate jdbcTemplate;
//
//    public CustomItemWriter(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public void write(List<? extends Object> items) throws Exception {
//        for (Object item : items) {
//            if (item instanceof Person) {
//                Person person = (Person) item;
//                // Check if a record with the same ID exists in the PERSON table
//                int countPerson = jdbcTemplate.queryForObject(
//                    "SELECT COUNT(*) FROM PERSON WHERE id = ?",
//                    Integer.class, person.getId());
//
//                if (countPerson > 0) {
//                    // Update the record in the PERSON table if it exists
//                    jdbcTemplate.update(
//                        "UPDATE PERSON SET firstName = ?, lastName = ?, birthdate = ? WHERE id = ?",
//                        person.getFirstName(), person.getLastName(), person.getBirthdate(), person.getId());
//                } else {
//                    // Insert the record into the PERSON table if it doesn't exist
//                    jdbcTemplate.update(
//                        "INSERT INTO PERSON (id, firstName, lastName, birthdate) VALUES (?, ?, ?, ?)",
//                        person.getId(), person.getFirstName(), person.getLastName(), person.getBirthdate());
//                }
//            } else if (item instanceof Student) {
//                Student student = (Student) item;
//                // Check if a record with the same ID exists in the STUDENT table
//                int countStudent = jdbcTemplate.queryForObject(
//                    "SELECT COUNT(*) FROM STUDENT WHERE id = ?",
//                    Integer.class, student.getId());
//
//                if (countStudent > 0) {
//                    // Update the record in the STUDENT table if it exists
//                    jdbcTemplate.update(
//                        "UPDATE STUDENT SET firstName = ?, lastName = ?, birthdate = ? WHERE id = ?",
//                        student.getFirstName(), student.getLastName(), student.getBirthdate(), student.getId());
//                } else {
//                    // Insert the record into the STUDENT table if it doesn't exist
//                    jdbcTemplate.update(
//                        "INSERT INTO STUDENT (id, firstName, lastName, birthdate) VALUES (?, ?, ?, ?)",
//                        student.getId(), student.getFirstName(), student.getLastName(), student.getBirthdate());
//                }
//            }
//
//            // Delete records from STUDENT that are not in PERSON
//            deleteRecordsNotInPersonTable();
//
//            // Delete records from PERSON that are not in STUDENT
//            deleteRecordsNotInStudentTable();
//        }
//    }
//
//    private void deleteRecordsNotInPersonTable() {
//        String deleteSql = "DELETE FROM PERSON WHERE id NOT IN (SELECT id FROM STUDENT)";
//        jdbcTemplate.update(deleteSql);
//    }
//
//    private void deleteRecordsNotInStudentTable() {
//        String deleteSql = "DELETE FROM STUDENT WHERE id NOT IN (SELECT id FROM PERSON)";
//        jdbcTemplate.update(deleteSql);
//    }
//}
//
//
