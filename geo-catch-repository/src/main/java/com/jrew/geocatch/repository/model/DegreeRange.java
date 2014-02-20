package com.jrew.geocatch.repository.model;

/**
 *  Util class for degree range value representation.
 */
public class DegreeRange {

    /** Start degree range value **/
    private double startDegree;

    /** End degree range value **/
    private double endDegree;

    /**
     */
    public DegreeRange() {
    }

    /**
     *
     * @param startDegree
     * @param endDegree
     */
    public DegreeRange(double startDegree, double endDegree) {
        this.startDegree = startDegree;
        this.endDegree = endDegree;
    }

    /**
     * @return
     */
    public double getStartDegree() {
        return startDegree;
    }

    /**
     * @param startDegree
     */
    public void setStartDegree(double startDegree) {
        this.startDegree = startDegree;
    }

    /**
     * @return
     */
    public double getEndDegree() {
        return endDegree;
    }

    /**
     * @param endDegree
     */
    public void setEndDegree(double endDegree) {
        this.endDegree = endDegree;
    }

    /**
     *
     * @param degree
     * @return
     */
    public boolean isDegreeInside(double degree) {

        if (startDegree >= degree && degree < endDegree ) {
            return true;
        }

        return false;
    }
}
