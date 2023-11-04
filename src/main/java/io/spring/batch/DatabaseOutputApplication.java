package io.spring.batch;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableBatchProcessing
@ComponentScan(basePackages = "io.spring.batch")
public class DatabaseOutputApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseOutputApplication.class);

	@Autowired
	public JobLauncher jobLauncher;

	@Autowired
	public Job job;
	
	public static void main(String[] args) {
		SpringApplication.run(DatabaseOutputApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", UUID.randomUUID().toString())
                .addLong("JobId",System.currentTimeMillis())
                .addLong("time",System.currentTimeMillis()).toJobParameters();
		
		JobExecution execution = jobLauncher.run(job, jobParameters);
		System.out.println("STATUS :: "+execution.getStatus());
		
	}
}