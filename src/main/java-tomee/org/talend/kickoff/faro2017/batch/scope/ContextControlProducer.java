package org.talend.kickoff.faro2017.batch.scope;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.api.ContextControl;

@ApplicationScoped
public class ContextControlProducer {
    @Produces
    public ContextControl contextControl() {
        return CdiContainerLoader.getCdiContainer().getContextControl();
    }
}
