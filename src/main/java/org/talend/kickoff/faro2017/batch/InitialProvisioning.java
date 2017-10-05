package org.talend.kickoff.faro2017.batch;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Slf4j
@ApplicationScoped
public class InitialProvisioning {
    @Inject
    private JobOperator jobOperator;

    @Inject
    @ConfigProperty(name = "provisioning.skip", defaultValue = "false")
    private Boolean skip;

    void init(@Observes @Initialized(ApplicationScoped.class) final Object init) {
        if (skip) {
            log.info("Initial provisioning configured to be skipped");
            return;
        }
        final long id = jobOperator.start("quote", new Properties());
        log.info("Reloading data, job identifier={}", id);
    }
}
