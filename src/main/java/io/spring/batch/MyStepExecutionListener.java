package io.spring.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import io.spring.batch.domain.DatabaseUtility;

public class MyStepExecutionListener implements StepExecutionListener {

    private final DatabaseUtility databaseUtility;

    public MyStepExecutionListener(DatabaseUtility databaseUtility) {
        this.databaseUtility = databaseUtility;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Before step execution");
        // Additional logic before step execution
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("After step execution");

        // Access the step-scoped DatabaseUtility bean
        databaseUtility.deleteIdsNotInFlatFile(databaseUtility.readIdsFromFlatFile());

        // Additional logic after step execution
        return ExitStatus.COMPLETED;
    }
}
