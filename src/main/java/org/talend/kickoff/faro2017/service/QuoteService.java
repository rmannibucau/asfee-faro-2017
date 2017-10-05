package org.talend.kickoff.faro2017.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.talend.kickoff.faro2017.service.model.Quote;

@ApplicationScoped
public class QuoteService {
    @Inject
    private EntityManager entityManager;

    public void update(final Quote quote) {
        Quote managed = entityManager.find(Quote.class, quote.getSymbol());
        if (managed == null) {
            entityManager.persist(quote);
        } else { // merge
            managed.setPrice(quote.getPrice());
        }
    }
}
