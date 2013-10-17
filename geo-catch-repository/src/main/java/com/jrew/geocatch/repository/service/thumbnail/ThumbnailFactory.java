package com.jrew.geocatch.repository.service.thumbnail;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/16/13
 * Time: 2:02 PM
 */
public interface ThumbnailFactory {

    /**
     * Creates thumbnail for provided image
     *
     * @param pathToImage
     * @return
     * @throws IOException
     */
    public String createThumbnail(String pathToImage) throws IOException;
}
