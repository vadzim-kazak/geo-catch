package com.jrew.geocatch.repository.converter;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.web.model.ClientImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;

import java.util.List;

/**
 * Performs conversion {@link com.jrew.geocatch.repository.model.Image} entity
 * to {@link com.jrew.geocatch.web.model.ClientImage} entity
 */
@Qualifier("clientImageConverter")
public class ClientImageConverter implements Converter<Image, ClientImage> {

    @Autowired
    @Qualifier("domainPropertiesConverter")
    Converter<List<DomainProperty>, List<com.jrew.geocatch.web.model.DomainProperty>> domainPropertiesConverter;

    @Override
    public ClientImage convert(Image image) {

        ClientImage clientImage = new ClientImage();

        clientImage.setId(image.getId());
        clientImage.setDescription(image.getDescription());
        clientImage.setLatitude(image.getLatitude());
        clientImage.setLongitude(image.getLongitude());
        clientImage.setPath(image.getPath());
        clientImage.setDate(image.getDate());
        clientImage.setDomainProperties(domainPropertiesConverter.convert(image.getDomainProperties()));
        if (image.getPrivacyLevel() == Image.PrivacyLevel.PRIVATE) {
            clientImage.setPrivacyLevel(ClientImage.PrivacyLevel.PRIVATE);
        } else if (image.getPrivacyLevel() == Image.PrivacyLevel.PUBLIC) {
            clientImage.setPrivacyLevel(ClientImage.PrivacyLevel.PUBLIC);
        } else if (image.getPrivacyLevel() == Image.PrivacyLevel.FRIENDS_ONLY) {
            clientImage.setPrivacyLevel(ClientImage.PrivacyLevel.FRIENDS_ONLY);
        }

        return clientImage;
    }
}
