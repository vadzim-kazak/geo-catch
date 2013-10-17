package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/23/13
 * Time: 9:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageProvider {

    public List<Image> getImages(ViewBounds viewBounds);

    public void uploadImage(Image image) throws IOException;

    public void deleteImage(Image image);
}
