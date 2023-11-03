package io.spring.batch;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;

@Configuration
public class PersonItemWriter {
	private static final Logger logger = LoggerFactory.getLogger(PersonItemProcessor.class);
	private final DataSource dataSource;

    public PersonItemWriter(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public JdbcBatchItemWriter<Person> personItemWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();

        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("INSERT INTO person (id, first_Name, last_Name, birthdate) VALUES (:id1, :firstName, :lastName, :birthdate) " +
                "ON DUPLICATE KEY UPDATE " +
                "first_Name=VALUES(first_Name), last_Name=VALUES(last_Name), birthdate=VALUES(birthdate)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.setAssertUpdates(false);

        // Log the data before writing
        itemWriter.setItemSqlParameterSourceProvider(item -> {
            logger.info("Writing data: id={}, firstName={}, lastName={}, birthdate={}",
                    item.getId1(), item.getFirstName(), item.getLastName(), item.getBirthdate());
            return new BeanPropertySqlParameterSource(item);
        });

        try {
            itemWriter.afterPropertiesSet();
        } catch (DataIntegrityViolationException ex) {
            logger.error("Error while writing person data: {}", ex.getMessage());
        }

        return itemWriter;
    }
}

