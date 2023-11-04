package io.spring.batch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

//@Configuration
public class Step3Config {
	
	 private final PersonItemReader personItemReader;
	
	public Step3Config(PersonItemReader personItemReader, DataSource dataSource) {
		super();
		this.personItemReader = personItemReader;
		this.dataSource = dataSource;
	}	

	@Autowired
	public DataSource dataSource;
	
	private Set<Long> readIdsFromFlatFile() {
	    Set<Long> idsInFlatFile = new HashSet<>();
	    FlatFileItemReader<Person> reader = personItemReader;

	    try {
	        reader.open(new ExecutionContext());

	        Person item;
	        do {
	            item = reader.read();
	            if (item != null) {
	                idsInFlatFile.add(item.getId1());
	            }
	        } while (item != null);

	        reader.close();
	    } catch (Exception e) {
	        // Handle any exceptions
	        e.printStackTrace();
	    }

	    return idsInFlatFile;
	}


	private void deleteIdsNotInFlatFile(Set<Long> idsInFlatFile) {
	    try (Connection connection = dataSource.getConnection()) {
	        String sql = "DELETE FROM PERSON WHERE id NOT IN ("
	            + idsInFlatFile.stream()
	                .map(String::valueOf)
	                .collect(Collectors.joining(","))
	            + ")";

	        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	            int deletedRows = preparedStatement.executeUpdate();
	            System.out.println("Deleted " + deletedRows + " rows from PERSON table.");
	        }
	    } catch (SQLException e) {
	        System.out.println("Error while deleting records from PERSON table: " + e.getMessage());
	    }
	}

    @Bean
    public Step step3(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                	 Set<Long> idsInFlatFile = readIdsFromFlatFile();
 	                deleteIdsNotInFlatFile(idsInFlatFile);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}

