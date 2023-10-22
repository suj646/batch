package io.spring.batch;
import javax.batch.runtime.StepExecution;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.spring.batch.domain.Person;
import io.spring.batch.domain.PersonFieldSetMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.batch.core.StepListener;

import com.mysql.cj.log.Log;


@Configuration
@Slf4j
public class JobConfiguration {
	
	private static final Logger logger = Logger.getLogger(JobConfiguration.class.getName());
	
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
//	private  StudentWriteListener studentWriteListener;
	
	private final JdbcTemplate jdbcTemplate; // Inject this bean or create it in your configuration

    public JobConfiguration(DataSource dataSource) {
        // Initialize the JdbcTemplate with the DataSource
    	this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	
    private void deleteRecordsNotInStudentTable() {
        String deleteSql = "DELETE FROM STUDENT WHERE id NOT IN (SELECT id FROM PERSON)";
        jdbcTemplate.update(deleteSql);
    }
	
	@Autowired
	public DataSource dataSource;
	
	
	
	@Bean
	public FlatFileItemReader<Person> personItemReader() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
		reader.setLinesToSkip(1);
		reader.setResource(new ClassPathResource("/data/person.csv"));

		DefaultLineMapper<Person> customerLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"id1", "firstName", "lastName", "birthdate"});

		customerLineMapper.setLineTokenizer(tokenizer);
		customerLineMapper.setFieldSetMapper(new PersonFieldSetMapper());
		customerLineMapper.afterPropertiesSet();
		reader.setLineMapper(customerLineMapper);
		return reader;
	}

	
	@Bean
	public ItemProcessor<Person, Student> personToStudentProcessor() {
	    return new ItemProcessor<Person, Student>() {
	        @Override
	        public Student process(Person person) throws Exception {
	            // Check if the ID exists in the "PERSON" table
	            boolean idExistsInPerson = checkIfIdExistsInPersonTable(person.getId1());

	            if (idExistsInPerson) {
	                // Check if the ID exists in the "STUDENT" table
	                boolean idExistsInStudent = checkIfIdExistsInStudentTable(person.getId1());
	                
	                if (idExistsInStudent) {
	                    // If the ID exists in "STUDENT," update the existing record
	                    Student student = new Student();
	                    student.setId1(person.getId1());  
	                    student.setFirstName(person.getFirstName());
	                    student.setLastName(person.getLastName());
	                    student.setBirthdate(person.getBirthdate());
	                    
	                    // You should implement an update method for updating records in the "STUDENT" table
	                    // For example: updateStudentRecord(student);
	                    
	                    return student;
	                } else {
	                    // If the ID doesn't exist in "STUDENT," create a new student
	                    Student student = new Student();
	                    student.setId1(person.getId1());
	                    student.setFirstName(person.getFirstName());
	                    student.setLastName(person.getLastName());
	                    student.setBirthdate(person.getBirthdate());
	                    
	                    // You should implement an insert method for inserting records into the "STUDENT" table
	                    // For example: insertStudentRecord(student);
	                    
	                    return student;
	                }
	            } else {
	                // If the ID doesn't exist in "PERSON," return null to skip adding it to "STUDENT"
	                return null;
	            }
	            
	        }
	    };
	}
	
	
	private boolean checkIfIdExistsInStudentTable(Long id) {
	    try (Connection connection = dataSource.getConnection()) {
	        String sql = "SELECT COUNT(*) FROM STUDENT WHERE id = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            preparedStatement.setLong(1, id);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    int count = resultSet.getInt(1);
	                    return count > 0; // If count > 0, the ID exists in the "STUDENT" table
	                }
	            }
	        }
	    } catch (SQLException e) {
	        // Handle exceptions, e.g., log or throw an exception
	        e.printStackTrace();
	    }

	    // If there was an error or the ID was not found, return false
	    return false;
	}

	
	boolean checkIfIdExistsInPersonTable(Long id) {
	    try (Connection connection = dataSource.getConnection()) {
	        String sql = "SELECT COUNT(*) FROM PERSON WHERE id = ?";
	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            preparedStatement.setLong(1, id);

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                if (resultSet.next()) {
	                    int count = resultSet.getInt(1);
	                    return count > 0; // If count > 0, the ID exists in the "PERSON" table
	                }
	            }
	        }
	    } catch (SQLException e) {
	        // Handle exceptions, e.g., log or throw an exception
	        e.printStackTrace();
	    }

	    // If there was an error or the ID was not found, return false
	    return false;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<Person> personItemWriter() {
		JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();

		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO person (id, firstName, lastName, birthdate) VALUES (:id1, :firstName, :lastName, :birthdate) " +
	            "ON DUPLICATE KEY UPDATE " +
	            "firstName=VALUES(firstName), lastName=VALUES(lastName), birthdate=VALUES(birthdate)");
	    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
//		itemWriter.afterPropertiesSet();
		itemWriter.setAssertUpdates(false); // Do not assert that updates were performed

	    try {
	        itemWriter.afterPropertiesSet();
	    } catch (DataIntegrityViolationException ex) {
	        // Handle the exception here
	    	logger.log(Level.SEVERE,"Error while writing person data: " + ex.getMessage()); // You can log the exception or perform custom error handling
	    }

		return itemWriter;
	}
	
	
	
	
	
	@Bean
	public JdbcBatchItemWriter<Student> studentItemWriter() {
	    JdbcBatchItemWriter<Student> itemWriter = new JdbcBatchItemWriter<>();

	    itemWriter.setDataSource(this.dataSource);
	    itemWriter.setSql("INSERT INTO STUDENT (id, firstName, lastName, birthdate) VALUES (:id1, :firstName, :lastName, :birthdate) " +
	            "ON DUPLICATE KEY UPDATE " +
	            "firstName=VALUES(firstName), lastName=VALUES(lastName), birthdate=VALUES(birthdate)");
	    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());

	    // Handle duplicate key exceptions
	    itemWriter.setAssertUpdates(false); // Do not assert that updates were performed

	    try {
	        itemWriter.afterPropertiesSet();
	    } catch (DataIntegrityViolationException ex) {
	        // Handle the exception here
	    	logger.log(Level.SEVERE,"Error while writing student data: " + ex.getMessage());
	    
	    }
	    
	    


	    return itemWriter;
	}




	@Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Person, Person>chunk(10)
                .reader(personItemReader())  // Read from the flat file and insert into the "person" table
                .writer(personItemWriter())  // Write to the "person" table
                .build();
    }
	
	
	@Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<Person, Student>chunk(10)
                .reader(personItemReader())
                .processor(personToStudentProcessor())
                .writer(studentItemWriter())
                .build();
    }

	@Bean
    public Job job() {
        return jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
                .on("COMPLETED").to(deleteStudentRecordsStep())
                .end()
                .build();
    }
	
	@Bean
	public Step deleteStudentRecordsStep() {
	    return stepBuilderFactory.get("deleteStudentRecordsStep")
	            .tasklet((contribution, chunkContext) -> {
	                // Execute the method to delete records from STUDENT table
	                deleteRecordsNotInStudentTable();
	                return RepeatStatus.FINISHED;
	            })
	            .build();
	}
	
}
