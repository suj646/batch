package io.spring.batch;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.List;

@Component
public class PersonItemWriter implements ItemWriter<Person> {

    private final JdbcTemplate jdbcTemplate;

    public PersonItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(List<? extends Person> persons) throws Exception {
        for (Person person : persons) {
            if (personExists(person.getId1())) {
                updatePerson(person);
            } else {
                insertPerson(person);
            }
        }
        
//        List<Long> deletedIds = deleteRecordsNotInPersonTable();
//        if (!deletedIds.isEmpty()) {
//            System.out.println("Deleted IDs: " + deletedIds);
//        }
        
        
    }

    private void insertPerson(Person person) {
        String sql = "INSERT INTO person (id, first_Name, last_Name, birthdate) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, person.getId1(), person.getFirstName(), person.getLastName(), person.getBirthdate());
    }

    private void updatePerson(Person person) {
        String sql = "UPDATE person SET first_Name = ?, last_Name = ?, birthdate = ? WHERE id = ?";
        jdbcTemplate.update(sql, person.getFirstName(), person.getLastName(), person.getBirthdate(), person.getId1());
    }

    private boolean personExists(long id) {
        String sql = "SELECT COUNT(*) FROM person WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }
    
    private List<Long> deleteRecordsNotInPersonTable() {
        String selectDeletedIdsSql = "SELECT id FROM PERSON WHERE id NOT IN (SELECT id FROM STUDENT)";
        List<Long> deletedIds = jdbcTemplate.queryForList(selectDeletedIdsSql, Long.class);

        String deleteSql = "DELETE FROM PERSON WHERE id NOT IN (SELECT id FROM STUDENT)";
        jdbcTemplate.update(deleteSql);

        return deletedIds;
    }
}



