package com.jrew.geocatch.mobile.service;

import com.androidmapsextensions.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.03.14
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class MapTracker {

    /** **/
    private GoogleMap googleMap;

    /** **/
    private float previousMapZoomLevel = -1.0f;

    /** **/
    private boolean isMapZooming = false;

    /** **/
    private boolean isMapFirstTimeViewed = false;

    /** **/
    private LatLngBounds currentMapBounds, previousMapBounds;

    /**
     *
     * @param googleMap
     */
    public MapTracker(GoogleMap googleMap) {
        this.googleMap = googleMap;
        isMapFirstTimeViewed = true;
    }

    /**
     *
     * @param cameraPosition
     */
    public void handleCameraChange(CameraPosition cameraPosition) {

        if(previousMapZoomLevel != cameraPosition.zoom){
            isMapZooming = true;
        } else {
            isMapZooming = false;
        }
        previousMapZoomLevel = cameraPosition.zoom;

        previousMapBounds = currentMapBounds;
        currentMapBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
    }

    /**
     *
     * @return
     */
    public boolean isMapZooming() {
        return isMapZooming;
    }

    /**
     *
     * @return
     */
    public LatLngBounds getPreviousMapBounds() {
        return previousMapBounds;
    }

    /**
     *
     * @return
     */
    public LatLngBounds getCurrentMapBounds() {
        return currentMapBounds;
    }

    /**
     *
     * @return
     */
    public boolean isMapFirstTimeViewed() {
        return isMapFirstTimeViewed;
    }

    /**
     *
     * @param mapFirstTimeViewed
     */
    public void setMapFirstTimeViewed(boolean mapFirstTimeViewed) {
        isMapFirstTimeViewed = mapFirstTimeViewed;
    }
}
