package com.jrew.geocatch.repository.dao.filesystem;

import com.amazonaws.regions.Regions;
import com.jrew.geocatch.repository.model.Region;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 20.02.14
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
public class AmazonS3RegionsConfig {

    /** **/
    private Map<String, Region> regions;

    /**
     *
     * @return
     */
    public Map<String, Region> getRegions() {
        return regions;
    }

    /**
     *
     * @param regions
     */
    public void setRegions(Map<String, Region> regions) {
        this.regions = regions;
    }

    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public Regions getRegionForLocation(double latitude, double longitude) {

       for (Map.Entry<String, Region> entry : regions.entrySet()) {
            Region region = entry.getValue();
           if (region.isLocationInside(latitude, longitude)) {
                return Regions.valueOf(entry.getKey());
           }
       }

       return Regions.DEFAULT_REGION;
   }
}
