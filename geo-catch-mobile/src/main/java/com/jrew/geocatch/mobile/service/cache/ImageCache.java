package com.jrew.geocatch.mobile.service.cache;

import android.support.v4.util.LruCache;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jrew.geocatch.mobile.util.SearchCriteriaHolder;
import com.jrew.geocatch.mobile.util.SearchCriteriaUtil;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.ViewBounds;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/25/14
 * Time: 2:29 PM
 */
public class ImageCache {

    /** 4 mb **/
    private static final int CACHE_SIZE = 4 * 1024 * 1024;

    /** **/
    private static ImageCache instance;

    /** **/
    private LruCache<Long, ClientImagePreview> clientImagePreviewCache;

    /** **/
    private LruCache<Long, ClientImage> clientImageCache;

    /**
     *
     */
    private ImageCache() {

        clientImagePreviewCache = new LruCache<Long, ClientImagePreview>(CACHE_SIZE / 2);
        clientImageCache = new LruCache<Long, ClientImage>(CACHE_SIZE / 2);
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
     *
     * @param searchCriteria@return
     */
    public List<ClientImagePreview> getClientImagePreview(SearchCriteria searchCriteria) {

        Map<Long, ClientImagePreview> images = clientImagePreviewCache.snapshot();
        List<ClientImagePreview> cachedImages = new ArrayList<ClientImagePreview>();
        for (Map.Entry<Long, ClientImagePreview> entries : images.entrySet()) {
            ClientImagePreview image = entries.getValue();
            if (SearchCriteriaUtil.isPassingSearchCriteria(image, searchCriteria)) {
                cachedImages.add(image);
            }
        }

        return cachedImages;
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

}
