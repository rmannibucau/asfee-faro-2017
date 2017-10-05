package org.talend.kickoff.faro2017.batch.scope;

import javax.batch.api.chunk.listener.ChunkListener;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.control.RequestContextController;
import javax.inject.Inject;
import javax.inject.Named;

// ensures the scope is active for the batch
@Named
@ApplicationScoped
public class ScopeActivator implements ChunkListener {
    @Inject
    private RequestContextController requestContextController;

    @Override
    public void beforeChunk() throws Exception {
        requestContextController.activate();
    }

    @Override
    public void afterChunk() throws Exception {
        requestContextController.deactivate();
    }

    @Override
    public void onError(final Exception e) throws Exception {
        afterChunk();
    }
}
