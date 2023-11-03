package io.spring.batch.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


public class PersonFieldSetMapper implements FieldSetMapper<Person> {
    @Override
    public Person mapFieldSet(FieldSet fieldSet) {
        Person person = new Person();
        person.setId1(fieldSet.readLong("id1"));
        person.setFirstName(fieldSet.readString("firstName"));
        person.setLastName(fieldSet.readString("lastName"));
        person.setBirthdate(fieldSet.readDate("birthdate"));
        return person;
    }
}
