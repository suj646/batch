package io.spring.batch;


import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

public class StudentItemWriter implements ItemWriter<Student> {
    private JdbcTemplate jdbcTemplate;

    public StudentItemWriter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void write(java.util.List<? extends Student> items) throws Exception {
        // No need to write the Student records here since they were updated in the processor
    }
}

