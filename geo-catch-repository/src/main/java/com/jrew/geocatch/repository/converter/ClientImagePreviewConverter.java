package com.jrew.geocatch.repository.converter;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.web.model.ClientImagePreview;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;

/**
 * Performs conversion {@link Image} entity to
 * {@link com.jrew.geocatch.web.model.ClientImagePreview} entity
 */
@Qualifier("clientImagePreviewConverter")
public class ClientImagePreviewConverter implements Converter<Image, ClientImagePreview> {

    @Override
    public ClientImagePreview convert(Image image) {

        ClientImagePreview clientImagePreview = new ClientImagePreview();
        clientImagePreview.setId(image.getId());
        clientImagePreview.setLatitude(image.getLatitude());
        clientImagePreview.setLongitude(image.getLongitude());
        clientImagePreview.setThumbnailPath(image.getThumbnailPath());

        return clientImagePreview;
    }
}
