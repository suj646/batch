package io.spring.batch.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;



public class PersonFieldSetMapper implements FieldSetMapper<Person> {

	@Override
	public Person mapFieldSet(FieldSet fieldSet) throws BindException {
		return new Person(fieldSet.readLong("id1"),
				fieldSet.readString("firstName"),
				fieldSet.readString("lastName"),
				fieldSet.readDate("birthdate", "yyyy-MM-dd HH:mm:ss"));
	}
}