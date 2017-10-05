package org.talend.kickoff.faro2017.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class JpaConfiguration {
    @Produces // container does the scoping for us in EE
    @PersistenceContext
    private EntityManager em;
}
