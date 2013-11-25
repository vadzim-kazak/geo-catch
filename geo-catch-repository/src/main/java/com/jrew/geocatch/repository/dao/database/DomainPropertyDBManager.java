package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.DomainProperty;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/25/13
 * Time: 3:08 PM
 */
public interface DomainPropertyDBManager {

    /**
     *
     * @param domainProperty
     */
    public void saveDomainProperty(DomainProperty domainProperty);

}
