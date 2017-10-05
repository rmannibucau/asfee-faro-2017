package org.talend.kickoff.faro2017.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.rules.RuleChain.outerRule;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.meecrowave.junit.MonoMeecrowave;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.talend.kickoff.faro2017.service.model.Quote;
import org.talend.kickoff.faro2017.test.RequestScopeRule;
import org.talend.kickoff.faro2017.test.VolatileProvisioning;

@RunWith(MonoMeecrowave.Runner.class)
public class BatchTest {
    @Inject
    private EntityManager em;

    @ClassRule
    public static final TestRule PIPELINE = outerRule(new MonoMeecrowave.Rule())
            .around(new RequestScopeRule())
            .around(new VolatileProvisioning());

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
