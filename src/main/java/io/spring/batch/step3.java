//package io.spring.batch;
//import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
//import org.springframework.batch.item.database.JdbcBatchItemWriter;
//import org.springframework.batch.item.database.JdbcCursorItemReader;
//import org.springframework.context.annotation.Bean;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//
//import io.spring.batch.domain.Person;
//import io.spring.batch.domain.Person2;
//
//import javax.sql.DataSource;
//
//import org.springframework.batch.item.ItemProcessor;
//
//public class step3 {
//
//	private final JdbcTemplate jdbcTemplate;
//
//    public step3(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//	
//
//@Bean
//public JdbcCursorItemReader<Person> person2ItemReader(DataSource dataSource) {
//    JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
//    reader.setDataSource(dataSource);
//    reader.setSql("SELECT id, firstName FROM Person");
//    reader.setRowMapper(new PersonRowMapper());
//    return reader;
//}
//
//@Bean
//public JdbcBatchItemWriter<Person> person2ItemWriter(DataSource dataSource) {
//    JdbcBatchItemWriter<Person> itemWriter = new JdbcBatchItemWriter<>();
//    itemWriter.setDataSource(dataSource);
//    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
//    itemWriter.setSql("UPDATE Person2 SET firstName = :firstName WHERE id = :id");
//    return itemWriter;
//}
//
//@Bean
//public ItemProcessor<Person, Person2> person2ItemProcessor() {
//    return item -> { 
//        // Compare the firstName value from Person and update Person2 if it matches
//        // You can add your logic here to decide how to update
//        if (item != null) {
//            String firstName = item.getFirstName();
//            if (firstName != null) {
//                // Check if there is a matching record in Person2
//                // You can modify this query as needed
//                String selectQuery = "SELECT id FROM Person2 WHERE firstName = ?";
//                Integer id = jdbcTemplate.query(selectQuery, new Object[]{firstName}, (rs, rowNum) -> rs.getInt("id")).stream().findFirst().orElse(null);
//                if (id != null) {
//                    // Update the Person2 record
//                    return new Person2(id, "Akash"); // Modify the new value as needed
//                }
//            }
//        }
//        return null; // Return null for records that don't need an update
//    };
//}
//
//
//
//
//}
