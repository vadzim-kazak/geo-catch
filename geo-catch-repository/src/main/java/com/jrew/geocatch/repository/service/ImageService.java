package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.ClientImage;
import com.jrew.geocatch.repository.model.ClientImagePreview;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.web.model.ImageReview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Represents images manipulation functionality.
 */
public interface ImageService {

    /**
     *
     * @param searchCriteria
     * @return
     */
    public List<ClientImagePreview> getImages(SearchCriteria searchCriteria);

    /**
     *
     * @param imageId
     * @return
     */
    public ClientImage getImage(long imageId);

    /**
     *
     * @param image
     */
    public void updateImage(Image image);

    /**
     *
     *
     * @param image
     * @throws IOException
     */
    public void uploadImage(Image image) throws IOException;

    /**
     *
     * @param imageId
     * @param deviceId
     */
    public void deleteImage(long imageId, String deviceId);

}
