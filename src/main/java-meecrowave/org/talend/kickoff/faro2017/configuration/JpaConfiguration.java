package org.talend.kickoff.faro2017.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;

@ApplicationScoped
public class JpaConfiguration {
    @Inject
    @PersistenceUnitName("kickoff")
    private EntityManagerFactory entityManagerFactory;

    @Produces
    @RequestScoped
    public EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void closeEntityManager(@Disposes final EntityManager entityManager) {
        entityManager.close();
    }
}
