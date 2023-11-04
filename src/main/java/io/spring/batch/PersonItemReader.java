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
	private Set<Long> flatFileIds;
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
    
    public Set<Long> readFlatFileIds() {
        if (flatFileIds == null) {
            flatFileIds = new HashSet<>();

            try {
                open(new ExecutionContext());

                Person item;
                do {
                    item = read();
                    if (item != null) {
                        flatFileIds.add(item.getId1());
                    }
                } while (item != null);

                close();
            } catch (Exception e) {
                // Handle any exceptions
//               e.printStackTrace();
               
               logger.info("Error while reading the person ", e);
            }
        }

        return flatFileIds;
    }
}

