package com.jrew.geocatch.repository.service.thumbnail;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/16/13
 * Time: 2:04 PM
 *
 * Thumbnail manager implementation.
 * Uses Thumbnailator library.
 * See http://code.google.com/p/thumbnailator/ for more details.
 *
 */
public class CoobirdThumbnailFactoryImpl implements ThumbnailFactory {

    /** Thumbnail image postfix **/
    @Value("#{configProperties['thumbnailFactory.thumbnailPostfix']}")
    public String thumbnailPostfix;

    /** Scale factor of original image **/
    @Value("#{configProperties['thumbnailFactory.scaleFactor']}")
    public double scaleFactor;

    /**
     * Constructor
     */
    public CoobirdThumbnailFactoryImpl() {}

    /**
     * Creates thumbnail image for provided image.
     * Puts thumbnail in the same directory as original image.
     *
     * @param pathToImage full path to original image
     * @return path to created thumbnail image
     * @throws IOException in case of creation error
     */
    @Override
    public String createThumbnail(String pathToImage) throws IOException {

        // Create thumbnail image full path
        String pathToThumbnail = FilenameUtils.getFullPath(pathToImage) +
                FilenameUtils.getBaseName(pathToImage) + thumbnailPostfix + "." +
                FilenameUtils.getExtension(pathToImage);

        File thumbnailFile = new File(pathToThumbnail);
        if (!thumbnailFile.exists()) {
            Thumbnails.of(pathToImage).scale(scaleFactor).toFile(thumbnailFile);
        }

        return pathToThumbnail;
    }
}
