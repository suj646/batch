package io.spring.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;

import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Transactional
public class CustomDeleteItemWriter implements ItemWriter<Person> {
    private static final Logger logger = LoggerFactory.getLogger(CustomDeleteItemWriter.class);
    private final DataSource dataSource;
    private final Set<Long> flatFileIds;
    private final PersonItemReader personItemReader;

    public CustomDeleteItemWriter(DataSource dataSource, Set<Long> readFlatFileIds, PersonItemReader personItemReader) {
        this.dataSource = dataSource;
        this.flatFileIds = personItemReader.readFlatFileIds();
        this.personItemReader = personItemReader;
    }

    @Override
    public void write(List<? extends Person> items) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            // Use a subquery to delete only the records in the "person" table that do not match the flat file IDs
            String sql = "DELETE FROM person WHERE id = ? AND id NOT IN ("
                + flatFileIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "))
                + ")";
            logger.info("CustomDeleteItemWriter: SQL statement: {}", sql); // Log the SQL statement

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            	for (Person person : items) {
            	preparedStatement.setLong(1, person.getId1());
            	}
//                for (Person person : items) {
////                    logger.info("CustomDeleteItemWriter: Deleting record with ID: {}", person.getId1());
//                    preparedStatement.setLong(1, person.getId1());
//                    preparedStatement.addBatch();
//                }
                preparedStatement.executeUpdate();
//                preparedStatement.executeLargeUpdate();
//                logger.info("Deleted {} rows from PERSON table", deletedRows);
//                int[] batchResult = preparedStatement.executeBatch(); // Execute the batch of statements and capture the result
//                logger.info("CustomDeleteItemWriter: Deleted {} records from the database.", batchResult.length);
            }
        } catch (SQLException e) {
            logger.error("Error in CustomDeleteItemWriter", e);
        }
    	
//    	    try (Connection connection = dataSource.getConnection()) {
//    	    	Set<Long> idsInFlatFile=personItemReader.readFlatFileIds();
//    	        String sql = "DELETE FROM PERSON WHERE id NOT IN ("
//    	            + idsInFlatFile.stream()
//    	                .map(String::valueOf)
//    	                .collect(Collectors.joining(","))
//    	            + ")";
//
//    	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//    	            int deletedRows = preparedStatement.executeUpdate();
//    	            logger.info("Deleted {} rows from PERSON table", deletedRows); // Log the number of rows deleted
//    	        }
//    	    } catch (SQLException e) {
//    	        logger.error("Error while deleting records from PERSON table", e); // Log the error
//    	    }
    }
    
}
