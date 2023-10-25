//package io.spring.batch;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import javax.persistence.criteria.CriteriaBuilder.In;
//
//import org.springframework.batch.item.file.LineMapper;
//
//
//
//
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
////public class PersonLineMapper implements LineMapper<Person> {
////    @Value("${date.format}")
////    private String dateFormatPattern;
////
////    @Override
////    public Person mapLine(String line, int lineNumber) throws Exception {
////        String id1Str = line.substring(0, 2);
////        String firstName = line.substring(3,6); // Updated position
////        String lastName = line.substring(7, 11); // Updated position
////        String contact=line.substring(12,14);
////        String birthdateStr = line.substring(15, 33).trim();
////
////        // Additional fields for lastUpdateTs, hostNo, and prodBrndNo
////        String lastUpdateTsStr = line.substring(34, 53).trim();
////        String hostNoStr = line.substring(54, 57).trim();
////        String prodBrndNoStr = line.substring(58, 60).trim();
////
////        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
////
////        // Now, parse the id1, birthdate, lastUpdateTs, hostNo, and prodBrndNo strings
////        long id1 = Long.parseLong(id1Str);
////        Date birthdate = null;
////        Timestamp lastUpdateTs = null; // Use Timestamp for lastUpdateTs
////        short hostNo = Short.parseShort(hostNoStr);
////        int prodBrndNo = Integer.parseInt(prodBrndNoStr);
////
////        try {
////            birthdate = dateFormat.parse(birthdateStr);
////            lastUpdateTs = new Timestamp(dateFormat.parse(lastUpdateTsStr).getTime());
////        } catch (ParseException e) {
////            // Handle the date parsing exception
////            e.printStackTrace();
////        }
////
////        return new Person(id1, firstName.trim(), lastName.trim(), contact.trim(), birthdate, lastUpdateTs, hostNo, prodBrndNo);
////    }
////}
//
//import org.springframework.batch.item.file.LineMapper;
//
//public class PersonLineMapper implements LineMapper<Person> {
//    @Override
//    public Person mapLine(String line, int lineNumber) throws Exception {
//        String[] parts = line.split("\\s+");
//
//        if (parts.length < 4) {
//            // Handle the case where the input format is not as expected
//            throw new IllegalArgumentException("Invalid input format");
//        }
//
//        String id1Str = parts[0];
//        String firstName = parts[1];
//        String lastName = parts[2];
//        String prodBrndNoStr = parts[3]; // Combine date and time parts
//
////        Timestamp birthdate = Timestamp.valueOf(birthdateStr);
//
//        long id1 = Long.parseLong(id1Str);
//
//        Integer prodBrndNo=Integer.parseInt(prodBrndNoStr);
//        Person person = new Person();
//        person.setId1(id1);
//        person.setFirstName(firstName);
//        person.setLastName(lastName);
//
//        return person;
//    }
//}
//
//
//
//
