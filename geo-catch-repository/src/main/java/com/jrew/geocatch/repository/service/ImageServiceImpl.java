package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.ImageDBManager;
import com.jrew.geocatch.repository.dao.filesystem.FileSystemManager;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@link ImageService} interface
 */
public class ImageServiceImpl implements ImageService {

    /** **/
    private final FileSystemManager fileSystemManager;

    /** **/
    private final ImageDBManager imageDBManager;

    /** **/
    @Autowired
    private ImageReviewService imageReviewService;

    /** **/
    @Autowired
    @Qualifier("clientImagePreviewConverter")
    private Converter<Image, ClientImagePreview> clientImagePreviewConverter;

    /** **/
    @Autowired
    @Qualifier("clientImageConverter")
    private Converter<Image, ClientImage> clientImageConverter;

    public ImageServiceImpl(FileSystemManager fileSystemManager, ImageDBManager imageDBManager) {
        this.fileSystemManager = fileSystemManager;
        this.imageDBManager = imageDBManager;
    }

    @Override
    public List<ClientImagePreview> getImages(SearchCriteria searchCriteria) {

        List<Image> images = imageDBManager.loadImages(searchCriteria);

        // Update images paths
        fileSystemManager.updateThumbnailPath(images);

        return convertToClientImages(images, searchCriteria);
    }

    @Override
    public ClientImage getImage(long imageId) {
        Image image = imageDBManager.loadImage(imageId);
        fileSystemManager.updatePath(image);

        ClientImage clientImage = clientImageConverter.convert(image);

        clientImage.setLikesCount(imageReviewService.getLikeReviewsCount(image.getId()));
        clientImage.setDislikesCount(imageReviewService.getDislikeReviewsCount(image.getId()));
        clientImage.setReportsCount(imageReviewService.getReportReviewsCount(image.getId()));

        clientImage.setLikeSelected(imageReviewService.isLikeSelected(image.getId(), image.getDeviceId()));
        clientImage.setDislikeSelected(imageReviewService.isDislikeSelected(image.getId(), image.getDeviceId()));
        clientImage.setReportSelected(imageReviewService.isReportSelected(image.getId(), image.getDeviceId()));

        return clientImage;
    }

    @Override
    public void uploadImage(Image image) throws IOException {

        // Save uploaded image to file system
        fileSystemManager.saveImage(image);

        // Save image to database
        imageDBManager.saveImage(image);
    }

    @Override
    public void deleteImage(long imageId, String deviceId) {

        Image image = imageDBManager.loadImage(imageId);

        // Need to verify that image has the same deviceId as provided before deletion
        if (image != null && image.getDeviceId().equalsIgnoreCase(deviceId)) {

            // 1) Remove image from file system
            try {
                fileSystemManager.deleteImage(image);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            //2) Remove image from db
            imageDBManager.deleteImage(image);
        }
    }

    /**
     *
     *
     * @param images
     * @param searchCriteria
     * @return
     */
    private List<ClientImagePreview> convertToClientImages(List<Image> images, SearchCriteria searchCriteria) {
        List<ClientImagePreview> clientImagePreviews = new ArrayList<ClientImagePreview>(images.size());
        if (images != null) {
            for (Image image : images) {
                ClientImagePreview clientImagePreview = clientImagePreviewConverter.convert(image);
                if (!StringUtils.isEmpty(searchCriteria.getDeviceId()) &&
                        searchCriteria.getDeviceId().equalsIgnoreCase(image.getDeviceId())) {
                    clientImagePreview.setOwn(true);
                }
                clientImagePreviews.add(clientImagePreview);
            }
        }

        return clientImagePreviews;
    }

    @Override
    public void updateImage(Image image) {
        imageDBManager.saveImage(image);
    }
}
