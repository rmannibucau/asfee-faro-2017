package org.talend.kickoff.faro2017.batch;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class BatchConfiguration {

    @Produces
    @ApplicationScoped
    public JobOperator jobOperator() {
        return BatchRuntime.getJobOperator();
    }
}
