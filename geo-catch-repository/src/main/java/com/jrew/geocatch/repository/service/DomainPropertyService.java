package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.DomainProperty;

import java.util.List;

/**
 * Represents operations with domain properties.
 */
public interface DomainPropertyService {

    /**
     *
     * @param type
     * @param domain
     * @return
     */
    public List<DomainProperty> loadDomainProperties(long type, String domain);

    /**
     *
     * @param uploadedDomainProperties
     */
    public void processDomainProperties(List<DomainProperty> uploadedDomainProperties);

}
