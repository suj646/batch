package io.spring.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import io.spring.batch.domain.Person;
import io.spring.batch.domain.Person2;
import io.spring.batch.domain.PersonFieldSetMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class JobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	

	@Autowired
	public DataSource dataSource;
	
	public JdbcTemplate jdbcTemplate;
	
	private final List<Person> processedData = new ArrayList<>();
	
	@Bean
	
    public List<Person> processedData() {
        return new ArrayList<>();
    }
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public ItemReader<? extends Person> person2ItemReader(DataSource dataSource) {
	    JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
	    reader.setDataSource(dataSource);
	    reader.setSql("SELECT id, firstName FROM Person");
	    reader.setRowMapper(new PersonRowMapper());
	    return reader;
	}
	
	
	@Bean
	public JdbcBatchItemWriter<Person> person2ItemWriter(DataSource dataSource) {
	    JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
	    itemWriter.setDataSource(dataSource);
	    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
	    itemWriter.setSql("UPDATE Person2 SET firstName = :firstName WHERE id = :id");
	    return itemWriter;
	}

	@Bean
	public ItemProcessor<Person, Person> person2ItemProcessor() {
	    return item -> {
	        // Compare the firstName value from Person and update Person2 if it matches
	        // You can add your logic here to decide how to update
	        if (item != null) {
	            String firstName = item.getFirstName();
	            if (firstName != null) {
	                // Check if there is a matching record in Person2
	                // You can modify this query as needed
	                String selectQuery = "SELECT id FROM Person2 WHERE firstName = GAURAV";
	                Integer id = jdbcTemplate.query(selectQuery, new Object[]{firstName}, (rs, rowNum) -> rs.getInt("id")).stream().findFirst().orElse(null);
	                if (id != null) {
	                    // Update the Person2 record
	                    return new Person(2, "Ajay"); // Modify the new value as needed
	                }
	            }
	        }
	        return null; // Return null for records that don't need an update
	    };
	}
	
	@Bean
	public FlatFileItemReader<Person> personItemReader() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource("/data/person.csv"));

		DefaultLineMapper<Person> customerLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"id", "firstName", "lastName", "birthdate"});

		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new PersonFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}
	
	
	@Bean
	public ItemProcessor<Person, Person> personItemProcessor() {
	    return item -> {
	        // Example: Convert the 'firstName' to uppercase
	        item.setFirstName(item.getFirstName().toUpperCase());

	        // Log the processed data to the console
	        Logger logger = LoggerFactory.getLogger(this.getClass());
	        
//	        logger.info("Processed Person: {}", item);

	        return item;
	    };
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<Person> personItemWriter() {
		JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();

		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO PERSON VALUES (:id, :firstName, :lastName, :birthdate)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		itemWriter.afterPropertiesSet();
		return itemWriter;
	}

////	@Bean
////	public Step step1() {
////		return stepBuilderFactory.get("step1")
////				.<Person, Person>chunk(10)
////				.reader(personItemReader())
////				.processor(personItemProcessor())
////				.writer(personItemWriter())
////				.build();
////	}
//	
//	
////	@Bean
////    public Step step1() {
////        return stepBuilderFactory.get("step1")
////                .<Person, Person>chunk(10)
////                .reader(personItemReader())
////                .processor(personItemProcessor())
////                .writer(list -> {
////                    // Save the processed data to the processedData list
////                    List<Person> processedData = processedData();
////                    processedData.addAll(list);
////                })
////                .build();
////    }
//	
//	
////	@Bean
////	public Step step1() {
////	    return stepBuilderFactory.get("step1")
////	            .<Person, Person>chunk(10)
////	            .reader(personItemReader())
////	            .processor(personItemProcessor())
////	            .writer(list -> {
////                  // Save the processed data to the processedData list
////                  List<Person> processedData = processedData();
////                  processedData.addAll(list);
////              })
////	            .listener(new StepExecutionListener() {
////	                @Override
////	                public void beforeStep(StepExecution stepExecution) {
////	                    System.out.println("Step 1 started");
////	                }
////
////	                @Override
////	                public ExitStatus afterStep(StepExecution stepExecution) {
////	                    System.out.println("Step 1 ended");
////						return null;
////	                }
////	            })
////	            .build();
////	}
//	
//	
	@Bean
	public Step step1() {
	    return stepBuilderFactory.get("step1")
	        .<Person, Person>chunk(10)
	        .reader(personItemReader())
	        .processor(personItemProcessor())
	        .writer(list -> {
	            // Save the processed data to the processedData list
	            List<Person> processedData = processedData();
	            processedData.addAll(list);

	            // Implement your database insert logic here
	            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	            for (Person person : list) {
	                // Check if a record with the same ID exists in the database
	                int count = jdbcTemplate.queryForObject(
	                    "SELECT COUNT(*) FROM PERSON WHERE id = ?",
	                    Integer.class, person.getId());

	                if (count == 0) {
	                    // Insert the record if it doesn't exist
	                    int updatedRows = jdbcTemplate.update(
	                        "INSERT INTO PERSON (id, firstName, lastName, birthdate) VALUES (?, ?, ?, ?)",
	                        person.getId(), person.getFirstName(), person.getLastName(), person.getBirthdate());

	                    System.out.println("Inserted " + updatedRows + " rows in the PERSON table for ID: " + person.getId());
	                }
	            }
	        })
	        .build();
	}
	
////	@Bean
////	public Step step2() {
////	    return stepBuilderFactory.get("step2")
////	            .tasklet((contribution, chunkContext) -> {
////	                // This is a Tasklet step for updating records in the PERSON table.
////	                // You can perform your database updates here.
////
////	                // Sample update using JdbcTemplate:
////	                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
////	                int updatedRows = jdbcTemplate.update(
////	                		 "UPDATE PERSON SET firstName = ? WHERE id = ?",
////	                         "NewFirstName",  // New firstName
////	                         1);            // Replace with the specific ID of the row you want to update
////
////	                System.out.println("Updated " + updatedRows + " rows in the PERSON table.");
////	                return RepeatStatus.FINISHED;
////	            })
////	            .build();
////	}
//
//	
//	
////	@Bean
////	public Step step2() {
////	    return stepBuilderFactory.get("step2")
////	            .tasklet((contribution, chunkContext) -> {
////	                // This is a Tasklet step for comparing data in the processedData list with data in the database.
////
////	                // Retrieve the processed data from step1
////	                List<Person> processedData = processedData();
////
////	                // Sample query using JdbcTemplate to retrieve data from the database
////	                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
////	                List<Person> databaseData = jdbcTemplate.query(
////	                        "SELECT * FROM PERSON",
////	                        new BeanPropertyRowMapper<>(Person.class)
////	                );
////
////	                // Log the size of processedData and databaseData
////	                System.out.println("Processed data size: " + processedData.size());
////	                System.out.println("Database data size: " + databaseData.size());
////
////	                // Compare the data from Step 1 with the data in the database
////	                for (Person dbPerson : databaseData) {
////	                    for (Person csvPerson : processedData) {
////	                        if (dbPerson.getId() == csvPerson.getId()) {
////	                            if (!dbPerson.equals(csvPerson)) {
////	                                // Data doesn't match
////	                                System.out.println("Data mismatch for ID: " + dbPerson.getId());
////	                            } else {
////	                                // Data matches
////	                                System.out.println("Data matches for ID: " + dbPerson.getId());
////	                            }
////	                        }
////	                    }
////	                }
////
////	                return RepeatStatus.FINISHED;
////	            })
////	            .build(); 
////	}
	
	@Bean
	public Step step2() {
	    return stepBuilderFactory.get("step2")
	        .tasklet((contribution, chunkContext) -> {
	            // Sample query using JdbcTemplate to retrieve data from the database
	            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	            List<Person> databaseData = jdbcTemplate.query(
	                "SELECT * FROM PERSON",
	                new BeanPropertyRowMapper<>(Person.class)
	            );

	            // Retrieve the processed data from step1
	            List<Person> processedData = processedData();

	            // Log the size of processedData and databaseData
	            System.out.println("Processed data size: " + processedData.size());
	            System.out.println("Database data size: " + databaseData.size());

	            // Update records in the database
	            for (Person dbPerson : databaseData) {
	                for (Person csvPerson : processedData) {
	                    if (dbPerson.getId() == csvPerson.getId()) {
	                        if (!dbPerson.equals(csvPerson)) {
	                            // Data doesn't match
	                            System.out.println("Data mismatch for ID: " + dbPerson.getId());

	                            // Implement your update logic here
	                            // Sample update using JdbcTemplate:
	                            int updatedRows = jdbcTemplate.update(
	                                "UPDATE PERSON SET firstName = ? WHERE id = ?",
	                                csvPerson.getFirstName(), dbPerson.getId());
	                            System.out.println("Updated " + updatedRows + " rows in the PERSON table.");
	                        } else {
	                            // Data matches
	                            System.out.println("Data matches for ID: " + dbPerson.getId());
	                        }
	                    }
	                }
	            }

	            // Delete a specific record with ID 2
	            for (Person dbPerson : databaseData) {
	                if (dbPerson.getId() == 3) {
	                    System.out.println("Data to be deleted for ID: " + dbPerson.getId());

	                    // Implement your delete logic here
	                    // Sample delete using JdbcTemplate:
	                    int deletedRows = jdbcTemplate.update(
	                        "DELETE FROM PERSON WHERE id = ?",
	                        dbPerson.getId());
	                    System.out.println("Deleted " + deletedRows + " rows in the PERSON table.");
	                }
	            }

	            return RepeatStatus.FINISHED;
	        })
	        .build();
	}
	
	
	@Bean
	public Step step3() {
	    return stepBuilderFactory.get("step3")
	        .<Person, Person>chunk(10)
	        .reader(person2ItemReader(dataSource))
	        .processor(person2ItemProcessor())
	        .writer(person2ItemWriter(dataSource))
	        .build();
	}
	

	
	@Bean
	public Job job() {
	    return jobBuilderFactory.get("job")
	            .start(step1())
	            .next(step2())
	            .next(step3())// Add the new step here
	            .build();
	}
}


