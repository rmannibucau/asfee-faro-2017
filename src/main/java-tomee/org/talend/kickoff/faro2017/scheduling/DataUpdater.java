package org.talend.kickoff.faro2017.scheduling;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Slf4j
@Singleton // take care it is the ejb one
public class DataUpdater {
    @Inject
    private JobOperator operator;

    @Schedule // daily is the default
    public void onTrigger() {
        final long id = operator.start("quote", new Properties());
        log.info("Reloading data, job identifier={}", id);
    }
}
