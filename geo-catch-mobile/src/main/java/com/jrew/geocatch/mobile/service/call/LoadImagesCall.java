package com.jrew.geocatch.mobile.service.call;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.model.Image;
import com.jrew.geocatch.mobile.service.ImageService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 3:06 PM
 */
public class LoadImagesCall implements RestCall {

    /** **/
    private Resources resources;

    /**
     *
     * @param resources
     */
    public LoadImagesCall(Resources resources) {
        this.resources = resources;
    }

    @Override
    public Bundle process(Intent intent) throws Exception{

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        LatLngBounds latLngBounds = (LatLngBounds) intent.getParcelableExtra(ImageService.REQUEST_KEY);

        StringBuilder loadImagesUrl = new StringBuilder();
        loadImagesUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryLoadImagesUri))
                .append(latLngBounds.northeast.latitude).append('/')
                .append(latLngBounds.northeast.longitude).append('/')
                .append(latLngBounds.southwest.latitude).append('/')
                .append(latLngBounds.southwest.longitude);

        Bundle bundle = new Bundle();

        HttpGet httpGet = new HttpGet(loadImagesUrl.toString());


        HttpResponse response = httpClient.execute(httpGet, localContext);

        // put result to intent bundle
        JSONArray result = parseHttpResponse(response);
        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 0; i < result.length(); i++) {
            images.add(convertToImage(result.getJSONObject(i)));
        }

        bundle.putSerializable(ImageService.RESULT_KEY, images);

        return bundle;
    }

    public JSONArray parseHttpResponse(HttpResponse response) throws Exception {
        String jsonString="";
        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader bReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
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

    private Image convertToImage(JSONObject jsonObject) throws JSONException {
        Image image = new Image();
        image.setId(Integer.parseInt(jsonObject.getString("id")));

        image.setLatitude(Double.parseDouble(jsonObject.getString("latitude")));
        image.setLongitude(Double.parseDouble(jsonObject.getString("longitude")));
        image.setThumbnailPath(jsonObject.getString("thumbnailPath"));
        image.setPath(jsonObject.getString("path"));

        return image;
    }
}

