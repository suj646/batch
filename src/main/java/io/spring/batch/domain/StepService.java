package io.spring.batch.domain;

import java.util.Set;

import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StepService {

    private final DatabaseUtility databaseUtility;

   
    public StepService(DatabaseUtility databaseUtility) {
        this.databaseUtility = databaseUtility;
    }

    public void beforeStep(StepExecution stepExecution) {
        Set<Long> idsInFlatFile = databaseUtility.readIdsFromFlatFile();
        databaseUtility.deleteIdsNotInFlatFile(idsInFlatFile);
    }

    public void afterStep(StepExecution stepExecution) {
        // Your afterStep logic here
    }
}

