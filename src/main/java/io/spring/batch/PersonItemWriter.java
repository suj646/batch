package io.spring.batch;

import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.dao.DataIntegrityViolationException;
import io.spring.batch.domain.Person;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersonItemWriter extends JdbcBatchItemWriter<Person> {
    private static final Logger logger = LogManager.getLogger(PersonItemWriter.class);

    public PersonItemWriter(DataSource dataSource) {
        this.setDataSource(dataSource);
        this.setSql("INSERT INTO person (id, firstName, lastName, birthdate) " +
                "VALUES (:id1, :firstName, :lastName, :birthdate) " +
                "ON DUPLICATE KEY UPDATE " +
                "firstName=VALUES(firstName), lastName=VALUES(lastName), birthdate=VALUES(birthdate)");
        this.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        this.setAssertUpdates(false); // Do not assert that updates were performed

        try {
            this.afterPropertiesSet();
        } catch (DataIntegrityViolationException ex) {
            // Handle the exception here
            logger.error("Error while writing person data: " + ex.getMessage());
            // You can log the exception or perform custom error handling
        }
    }
}
