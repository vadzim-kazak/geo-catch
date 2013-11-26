package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.DomainProperty;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * JPA implementation of DomainPropertyDBManager interface
 */
public class DomainPropertyDBManagerJPAImpl implements DomainPropertyDBManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public void saveDomainProperty(DomainProperty domainProperty) {
        entityManager.persist(domainProperty);
        entityManager.flush();
        entityManager.close();
    }

    @Override
    public DomainProperty loadDomainProperty(long id) {
       return entityManager.find(DomainProperty.class, id);
    }

    @Override
    public DomainProperty findDomainProperty(String type, String value) {

        String searchQuery = "SELECT domainProperty FROM DomainProperty domainProperty WHERE domainProperty.type LIKE ?1 AND " +
                "domainProperty.value LIKE ?2 ";

        TypedQuery<DomainProperty> query = entityManager.createQuery(searchQuery, DomainProperty.class);
        query.setParameter(1, type);
        query.setParameter(2, value);

        return query.getSingleResult();
    }

    @Override
    public List<DomainProperty> loadDomainProperties(long type) {

        String searchQuery = "SELECT domainProperty FROM DomainProperty domainProperty WHERE domainProperty.type LIKE ?1";
        TypedQuery<DomainProperty> query = entityManager.createQuery(searchQuery, DomainProperty.class);
        query.setParameter(1, type);

        return query.getResultList();
    }
}
