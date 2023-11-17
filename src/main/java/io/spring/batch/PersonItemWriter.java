package io.spring.batch;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class PersonItemWriter implements ItemWriter<Person> {
	private static final Logger logger = Logger.getLogger(PersonItemWriter.class.getName());

    private final JdbcTemplate jdbcTemplate;

    public PersonItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(List<? extends Person> persons) throws Exception {
    	Set<Long> processedIds = new HashSet<>();
        for (Person person : persons) {
//        	logger.info("Processing person: " + person);
            if (personExists(person.getId1())) {
                updatePerson(person);
            } else {
                insertPerson(person);
            }
            processedIds.add(person.getId1());
        }
        logger.info("Processed IDs: " + processedIds);
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
}



