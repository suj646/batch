package io.spring.batch.domain;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PersonFieldSetMapper implements FieldSetMapper<Person> {

	@Override
	public Person mapFieldSet(FieldSet fieldSet) throws BindException {
		return new Person((int) fieldSet.readLong("id"),
				fieldSet.readString("firstName")
				);
	}

//	@Override
//	public Person mapFieldSet(FieldSet fieldSet) throws BindException {
//		// TODO Auto-generated method stub
//		return null;
//	}
}