package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.ImageDBManager;
import com.jrew.geocatch.repository.dao.filesystem.FileSystemManager;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.ViewBounds;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    /** **/
    @Value("#{configProperties['imagesFilteringDegreeThreshold']}")
    private double imagesFilteringDegreeThreshold;

    /** **/
    @Value("#{configProperties['imagesFilteringAreasNumber']}")
    private double imagesFilteringAreasNumber;

    public ImageServiceImpl(FileSystemManager fileSystemManager, ImageDBManager imageDBManager) {
        this.fileSystemManager = fileSystemManager;
        this.imageDBManager = imageDBManager;
    }

    @Override
    public List<ClientImagePreview> getImages(SearchCriteria searchCriteria) {

        List<Image> images = imageDBManager.loadImages(searchCriteria);
        images = filterImages(searchCriteria, images);

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

    /**
     *
     * @param searchCriteria
     * @param images
     * @return
     */
    private List<Image> filterImages(SearchCriteria searchCriteria, List<Image> images) {

        if (searchCriteria.isLoadOwnImages()) {
            return images;
        }

        ViewBounds viewBounds = searchCriteria.getViewBounds();
        double latitudeRange = viewBounds.getNorthEastLat() - viewBounds.getSouthWestLat();
        double longitudeRange = viewBounds.getNorthEastLng() - viewBounds.getSouthWestLng();

        double minDegreeRange = Math.min(latitudeRange, longitudeRange);
        if ( minDegreeRange < imagesFilteringDegreeThreshold) {
            return images;
        } else {
            List<Image> filteredResult = new ArrayList<Image>();
            double initialLatitude =  viewBounds.getSouthWestLat();
            double initialLongitude =  viewBounds.getSouthWestLng();
            double areaSize = minDegreeRange / imagesFilteringAreasNumber;
            int latitudeAreasNumber = (int)(latitudeRange / areaSize);
            int longitudeAreasNumber = (int)(longitudeRange / areaSize);
            for (int i = 0; i < latitudeAreasNumber; i++) {
                for (int j = 0; j < longitudeAreasNumber; j++ ) {
                    Image image = getImageForArea((i + 1) * areaSize + initialLatitude,
                                                  (j + 1) * areaSize + initialLongitude,
                                                   i * areaSize + initialLatitude,
                                                   j * areaSize + initialLongitude,
                                                   images);
                    if (image != null) {
                        filteredResult.add(image);
                    }
                }
            }

            return filteredResult;
        }
    }

    /**
     *
     * @param northEastLat
     * @param northEastLng
     * @param southWestLat
     * @param southWestLng
     * @return
     */
    private Image getImageForArea(double northEastLat,
                                  double northEastLng,
                                  double southWestLat,
                                  double southWestLng,
                                  List<Image> images) {

        for (Image image: images) {
            if (image.getLatitude() >= southWestLat && image.getLatitude() < northEastLat &&
                image.getLongitude() >= southWestLng && image.getLongitude() < northEastLng ) {

                return image;
            }
        }

        return null;
    }
}
