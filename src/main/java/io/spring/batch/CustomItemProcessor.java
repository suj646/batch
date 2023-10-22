package io.spring.batch;

import org.springframework.batch.item.ItemProcessor;

import io.spring.batch.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor<T> implements ItemProcessor<T, T> {
    private static final Logger logger = LoggerFactory.getLogger(CustomItemProcessor.class);

    @Override
    public T process(T item) throws Exception {
        if (item instanceof Person) {
            Person person = (Person) item;
            logger.info("Processing Person: {}", person);
            // Process the name field to convert it to uppercase.
            person.setFirstName(person.getFirstName().toUpperCase());
        } else if (item instanceof Student) {
            Student student = (Student) item;
            logger.info("Processing Student: {}", student);
            // Process the name field to convert it to uppercase.
            student.setFirstName(student.getFirstName().toUpperCase());
        }

        return item;
    }
}


