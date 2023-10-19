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
	
	@Bean
	public CustomItemProcessor customItemProcessor(JdbcTemplate jdbcTemplate) {
	    return new CustomItemProcessor(jdbcTemplate);
	}
	
	@Bean
	public StudentItemWriter studentItemWriter(JdbcTemplate jdbcTemplate) {
	    return new StudentItemWriter(jdbcTemplate);
	}

	
	@Autowired
	public DataSource dataSource;
	
	public JdbcTemplate jdbcTemplate;
	
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
	    return new JdbcTemplate(dataSource);
	}
	
	
	@Bean
	public FlatFileItemReader<Person> personItemReader() {
		FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
//		reader.setLinesToSkip(1);
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

//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Bean
//	public JdbcBatchItemWriter<Person> personItemWriter() {
//		JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
//
//		itemWriter.setDataSource(this.dataSource);
//		itemWriter.setSql("INSERT INTO PERSON VALUES (:id, :firstName, :lastName, :birthdate)");
//		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
//		itemWriter.afterPropertiesSet();
//		return itemWriter;
//	}

	@Bean
	public Step step1() {
	    return stepBuilderFactory.get("step1")
	            .<Person, Student>chunk(10)
	            .reader(personItemReader())
	            .processor(customItemProcessor(jdbcTemplate(dataSource)))
	            .writer(studentItemWriter(jdbcTemplate(dataSource)))
	            .build();
	}

	

	

	

	
	@Bean
	public Job job() {
	    return jobBuilderFactory.get("job")
	            .start(step1())
	           
	            .build();
	}
}


