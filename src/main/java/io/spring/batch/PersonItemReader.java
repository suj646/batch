package io.spring.batch;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import io.spring.batch.domain.PersonFieldSetMapper;
import io.spring.batch.domain.Person;



@Configuration

public class PersonItemReader{

  
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
}
