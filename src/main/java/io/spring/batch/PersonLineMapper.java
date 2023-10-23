package io.spring.batch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.batch.item.file.LineMapper;

import io.spring.batch.domain.Person;

public class PersonLineMapper implements LineMapper<Person> {
    @Override
    public Person mapLine(String line, int lineNumber) throws Exception {
    	String id1Str = line.substring(0, 2);
        String firstName = line.substring(3,8);
        String lastName = line.substring(9, 16);
        String birthdateStr = line.substring(17,35).trim(); // Assuming it's everything after position 12

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        // Now, parse the id1 and birthdate strings
        long id1 = Long.parseLong(id1Str);
        Date birthdate = null;
        try {
            birthdate = dateFormat.parse(birthdateStr);
        } catch (ParseException e) {
            // Handle the date parsing exception
            e.printStackTrace();
        }

        return new Person(id1, firstName, lastName, birthdate);
    }
}


