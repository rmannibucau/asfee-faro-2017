package org.talend.kickoff.faro2017.test;

import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.inject.spi.CDI;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RequestScopeRule implements TestRule {
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final CDI<Object> current = CDI.current();
                final RequestContextController requestContextController = current.select(RequestContextController.class).get();
                requestContextController.activate();
                try {
                    base.evaluate();
                } finally {
                    requestContextController.deactivate();
                }
            }
        };
    }
}
