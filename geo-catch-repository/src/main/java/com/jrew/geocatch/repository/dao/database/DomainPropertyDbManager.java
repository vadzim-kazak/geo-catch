package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.DomainProperty;

import java.util.List;

/**
 * Presents domain properties processing functionality
 */
public interface DomainPropertyDBManager {

    /**
     * Saves domain property
     *
     * @param domainProperty
     */
    public void saveDomainProperty(DomainProperty domainProperty);

    /**
     * Loads domain property
     *
     * @param id
     * @return
     */
    public DomainProperty loadDomainProperty(long id);

    /**
     *
     *
     * @param locale
     * @return
     */
    public List<DomainProperty> loadDomainProperties(String locale);

    /**
     * Finds domain property
     *
     *
     * @param type
     * @param value
     * @return
     */
    public DomainProperty findDomainProperty(long type, String value);

}
