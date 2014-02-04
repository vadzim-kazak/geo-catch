package com.jrew.geocatch.mobile.model;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import com.jrew.geocatch.mobile.util.ConversionUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.02.14
 * Time: 11:23
 * To change this template use File | Settings | File Templates.
 */
public class PostponedImage {

    /** **/
    private long id;

    /** **/
    private byte[] imageData;

    /** **/
    private byte[] bitmapData;

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     *
     * @param imageData
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    /**
     *
     * @return
     */
    public byte[] getBitmapData() {
        return bitmapData;
    }

    /**
     *
     * @param bitmapData
     */
    public void setBitmapData(byte[] bitmapData) {
        this.bitmapData = bitmapData;
    }

    /**
     *
     * @param bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        bitmapData = ConversionUtil.marshallBitmap(bitmap);
    }

    /**
     *
     * @return
     */
    public Bitmap getBitmap() {
        return ConversionUtil.unmarshallBitmap(bitmapData);
    }

    /**
     *
     * @param image
     */
    public void setUploadImage(UploadImage image) {
        imageData = ConversionUtil.marshallSerializable(image);
    }

    /**
     *
     * @return
     */
    public UploadImage getUploadImage() {
        return (UploadImage) ConversionUtil.unmarshallSerializable(imageData);
    }
}
