package org.talend.kickoff.faro2017.batch.component;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Iterator;
import javax.batch.api.BatchProperty;
import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;

import org.talend.kickoff.faro2017.batch.service.CboeService;

@Slf4j
@Named
@Dependent // note: prod impl would handle checkpoints probably
public class SymbolReader extends AbstractItemReader {
    @Inject
    private CboeService cboeService;

    @Inject
    @BatchProperty
    private Integer maxSymbols;

    private Iterator<String> symbols;

    @Override
    public void open(final Serializable serializable) throws Exception {
        symbols = cboeService.findSymbols(maxSymbols == null || maxSymbols == 0 ? Integer.MAX_VALUE : maxSymbols);
    }

    @Override
    public Object readItem() throws Exception {
        return symbols.hasNext() ? symbols.next() : null;
    }
}
