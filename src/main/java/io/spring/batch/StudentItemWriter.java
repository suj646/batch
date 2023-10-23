package io.spring.batch;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class StudentItemWriter implements ItemWriter<Student> {

    private final JdbcBatchItemWriter<Student> studentWriter;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.studentWriter = new JdbcBatchItemWriterBuilder<Student>()
                .dataSource(dataSource)
                .sql("INSERT INTO STUDENT (id, firstName, lastName, birthdate) VALUES (:id1, :firstName, :lastName, :birthdate) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "firstName=VALUES(firstName), lastName=VALUES(lastName), birthdate=VALUES(birthdate)")
                .itemSqlParameterSourceProvider(new ItemSqlParameterSourceProvider<Student>() {
                    @Override
                    public SqlParameterSource createSqlParameterSource(Student student) {
                        MapSqlParameterSource source = new MapSqlParameterSource();
                        source.addValue("id1", student.getId1());
                        source.addValue("firstName", student.getFirstName());
                        source.addValue("lastName", student.getLastName());
                        source.addValue("birthdate", student.getBirthdate());
                        return source;
                    }
                })
                .assertUpdates(false)
                .build();
        studentWriter.afterPropertiesSet();
    }

    @Override
    public void write(List<? extends Student> students) throws Exception {
        studentWriter.write(students);
        deleteRecordsNotInStudentTable();
    }

    private void deleteRecordsNotInStudentTable() {
        String deleteSql = "DELETE FROM STUDENT WHERE id NOT IN (SELECT id FROM PERSON)";
        jdbcTemplate.update(deleteSql);
    }
}




