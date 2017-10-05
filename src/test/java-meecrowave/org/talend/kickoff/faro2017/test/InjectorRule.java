package org.talend.kickoff.faro2017.test;

import lombok.AllArgsConstructor;

import org.apache.meecrowave.testing.Injector;
import org.junit.rules.ExternalResource;

@AllArgsConstructor
public class InjectorRule extends ExternalResource {
    private final Object test;

    @Override
    protected void before() throws Throwable {
        Injector.inject(test);
    }
}
