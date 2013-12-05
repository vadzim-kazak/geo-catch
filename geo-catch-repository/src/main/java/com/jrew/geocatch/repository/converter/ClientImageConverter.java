package com.jrew.geocatch.repository.converter;

import com.jrew.geocatch.repository.model.ClientImage;
import com.jrew.geocatch.repository.model.Image;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;

/**
 * Performs conversion {@link com.jrew.geocatch.repository.model.Image} entity
 * to {@link com.jrew.geocatch.repository.model.ClientImage} entity
 */
@Qualifier("clientImageConverter")
public class ClientImageConverter implements Converter<Image, ClientImage> {

    @Override
    public ClientImage convert(Image image) {

        ClientImage clientImage = new ClientImage();

        clientImage.setId(image.getId());
        clientImage.setDescription(image.getDescription());
        clientImage.setLatitude(image.getLatitude());
        clientImage.setLongitude(image.getLongitude());
        clientImage.setPath(image.getPath());
        clientImage.setDate(image.getDate());
        clientImage.setDomainProperties(image.getDomainProperties());
        clientImage.setRating(image.getRating());
        clientImage.setPrivacyLevel(image.getPrivacyLevel());

        return clientImage;
    }
}
