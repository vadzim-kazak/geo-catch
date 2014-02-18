package com.jrew.geocatch.repository.converter;

import com.jrew.geocatch.web.model.DomainProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 18.02.14
 * Time: 19:33
 * To change this template use File | Settings | File Templates.
 */
@Qualifier("domainPropertiesConverter")
public class DomainPropertiesConverter implements Converter<List<com.jrew.geocatch.repository.model.DomainProperty>, List<DomainProperty>> {

    @Autowired
    @Qualifier("domainPropertyConverter")
    private Converter<com.jrew.geocatch.repository.model.DomainProperty, DomainProperty> domainPropertyConverter;

    @Override
    public List<DomainProperty> convert(List<com.jrew.geocatch.repository.model.DomainProperty> modelDomainProperties) {

        List<DomainProperty> webDomainProperties = new ArrayList<DomainProperty>();

        if (modelDomainProperties != null && modelDomainProperties.size() > 0) {
            for (com.jrew.geocatch.repository.model.DomainProperty modelDomainProperty : modelDomainProperties) {
                webDomainProperties.add(domainPropertyConverter.convert(modelDomainProperty));
            }
        }

        return webDomainProperties;
    }
}
