package org.talend.kickoff.faro2017.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.inject.spi.CDI;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.batchee.test.JobLauncher;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class VolatileProvisioning implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final Properties config = new Properties();
                config.setProperty("maxSymbols", "10");
                assertEquals(BatchStatus.COMPLETED, JobLauncher.start("quote", config).getBatchStatus());
                try {
                    base.evaluate();
                } finally {
                    final CDI<Object> current = CDI.current();
                    final RequestContextController requestContextController = current.select(RequestContextController.class).get();
                    requestContextController.activate();
                    final EntityManager em = current.select(EntityManager.class).get();
                    try {
                        final EntityTransaction transaction = em.getTransaction();
                        transaction.begin();
                        em.createQuery("delete from Quote")
                          .executeUpdate();
                        transaction.commit();
                    } finally {
                        requestContextController.deactivate();
                    }
                }
            }
        };
    }
}
