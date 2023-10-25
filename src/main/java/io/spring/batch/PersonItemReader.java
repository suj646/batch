package io.spring.batch;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.core.io.ClassPathResource;
import io.spring.batch.domain.PersonFieldSetMapper;
import io.spring.batch.domain.Person;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.core.io.ClassPathResource;

public class PersonItemReader extends FlatFileItemReader<Person> {

  public PersonItemReader() {
      this.setLinesToSkip(1);
      this.setResource(new ClassPathResource("/data/person.csv"));

      DefaultLineMapper<Person> customerLineMapper = new DefaultLineMapper<>();

      DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
      tokenizer.setNames(new String[] {"id1", "firstName", "lastName", "birthdate"});

      customerLineMapper.setLineTokenizer(tokenizer);
      customerLineMapper.setFieldSetMapper(new PersonFieldSetMapper());
      customerLineMapper.afterPropertiesSet();
      this.setLineMapper(customerLineMapper);
  }
}
