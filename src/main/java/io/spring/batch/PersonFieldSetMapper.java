package io.spring.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PersonFieldSetMapper implements FieldSetMapper<Person> {
    @Override
    public Person mapFieldSet(FieldSet fieldSet) {
        Person person = new Person();
//        person.setId1(fieldSet.readLong("id1"));
//        String id = fieldSet.readString("id1");
//        person.setId1(convertAlphaIdToNumeric(id));
        person.setId1(fieldSet.readLong("id1"));
        person.setFirstName(fieldSet.readString("firstName"));
        person.setLastName(fieldSet.readString("lastName"));
        person.setBirthdate(fieldSet.readDate("birthdate"));
        return person;
    }
    
    private int convertAlphaIdToNumeric(String alphaId) {
        switch (alphaId) {
            case "A":
                return 1;
            case "B":
                return 2;
            case "C":
                return 3;
            case "D":
                return 4;
            case "E":
                return 5;
            case "F":
            	return 6;
            case "G":
            	return 7;
            case "H":
            	return 8;
            case "I":
            	return 9;
            case "J":
            	return 10;
            default:
                // Handle other cases or return a default value (e.g., 0).
                return 0;
        }
    }
}
