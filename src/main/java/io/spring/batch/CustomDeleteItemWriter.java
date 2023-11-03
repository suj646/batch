package io.spring.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;

public class CustomDeleteItemWriter implements ItemWriter<Person> {
    private static final Logger logger = LoggerFactory.getLogger(CustomDeleteItemWriter.class);
    private final DataSource dataSource;
    private final Set<Long> flatFileIds;

    public CustomDeleteItemWriter(DataSource dataSource, Set<Long> flatFileIds) {
        this.dataSource = dataSource;
        this.flatFileIds = flatFileIds;
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
                    logger.info("CustomDeleteItemWriter: Deleting record with ID: {}", person.getId1());
                    preparedStatement.setLong(1, person.getId1());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                logger.info("CustomDeleteItemWriter: Deleted {} records from the database.", items.size());
            }
        } catch (SQLException e) {
            logger.error("Error in CustomDeleteItemWriter", e);
        }
    }
}
