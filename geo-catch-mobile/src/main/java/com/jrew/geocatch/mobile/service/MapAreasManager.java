package com.jrew.geocatch.mobile.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Polyline;
import com.androidmapsextensions.PolylineOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.web.model.ClientImagePreview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.03.14
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
public class MapAreasManager {

    /** **/
    public static final int INITIAL_IMAGE_AREA_INDEX = -1;

    /** **/
    private GoogleMap googleMap;

    /** **/
    private double latitudeRange, longitudeRange;

    /** **/
    private double areaSize;

    /** **/
    private double minDegreeRange;

    /** **/
    private double imagesFilteringAreasNumber, imagesFilteringDegreeThreshold;

    /** **/
    private int latitudeAreasNumber, longitudeAreasNumber;

    /** **/
    private LatLng initialPoint;

    /** **/
    private boolean[][] currentMapOccupancyMatrix;

    /** **/
    private LatLngBounds latLngBounds;

//    /** **/
//    private List<Polyline> polylines;

    /** **/
    private boolean isPortraitMode;

    /**
     *
     * @param googleMap
     */
    public MapAreasManager(GoogleMap googleMap, Activity activity) {

        this.googleMap = googleMap;

        imagesFilteringAreasNumber = Double.parseDouble(
                activity.getResources().getString(R.config.imagesFilteringAreasNumber));

        imagesFilteringDegreeThreshold = Double.parseDouble(
                activity.getResources().getString(R.config.imagesFilteringDegreeThreshold));

        Display display = activity.getWindowManager().getDefaultDisplay();
        if (display.getWidth() < display.getHeight()) {
            isPortraitMode = true;
        } else {
            isPortraitMode = false;
        }

        //polylines = new ArrayList<Polyline>();
    }

    /**
     *
     */
    public void init() {

        latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;

        latitudeRange = latLngBounds.northeast.latitude - latLngBounds.southwest.latitude;
        longitudeRange = latLngBounds.northeast.longitude - latLngBounds.southwest.longitude;

        if (isPortraitMode) {
            areaSize = longitudeRange / imagesFilteringAreasNumber;
        } else {
            areaSize = 2 * latitudeRange / imagesFilteringAreasNumber;
        }

        minDegreeRange = Math.min(latitudeRange, longitudeRange);

        latitudeAreasNumber = (int)(latitudeRange / areaSize);
        longitudeAreasNumber = (int)(longitudeRange / areaSize);

        initialPoint = latLngBounds.southwest;

        currentMapOccupancyMatrix = new boolean[latitudeAreasNumber][longitudeAreasNumber];

        //displayAreasBorders();
    }

    /**
     *
     * @return
     */
    public boolean isMapAreasMode() {

        if (minDegreeRange > imagesFilteringDegreeThreshold) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param image
     * @param result
     */
    public void calculateImageAreaIndex(ClientImagePreview image, Point result) {

        result.x = INITIAL_IMAGE_AREA_INDEX;
        result.y = INITIAL_IMAGE_AREA_INDEX;

        for (int i = 0; i < latitudeAreasNumber; i++) {
            for (int j = 0; j < longitudeAreasNumber; j++ ) {

                if (image.getLatitude() >= i * areaSize + initialPoint.latitude &&
                        image.getLatitude() < (i + 1) * areaSize + initialPoint.latitude &&
                        image.getLongitude() >=  j * areaSize + initialPoint.longitude &&
                        image.getLongitude() < (j + 1) * areaSize + initialPoint.longitude) {

                    result.x = i;
                    result.y = j;
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean[][] getCurrentMapOccupancyMatrix() {
        return currentMapOccupancyMatrix;
    }

//    /**
//     *
//     */
//    private void displayAreasBorders() {
//
//        clearPolylines();
//
//        // Horizontal lines
//        for (int i = 0; i < latitudeAreasNumber +1; i++) {
//
//            PolylineOptions polylineOptions = createPolylineOptions();
//
//            LatLng point = new LatLng(i * areaSize + initialPoint.latitude, initialPoint.longitude);
//            polylineOptions.add(point);
//            point = new LatLng(i * areaSize + initialPoint.latitude, latLngBounds.northeast.longitude);
//            polylineOptions.add(point);
//
//            polylines.add(googleMap.addPolyline(polylineOptions));
//        }
//
//        for (int j = 0; j <  longitudeAreasNumber + 1; j++ ) {
//            PolylineOptions polylineOptions = createPolylineOptions();
//
//            LatLng point = new LatLng(initialPoint.latitude, j * areaSize + initialPoint.longitude);
//            polylineOptions.add(point);
//            point = new LatLng(latLngBounds.northeast.latitude, j * areaSize + initialPoint.longitude);
//            polylineOptions.add(point);
//
//            polylines.add(googleMap.addPolyline(polylineOptions));
//        }
//    }
//
//    /**
//     *
//     */
//    private void clearPolylines() {
//        Iterator<Polyline> iterator = polylines.iterator();
//        while(iterator.hasNext()) {
//            Polyline polyline = iterator.next();
//            polyline.remove();
//            iterator.remove();
//        }
//    }
//
//    /**
//     *
//     * @return
//     */
//    private PolylineOptions createPolylineOptions() {
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.color(Color.BLACK);
//        polylineOptions.width(2);
//
//        return polylineOptions;
//    }
}
