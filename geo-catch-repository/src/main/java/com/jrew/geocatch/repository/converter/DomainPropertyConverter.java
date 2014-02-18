package com.jrew.geocatch.repository.converter;

import com.jrew.geocatch.web.model.DomainProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 18.02.14
 * Time: 19:28
 * To change this template use File | Settings | File Templates.
 */
@Qualifier("domainPropertyConverter")
public class DomainPropertyConverter implements Converter<com.jrew.geocatch.repository.model.DomainProperty, DomainProperty> {

    @Override
    public DomainProperty convert(com.jrew.geocatch.repository.model.DomainProperty modelDomainProperty) {

        DomainProperty webDomainProperty = new DomainProperty();
        webDomainProperty.setId(modelDomainProperty.getId());
        webDomainProperty.setValue(modelDomainProperty.getValue());
        webDomainProperty.setItem(modelDomainProperty.getItem());
        webDomainProperty.setType(modelDomainProperty.getType());
        webDomainProperty.setLocale(modelDomainProperty.getLocale());

        return webDomainProperty;
    }
}
