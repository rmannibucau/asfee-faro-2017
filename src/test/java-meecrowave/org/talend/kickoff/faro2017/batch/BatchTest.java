package org.talend.kickoff.faro2017.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.RuleChain.outerRule;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.meecrowave.junit.MonoMeecrowave;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.talend.kickoff.faro2017.service.model.Quote;
import org.talend.kickoff.faro2017.test.InjectorRule;
import org.talend.kickoff.faro2017.test.RequestScopeRule;
import org.talend.kickoff.faro2017.test.VolatileProvisioning;

public class BatchTest {
    @Rule
    public final TestRule injector = outerRule(new MonoMeecrowave.Rule())
            .around(new InjectorRule(this))
            .around(new RequestScopeRule())
            .around(new VolatileProvisioning());

    @Inject
    private EntityManager em;

    @Test
    public void provision() {
        final List<Quote> quotes = em.createQuery("select q from Quote q", Quote.class)
                                     .getResultList();
        assertEquals(10, quotes.size());
        quotes.forEach(q -> {
            assertTrue(q.getPrice() != 0.);
            assertNotNull(q.getSymbol());
        });
    }
}
