package com.jrew.geocatch.mobile.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.jrew.geocatch.mobile.model.Image;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class WebUtil {

    /**
     *
     * @param response
     * @return
     * @throws java.io.IOException
     */
    public static Bitmap getImageFromWeb(HttpResponse response) throws IOException
    {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        if (response != null && response.getStatusLine().getStatusCode() == 200)
        {
            bitmap = BitmapFactory.decodeStream((InputStream) response.getEntity().getContent());
        }

        return bitmap;
    }

    /**
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static JSONArray parseHttpResponse(HttpResponse response) throws Exception {
        String jsonString = "";
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader bReader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = bReader.readLine()) != null) {
                sb.append(line + NL);
            }
            jsonString = sb.toString();
            bReader.close();
        }

        return new JSONArray(jsonString);
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws org.json.JSONException
     */
    public static Image convertToImage(JSONObject jsonObject) throws JSONException {
        Image image = new Image();
        image.setId(Integer.parseInt(jsonObject.getString("id")));

        image.setLatitude(Double.parseDouble(jsonObject.getString("latitude")));
        image.setLongitude(Double.parseDouble(jsonObject.getString("longitude")));
        image.setThumbnailPath(jsonObject.getString("thumbnailPath"));
        image.setPath(jsonObject.getString("path"));

        return image;
    }

}
