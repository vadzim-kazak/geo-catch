package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.DomainProperty;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("#{queryProperties['query.domain.property.find']}")
    private String findDomainPropertyQuery;

    @Value("#{queryProperties['query.domain.properties.load']}")
    private String loadDomainPropertiesQuery;

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

        TypedQuery<DomainProperty> query = entityManager.createQuery(findDomainPropertyQuery, DomainProperty.class);
        query.setParameter(1, type);
        query.setParameter(2, value);

        return query.getSingleResult();
    }

    @Override
    public List<DomainProperty> loadDomainProperties(long type, String locale) {

        TypedQuery<DomainProperty> query = entityManager.createQuery(loadDomainPropertiesQuery, DomainProperty.class);
        query.setParameter(1, type);
        query.setParameter(2, locale);

        return query.getResultList();
    }
}
