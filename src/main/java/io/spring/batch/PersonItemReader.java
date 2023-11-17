package io.spring.batch;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.core.io.ClassPathResource;


public class PersonItemReader extends FlatFileItemReader<Person> {
	 private static final Logger logger = LoggerFactory.getLogger(CustomDeleteItemWriter.class);
	
    public PersonItemReader() {
        setLinesToSkip(1);
        setResource(new ClassPathResource("/data/person.csv"));

        DefaultLineMapper<Person> customerLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[] {"id1", "firstName", "lastName", "birthdate"});

        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new PersonFieldSetMapper());
        customerLineMapper.afterPropertiesSet();
        setLineMapper(customerLineMapper);
    }
    
   
}

