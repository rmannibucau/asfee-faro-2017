package org.talend.kickoff.faro2017.test;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import javax.batch.runtime.BatchStatus;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;

import org.apache.batchee.test.JobLauncher;
import org.apache.deltaspike.core.api.provider.BeanProvider;
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
                    final TxHelper tx = BeanProvider.getContextualReference(TxHelper.class);
                    tx.inTransaction(() -> BeanProvider.getContextualReference(EntityManager.class)
                                                       .createQuery("delete from Quote").executeUpdate());
                }
            }
        };
    }

    @Singleton
    public static class TxHelper {
        public void inTransaction(final Runnable task) {
            task.run();
        }
    }
}
