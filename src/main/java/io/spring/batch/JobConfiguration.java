package io.spring.batch;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Component;


@Configuration
@Slf4j
@EnableBatchProcessing
public class JobConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(JobConfiguration.class);	
	

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	 @Autowired
	 private StudentItemWriter studentItemWriter;
	 
//	 @Autowired
//	 private FlatFileItemReader<Person> personItemReader;
//	 
	 
	
	 @Bean
	 public JdbcBatchItemWriter<Person> jdbcPersonItemWriter(DataSource dataSource) {
	     return new PersonItemWriter(dataSource).personItemWriter(dataSource);
	 }
	 
	 	@Autowired
	    private DataSource dataSource; // Make sure to inject your DataSource

	    @Bean
	    public CustomItemWriter customItemWriter(JdbcTemplate jdbcTemplate) {
	        return new CustomItemWriter(jdbcTemplate);
	    }
	    
	    @Bean
	    public JdbcTemplate jdbcTemplate() {
	        return new JdbcTemplate(dataSource);
	    }
//	 @Bean
//	    public ItemProcessor<Person, Person> customPersonItemProcessor() {
//	       Set<Long> flatFileIds = readIdsFromFlatFile(); 
//	       return new CustomPersonItemProcessor(dataSource, flatFileIds);
//	    }

	 @Bean
	 public ItemProcessor<Person, Person> personItemProcessor() {
	     return new PersonItemProcessor(dataSource);
	 }
	 
//	 @Autowired
//	 private JdbcBatchItemWriter<Person> personItemWriter;

	 
	 @Bean
	 public ItemProcessor<Person, Student> personToStudentProcessor() {
	     return new PersonToStudentProcessor(dataSource);
	 }
	
	public JobConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }
	
	
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
	public JdbcBatchItemWriter<Person> personItemWriter() {
		JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();

		itemWriter.setDataSource(this.dataSource);
		itemWriter.setSql("INSERT INTO person (id, first_Name, last_Name, birthdate) VALUES (:id1, :firstName, :lastName, :birthdate) " +
	            "ON DUPLICATE KEY UPDATE " +
	            "first_Name=VALUES(first_Name), last_Name=VALUES(last_Name), birthdate=VALUES(birthdate)");
	    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());

	    itemWriter.setAssertUpdates(false);


	    try {
	        itemWriter.afterPropertiesSet();
	    } catch (DataIntegrityViolationException ex) {

	    	logger.info("Error while writing person data: " ,ex.getMessage()); 

	    }

		return itemWriter;
	}

	
//	@Autowired
//    private Set<Long> flatFileIds;  // Assuming you've defined flatFileIds elsewhere in your configuration

//    @Bean
//    public CustomDeleteItemWriter customDeleteItemWriter() {
//        // Pass the dataSource and flatFileIds (Set<Long>) to the constructor
//        return new CustomDeleteItemWriter(dataSource, flatFileIds);
//    }



	

// ******* Working Step******

	@Bean
	public Step step1() {
	    return stepBuilderFactory.get("step1")
	            .<Person, Person>chunk(10)
	            .reader(personItemReader())
	            .processor(personItemProcessor())
//	            .writer(personItemWriter)
//	            .writer(customItemWriter(jdbcTemplate()))
//	            .writer(jdbcPersonItemWriter(dataSource))
	       .writer(personItemWriter())
	            .build();
	}
	
// ******* Working Step******
	
	
//	@Bean
//    public Step step1() {
//        return stepBuilderFactory.get("step1")
//            .<Person, Person>chunk(10)
//            .reader(personItemReader())
//            .processor(customPersonItemProcessor())
//            .writer(personItemWriter())
//            .build();
//    }
	
//	@Bean
//	public Step step1() {
//	    return stepBuilderFactory.get("step1")
//	            .<Person, Person>chunk(10)
//	            .reader(personItemReader())  
//	            .writer(new CompositeItemWriter<Person>() {{
//	                setDelegates(Arrays.asList(personItemWriter(), deleteItemWriter()));
//	            }})
//	            .build();
//	}
	
//	@Bean
//	public Step step1() {
//	    Set<Long> idsInFlatFile = readIdsFromFlatFile();
//	    return stepBuilderFactory.get("step1")
//	        .<Person, Person>chunk(10)
//	        .reader(personItemReader())
//	        .writer(new CompositeItemWriter<Person>() 
//	        {{
//	            setDelegates(Arrays.asList(personItemWriter(), new CustomDeleteItemWriter(dataSource, idsInFlatFile)));
//	        }})
//	        .build();
//	}
    
//    @Bean
//    public Step step1() {
//        Set<Long> flatFileIds = readIdsFromFlatFile(); // Read IDs from the flat file
//        return stepBuilderFactory.get("step1")
//                .<Person, Person>chunk(10)
//                .reader(personItemReader())
//                .processor(new CustomPersonItemProcessor(dataSource, flatFileIds))
////                .writer(new CustomDeleteItemWriter(dataSource, flatFileIds)) // Pass the flatFileIds
//                .writer(personItemWriter())
//                .build();
//    }
	
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


	
private Set<Long> readIdsFromFlatFile() {
    Set<Long> flatFileIds = new HashSet<>();
    FlatFileItemReader<Person> reader = personItemReader();

    try {
        reader.open(new ExecutionContext());

        Person item;
        do {
            item = reader.read();
            if (item != null) {
                flatFileIds.add(item.getId1());
            }
        } while (item != null);

        reader.close();
    } catch (Exception e) {
        // Handle any exceptions
        e.printStackTrace();
    }

    return flatFileIds;
    
}

}
