package com.jrew.geocatch.mobile.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.02.14
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class ConversionUtil {

    /**
     *
     * @param bitmap
     * @return
     */
    public static byte[] marshallBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        return stream.toByteArray();
    }

    /**
     *
     * @param data
     * @return
     */
    public static Bitmap unmarshallBitmap(byte[] data) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(mutableBitmap);
        return mutableBitmap;
    }

    /**
     *
     * @param object
     * @return
     */
    public static byte[] marshallSerializable(Serializable object) {

        byte[] result = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            result = bos.toByteArray();
        } catch (IOException ex ) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
            }try {
                bos.close();
            } catch (IOException ex) {}
        }

        return result;
    }

    /**
     *
     * @param data
     * @return
     */
    public static Object  unmarshallSerializable(byte[] data) {

        Object result = null;

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        } catch (IOException ex) {
        } catch (ClassNotFoundException ex) {
        }finally {
            try {
                bis.close();
            } catch (IOException ex) {
            } try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {}
        }

        return result;
    }
}
