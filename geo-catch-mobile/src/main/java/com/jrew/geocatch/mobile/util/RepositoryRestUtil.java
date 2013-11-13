package com.jrew.geocatch.mobile.util;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.model.Image;
import com.jrew.geocatch.mobile.service.ImageService;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/4/13
 * Time: 3:45 PM
 */
public class RepositoryRestUtil {

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadImages(Intent intent, Resources resources) throws Exception{

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


        HttpGet httpGet = new HttpGet(loadImagesUrl.toString());
        HttpResponse response = httpClient.execute(httpGet, localContext);

        Bundle bundle = new Bundle();

        // put result to intent bundle
        JSONArray result = WebUtil.parseHttpResponse(response);
        ArrayList<Image> images = new ArrayList<Image>();
        for (int i = 0; i < result.length(); i++) {
            images.add(WebUtil.convertToImage(result.getJSONObject(i)));
        }

        bundle.putSerializable(ImageService.RESULT_KEY, images);

        return bundle;
    }

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadThumbnail(Intent intent, Resources resources) throws Exception  {
        Image image = (Image) intent.getSerializableExtra(ImageService.IMAGE_KEY);
        return loadImageFromPath(intent, resources, image.getThumbnailPath());
    }

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadImage(Intent intent, Resources resources) throws Exception  {
        Image image = (Image) intent.getSerializableExtra(ImageService.IMAGE_KEY);
        return loadImageFromPath(intent, resources, image.getPath());
    }


    /**
     *
     * @param intent
     * @param resources
     * @param imagePath
     * @return
     * @throws Exception
     */
    public static Bundle loadImageFromPath(Intent intent, Resources resources, String imagePath) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        Image image = (Image) intent.getSerializableExtra(ImageService.IMAGE_KEY);

        StringBuilder loadImageThumbnailUrl = new StringBuilder();
        loadImageThumbnailUrl.append(resources.getString(R.config.repositoryUrl))
                .append(imagePath);

        // Temporary workaround. Need to be fixed on server side
        String finalUrl = loadImageThumbnailUrl.toString().replace("\\","/");

        HttpGet httpGet = new HttpGet(finalUrl);
        HttpResponse response = httpClient.execute(httpGet, localContext);

        Bundle bundle = new Bundle();

        // put result to intent bundle
        bundle.putSerializable(ImageService.IMAGE_KEY, image);
        bundle.putParcelable(ImageService.RESULT_KEY, WebUtil.getImageFromWeb(response));

        return bundle;
    }

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle uploadImage(Intent intent, Resources resources) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        StringBuilder uploadUrl = new StringBuilder();
        uploadUrl.append(resources.getString(R.config.repositoryUrl))
                 .append(resources.getString(R.config.repositoryPath))
                 .append(resources.getString(R.config.repositoryUploadImagesUri));


        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        entity.addPart(ImageUploadKeys.USER_ID,
                new StringBody(intent.getStringExtra(ImageUploadKeys.USER_ID)));
        entity.addPart(ImageUploadKeys.DESCRIPTION,
                new StringBody(intent.getStringExtra(ImageUploadKeys.DESCRIPTION)));
        entity.addPart(ImageUploadKeys.LATITUDE,
                new StringBody(intent.getStringExtra(ImageUploadKeys.LATITUDE)));
        entity.addPart(ImageUploadKeys.LONGITUDE,
                new StringBody(intent.getStringExtra(ImageUploadKeys.LONGITUDE)));
        entity.addPart(ImageUploadKeys.DATE,
                new StringBody(intent.getStringExtra(ImageUploadKeys.DATE)));
        entity.addPart(ImageUploadKeys.FILE,
                new StringBody(intent.getStringExtra(ImageUploadKeys.FILE)));

        HttpPost httpPost = new HttpPost(uploadUrl.toString());
        httpPost.setEntity(entity);

        HttpResponse response = httpClient.execute(httpPost, localContext);

        Bundle bundle = new Bundle();

        int status = response.getStatusLine().getStatusCode();
        if (status == 200) {
            bundle.putBoolean(ImageService.RESULT_KEY, true);
        } else {
            bundle.putBoolean(ImageService.RESULT_KEY, false);
        }

        return bundle;
    }
}
