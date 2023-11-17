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
import org.springframework.stereotype.Component;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Component
public class StudentItemWriter implements ItemWriter<Student> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(List<? extends Student> students) throws Exception {
        for (Student student : students) {
            if (studentExists(student.getId1())) {
                updateStudent(student);
            } else {
                insertStudent(student);
            }
        }

        List<Long> deletedIds = deleteRecordsNotInStudentTable();
        if (!deletedIds.isEmpty()) {
            System.out.println("Deleted IDs: " + deletedIds);
        }
    }

    private void insertStudent(Student student) {
        String sql = "INSERT INTO student (id, first_name, last_name, birthdate, age, city) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(
            sql,
            student.getId1(),
            student.getFirstName(),
            student.getLastName(),
            student.getBirthdate(),
            StudentDefaults.DEFAULT_AGE, // Default age from constants
            StudentDefaults.DEFAULT_CITY // Default city from constants
        );
    }


    private void updateStudent(Student student) {
        String sql = "UPDATE student SET first_name = ?, last_name = ?, birthdate = ?, age = ?, city = ? WHERE id = ?";
        jdbcTemplate.update(
            sql,
            student.getFirstName(),
            student.getLastName(),
            student.getBirthdate(),
            StudentDefaults.UPDATED_AGE, // Default age from constants
            StudentDefaults.DEFAULT_CITY_UPDATED, // Default city from constants
            student.getId1()
        );
    }

    private boolean studentExists(long id) {
        String sql = "SELECT COUNT(*) FROM student WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    private List<Long> deleteRecordsNotInStudentTable() {
        String selectDeletedIdsSql = "SELECT id FROM STUDENT WHERE id NOT IN (SELECT id FROM PERSON)";
        List<Long> deletedIds = jdbcTemplate.queryForList(selectDeletedIdsSql, Long.class);

        String deleteSql = "DELETE FROM STUDENT WHERE id NOT IN (SELECT id FROM PERSON)";
        jdbcTemplate.update(deleteSql);

        return deletedIds;
    }
}


