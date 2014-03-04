package com.jrew.geocatch.mobile.service.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.jrew.geocatch.mobile.util.SearchUtil;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/25/14
 * Time: 2:29 PM
 */
public class ImageCache {

    /** 4 mb **/
    private static final int CACHE_SIZE = 10 * 1024 * 1024;

    /** **/
    private static ImageCache instance;

    /** **/
    private LruCache<Long, ClientImagePreview> clientImagePreviewCache;

    /** **/
    private LruCache<Long, ClientImage> clientImageCache;

    /** **/
    private LruCache<Long, Bitmap> markerCache;

    /**
     *
     */
    private ImageCache() {

        clientImagePreviewCache = new LruCache<Long, ClientImagePreview>((int)(0.1 * CACHE_SIZE));
        clientImageCache = new LruCache<Long, ClientImage>((int)(0.1 * CACHE_SIZE));
        markerCache = new LruCache<Long, Bitmap>((int)(0.8 * CACHE_SIZE));
    }

    /**
     *
     * @return
     */
    public static ImageCache getInstance() {

        if (instance == null) {
            instance = new ImageCache();
        }

        return instance;
    }

    /**
     *
     * @param clientImagePreview
     */
    public void addClientImagePreview(ClientImagePreview clientImagePreview) {
        if (clientImagePreview != null && getClientImagePreview(clientImagePreview.getId()) == null) {
            clientImagePreviewCache.put(clientImagePreview.getId(), clientImagePreview);
        }
    }

    /**
     *
     * @param clientImagesPreview
     */
    public void addClientImagesPreview(List<ClientImagePreview> clientImagesPreview) {
        if (clientImagesPreview != null && !clientImagesPreview.isEmpty()) {
            for (ClientImagePreview clientImagePreview : clientImagesPreview) {
                addClientImagePreview(clientImagePreview);
            }
        }
    }

    /**
     *
     * @param searchCriteria
     * @return
     */
    public List<ClientImagePreview> getClientImagePreview(SearchCriteria searchCriteria) {

        Map<Long, ClientImagePreview> images = clientImagePreviewCache.snapshot();
        List<ClientImagePreview> cachedImages = new ArrayList<ClientImagePreview>();
        for (Map.Entry<Long, ClientImagePreview> entries : images.entrySet()) {
            ClientImagePreview image = entries.getValue();
            if (SearchUtil.isPassingSearchCriteria(image, searchCriteria)) {
                cachedImages.add(image);
            }
        }

        return  cachedImages;
    }

    /**
     *
     * @return
     */
    public  List<ClientImagePreview> getOwnClientImagePreview() {

        Map<Long, ClientImagePreview> images = clientImagePreviewCache.snapshot();
        List<ClientImagePreview> cachedImages = new ArrayList<ClientImagePreview>();
        for (Map.Entry<Long, ClientImagePreview> entries : images.entrySet()) {
            ClientImagePreview image = entries.getValue();
            if (image.isOwn()) {
                cachedImages.add(image);
            }
        }

        Collections.sort(cachedImages, new Comparator<ClientImagePreview>() {

            // Sort desc by id
            @Override
            public int compare(ClientImagePreview lhs, ClientImagePreview rhs) {

                if (lhs.getId() > rhs.getId()) {
                    return -1;
                } else if (lhs.getId() < rhs.getId()) {
                    return 11;
                }

                return 0;
            }
        });

        return cachedImages;
    }

    /**
     *
     * @param id
     * @return
     */
    public ClientImagePreview getClientImagePreview(Long id) {
        return clientImagePreviewCache.get(id);
    }

    /**
     *
     * @param clientImage
     */
    public void add(ClientImage clientImage) {
        if (clientImage != null && getClientImage(clientImage.getId()) == null) {
            clientImageCache.put(clientImage.getId(), clientImage);
        }
    }

    /**
     *
     * @param clientImage
     */
    public void replace(ClientImage clientImage) {
        if (clientImage != null) {
            clientImageCache.put(clientImage.getId(), clientImage);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ClientImage getClientImage(Long id) {
        return clientImageCache.get(id);
    }

    /**
     *
     * @param id
     */
    public void removeImage(long id) {
        clientImageCache.remove(id);
        clientImagePreviewCache.remove(id);
    }

    /**
     *
     * @param marker
     */
    public void addMarker(long imageId, Bitmap marker) {
        if (marker != null && getMarker(imageId) == null) {
            markerCache.put(imageId, marker);
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public Bitmap getMarker(Long id) {
        return markerCache.get(id);
    }

}
