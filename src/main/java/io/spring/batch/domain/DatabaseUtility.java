package io.spring.batch.domain;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import io.spring.batch.PersonItemReader;

@Component
public class DatabaseUtility {

    private final DataSource dataSource;
    private final PersonItemReader personItemReader;

    public DatabaseUtility(DataSource dataSource, PersonItemReader personItemReader) {
        this.dataSource = dataSource;
        this.personItemReader = personItemReader;
    }

    public Set<Long> readIdsFromFlatFile() {
        // Use the PersonItemReader to read IDs from the CSV file
        return personItemReader.readFlatFileIds();
    }

    public void deleteIdsNotInFlatFile(Set<Long> idsInFlatFile) {
        if (idsInFlatFile.isEmpty()) {
            System.out.println("No records to delete.");
            return;
        }

        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM PERSON WHERE id NOT IN (" +
                    idsInFlatFile.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")) +
                    ")";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int deletedRows = preparedStatement.executeUpdate();
                System.out.println("Deleted " + deletedRows + " rows from PERSON table.");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting records from PERSON table: " + e.getMessage());
        }
    }
}
