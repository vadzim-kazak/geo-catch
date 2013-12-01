package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.DomainPropertyDBManager;
import com.jrew.geocatch.repository.model.DomainProperty;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Implementation for {@link DomainPropertyService} interface
 */
public class DomainPropertyServiceImpl implements DomainPropertyService {

    private static final long UNCLASSIFIED_ITEM_MARKER = -1l;

    /** **/
    private DomainPropertyDBManager dbManager;

    /**
     *
     * @param dbManager
     */
    public DomainPropertyServiceImpl(DomainPropertyDBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public List<DomainProperty> loadDomainProperties(long type, String locale) {
        return dbManager.loadDomainProperties(type, locale);
    }

    @Override
    public void processDomainProperties(List<DomainProperty> uploadedDomainProperties) {

        for (DomainProperty uploadedDomainProperty : uploadedDomainProperties) {
                processUploadedDomainProperty(uploadedDomainProperty);
        }
    }

    /**
     *
     * @param domainProperty
     */
    private void processUploadedDomainProperty(DomainProperty domainProperty) {

        // Check whether or not current domain property contains id
        long domainPropertyId = domainProperty.getId();
        if (domainPropertyId > 0) {

            /**
             * Domain property contains id. This is mean that
             * user selected populated domain property field.
             * So, it is enough to map image property with current domain property.
             * And nothing is to to be done here.
             */

            /**
            DomainProperty persistedDomainProperty = dbManager.loadDomainProperty(domainPropertyId);
            domainProperty = persistedDomainProperty;
            **/

        } else {

            /**
             * Domain property doesn't contains id
             * Two scenarios are possible here:
             *
             * 1) Auto populated select list wasn't work because of some internet connection issues
             * In this case db could already contains domain property value and we don't need to create
             * one more entity there
             *
             * So, need to try to search domain property value in db
             */

            long domainPropertyType = domainProperty.getType();
            String domainPropertyValue = domainProperty.getValue();

            if (domainPropertyType > 0 && !StringUtils.isEmpty(domainPropertyValue)) {
                DomainProperty persistedDomainProperty =
                        dbManager.findDomainProperty(domainPropertyType, domainPropertyValue);
                if (persistedDomainProperty != null) {
                    domainProperty.setId(persistedDomainProperty.getId());
                    return;
                }
            }

            /**
             * 2) User entered new domain property which is not presented in db
             * In this case we need to set a special CODE to item which mark this Domain
             * Property as unclassified and will be handled later manually
             */

             domainProperty.setItem(UNCLASSIFIED_ITEM_MARKER);
             dbManager.saveDomainProperty(domainProperty);
        }
    }

}
