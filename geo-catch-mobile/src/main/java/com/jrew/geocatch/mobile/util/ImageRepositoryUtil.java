package com.jrew.geocatch.mobile.util;

import com.google.android.gms.maps.model.LatLngBounds;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/4/13
 * Time: 3:45 PM
 */
public class ImageRepositoryUtil {

    public static void loadImages(LatLngBounds latLngBounds) {

        StringBuilder url =  new StringBuilder();
        url.append("http://192.168.0.103:8080/fishing/spring/images/load/")
           .append(latLngBounds.northeast.latitude).append('/')
           .append(latLngBounds.northeast.longitude).append('/')
           .append(latLngBounds.southwest.latitude).append('/')
           .append(latLngBounds.southwest.longitude);

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpGet httpGet = new HttpGet(url.toString());

        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
