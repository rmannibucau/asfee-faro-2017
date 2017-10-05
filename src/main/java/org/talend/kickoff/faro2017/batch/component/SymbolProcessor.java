package org.talend.kickoff.faro2017.batch.component;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.talend.kickoff.faro2017.batch.service.YahooService;

@Named
@Dependent
public class SymbolProcessor implements ItemProcessor {
    @Inject
    private YahooService yahoo;

    @Override
    public Object processItem(final Object symbol) throws Exception {
        return yahoo.find(String.class.cast(symbol));
    }
}
