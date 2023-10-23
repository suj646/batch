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
	
	private final JdbcTemplate jdbcTemplate; 

    public JobConfiguration(DataSource dataSource) {
        // Initialize the JdbcTemplate with the DataSource
    	this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
	

	
	@Autowired
	public DataSource dataSource;
	
	
	@Bean
	public FlatFileItemReader<Person> personItemReader() {
	    return new PersonItemReader();
	}
	
	@Bean
	public ItemProcessor<Person, Student> personToStudentProcessor() {
	    return new PersonToStudentProcessor(dataSource);
	}
	
	
	@Bean
	public JdbcBatchItemWriter<Person> personItemWriter() {
	    return new PersonItemWriter(dataSource);
	}

	
	@Bean
	public StudentItemWriter studentItemWriter() {
	    return new StudentItemWriter(dataSource);
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
//                .on("COMPLETED").to(deleteStudentRecordsStep())
//                .end()
                .build();
    }
	

	
}
