package io.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<Person, Person> {
    private static final Logger logger = LoggerFactory.getLogger(PersonProcessor.class);

    @Override
    public Person process(Person person) {
//        logger.info("Processing person: {}", person);

        

        return person;
    }
}
