package org.talend.kickoff.faro2017.batch.scope;

import javax.batch.api.chunk.listener.ChunkListener;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ContextNotActiveException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.cdise.api.ContextControl;

@Named
@ApplicationScoped
public class ScopeActivator implements ChunkListener {
    @Inject
    private ContextControl contextControl;

    @Inject
    private BeanManager beanManager;

    @Override
    public void beforeChunk() throws Exception {
        contextControl.startContext(RequestScoped.class);
    }

    @Override
    public void afterChunk() throws Exception {
        try {
            beanManager.getContext(RequestScoped.class);
            contextControl.stopContext(RequestScoped.class);
        } catch (final ContextNotActiveException cnae) {
            // no-op
        }
    }

    @Override
    public void onError(final Exception e) throws Exception {
        afterChunk();
    }
}
