package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.DomainProperty;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/25/13
 * Time: 3:11 PM
 */
public class DomainPropertyJPAImpl implements DomainPropertyDBManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void saveDomainProperty(DomainProperty domainProperty) {
        entityManager.persist(domainProperty);
        entityManager.flush();
        entityManager.close();
    }
}
