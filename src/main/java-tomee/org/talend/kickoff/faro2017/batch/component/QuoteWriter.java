package org.talend.kickoff.faro2017.batch.component;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.talend.kickoff.faro2017.batch.service.Asyncs;
import org.talend.kickoff.faro2017.service.QuoteService;
import org.talend.kickoff.faro2017.service.model.Quote;

@Named
@Slf4j
@Dependent // same note than for the reader: in real life the checkpointing is important
public class QuoteWriter extends AbstractItemWriter {
    @Inject
    private QuoteService quoteService;

    @Inject
    private EntityManager em;

    @Override
    @Transactional
    public void writeItems(final List<Object> list) throws Exception {
        final List<Quote> quotes = Asyncs.unwrap(list);
        quotes.forEach(quote -> quoteService.update(quote));
        em.flush();
    }
}
