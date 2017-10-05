package org.talend.kickoff.faro2017.scheduling;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Slf4j
@ApplicationScoped
@Scheduled(cronExpression = "{yahoo.reloading.cron}") // all minutes
public class DataUpdater implements Job {
    @Inject
    private JobOperator operator;

    @Override
    public void execute(final JobExecutionContext context) throws JobExecutionException {
        final long id = operator.start("quote", new Properties());
        log.info("Reloading data, job identifier={}", id);
    }
}
