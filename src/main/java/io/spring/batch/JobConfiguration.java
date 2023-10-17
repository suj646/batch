package io.spring.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import io.spring.batch.domain.Person;
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


@Configuration
public class JobConfiguration {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	@Autowired
	public DataSource dataSource;
	
	private final List<Person> processedData = new ArrayList<>();
	
	@Bean
	
    public List<Person> processedData() {
        return new ArrayList<>();
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

//	@Bean
//	public Step step1() {
//		return stepBuilderFactory.get("step1")
//				.<Person, Person>chunk(10)
//				.reader(personItemReader())
//				.processor(personItemProcessor())
//				.writer(personItemWriter())
//				.build();
//	}
	
	
//	@Bean
//    public Step step1() {
//        return stepBuilderFactory.get("step1")
//                .<Person, Person>chunk(10)
//                .reader(personItemReader())
//                .processor(personItemProcessor())
//                .writer(list -> {
//                    // Save the processed data to the processedData list
//                    List<Person> processedData = processedData();
//                    processedData.addAll(list);
//                })
//                .build();
//    }
	
	
//	@Bean
//	public Step step1() {
//	    return stepBuilderFactory.get("step1")
//	            .<Person, Person>chunk(10)
//	            .reader(personItemReader())
//	            .processor(personItemProcessor())
//	            .writer(list -> {
//                  // Save the processed data to the processedData list
//                  List<Person> processedData = processedData();
//                  processedData.addAll(list);
//              })
//	            .listener(new StepExecutionListener() {
//	                @Override
//	                public void beforeStep(StepExecution stepExecution) {
//	                    System.out.println("Step 1 started");
//	                }
//
//	                @Override
//	                public ExitStatus afterStep(StepExecution stepExecution) {
//	                    System.out.println("Step 1 ended");
//						return null;
//	                }
//	            })
//	            .build();
//	}
	
	
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
	            JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
	            itemWriter.setDataSource(dataSource);
	            itemWriter.setSql("INSERT INTO PERSON VALUES (:id, :firstName, :lastName, :birthdate)");
	            itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
	            itemWriter.afterPropertiesSet();
	            itemWriter.write(list);
	        })
	        .listener(new StepExecutionListener() {
	            @Override
	            public void beforeStep(StepExecution stepExecution) {
	                System.out.println("Step 1 started");
	            }

	            @Override
	            public ExitStatus afterStep(StepExecution stepExecution) {
	                System.out.println("Step 1 ended");
	                return null;
	            }
	        })
	        .build();
	}
	
//	@Bean
//	public Step step2() {
//	    return stepBuilderFactory.get("step2")
//	            .tasklet((contribution, chunkContext) -> {
//	                // This is a Tasklet step for updating records in the PERSON table.
//	                // You can perform your database updates here.
//
//	                // Sample update using JdbcTemplate:
//	                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//	                int updatedRows = jdbcTemplate.update(
//	                		 "UPDATE PERSON SET firstName = ? WHERE id = ?",
//	                         "NewFirstName",  // New firstName
//	                         1);            // Replace with the specific ID of the row you want to update
//
//	                System.out.println("Updated " + updatedRows + " rows in the PERSON table.");
//	                return RepeatStatus.FINISHED;
//	            })
//	            .build();
//	}

	
	
//	@Bean
//	public Step step2() {
//	    return stepBuilderFactory.get("step2")
//	            .tasklet((contribution, chunkContext) -> {
//	                // This is a Tasklet step for comparing data in the processedData list with data in the database.
//
//	                // Retrieve the processed data from step1
//	                List<Person> processedData = processedData();
//
//	                // Sample query using JdbcTemplate to retrieve data from the database
//	                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//	                List<Person> databaseData = jdbcTemplate.query(
//	                        "SELECT * FROM PERSON",
//	                        new BeanPropertyRowMapper<>(Person.class)
//	                );
//
//	                // Log the size of processedData and databaseData
//	                System.out.println("Processed data size: " + processedData.size());
//	                System.out.println("Database data size: " + databaseData.size());
//
//	                // Compare the data from Step 1 with the data in the database
//	                for (Person dbPerson : databaseData) {
//	                    for (Person csvPerson : processedData) {
//	                        if (dbPerson.getId() == csvPerson.getId()) {
//	                            if (!dbPerson.equals(csvPerson)) {
//	                                // Data doesn't match
//	                                System.out.println("Data mismatch for ID: " + dbPerson.getId());
//	                            } else {
//	                                // Data matches
//	                                System.out.println("Data matches for ID: " + dbPerson.getId());
//	                            }
//	                        }
//	                    }
//	                }
//
//	                return RepeatStatus.FINISHED;
//	            })
//	            .build(); 
//	}
	
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

	            // Compare the data from Step 1 with the data in the database
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
	                                csvPerson.getFirstName(),  // New firstName
	                                dbPerson.getId()); // ID of the row you want to update
	                            System.out.println("Updated " + updatedRows + " rows in the PERSON table.");
	                        } else {
	                            // Data matches
	                            System.out.println("Data matches for ID: " + dbPerson.getId());
	                        }
	                    }
	                }
	            }

	            // Identify records to be deleted (present in database but not in CSV data)
//	            List<Integer> idsToDelete = new ArrayList<>();
	            List<Long> idsToDelete = new ArrayList<>();
	            for (Person dbPerson : databaseData) {
	                boolean found = false;
	                for (Person csvPerson : processedData) {
	                    if (dbPerson.getId() == csvPerson.getId()) {
	                        found = true;
	                        break;
	                    }
	                }
	                if (!found) {
	                    // Record is present in the database but not in CSV data
	                    idsToDelete.add(dbPerson.getId());
	                }
	            }

	            // Delete records from the database
	            for (Long id : idsToDelete) {
	                int deletedRows = jdbcTemplate.update(
	                    "DELETE FROM PERSON WHERE id = ?",
	                    id
	                );
	                System.out.println("Deleted " + deletedRows + " rows in the PERSON table.");
	            }

	            return RepeatStatus.FINISHED;
	        })
	        .build();
	}
	

	
	@Bean
	public Job job() {
	    return jobBuilderFactory.get("job")
	            .start(step1())
	            .next(step2()) // Add the new step here
	            .build();
	}
}