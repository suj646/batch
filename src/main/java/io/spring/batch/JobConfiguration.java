package io.spring.batch;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import lombok.extern.slf4j.Slf4j;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
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

import io.spring.batch.domain.StepService;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;


import org.springframework.batch.core.StepExecutionListener;

import com.mysql.cj.log.Log;


@Configuration
@Slf4j
public class JobConfiguration {
	
	private static final Logger logger = Logger.getLogger(JobConfiguration.class.getName());

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	 @Autowired
	 private StudentItemWriter studentItemWriter;

	@Autowired
	private PersonItemWriter personItemWriter;

	@Autowired
	private DataSource dataSource; // Make sure to inject your DataSource

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public ItemProcessor<Person, Person> personItemProcessor() {
		return new PersonItemProcessor(dataSource);
	}

	@Bean
	public ItemProcessor<Person, Student> personToStudentProcessor() {
		return new PersonToStudentProcessor(dataSource);
	}

	public JobConfiguration(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	public PersonItemReader personItemReader() {
		return new PersonItemReader();
	}
	
	@Bean
	public CustomDeleteItemWriter customDeleteItemWriter(PersonItemReader personItemReader) {
	    Set<Long> flatFileIds = personItemReader.readFlatFileIds(); // Use the method to read flat file IDs
	    return new CustomDeleteItemWriter(dataSource, flatFileIds, personItemReader);
	}
	
 
	@Autowired
    private StepService stepService;
    
    
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1And3")
            .<Person, Person>chunk(10)
            .reader(personItemReader())
            .writer(personItemWriter)
            .listener(new StepExecutionListener() {
                @Override
                public void beforeStep(StepExecution stepExecution) {
                    stepService.beforeStep(stepExecution);
                }

                @Override
                public ExitStatus afterStep(StepExecution stepExecution) {
                    stepService.afterStep(stepExecution);
                    return null;
                }
            })
            .build();
    }
	
	
	@Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .<Person, Student>chunk(10)
                .reader(personItemReader())
                .processor(personToStudentProcessor())
                .writer(studentItemWriter)
                .build();
    }





	@Bean
	public Job job() {
	    return jobBuilderFactory.get("job")
	            .start(step1())
	            .next(step2())
	            .build();
	}
}
