package com.jrew.geocatch.mobile.util;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ViewBounds;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
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
                     .append(resources.getString(R.config.repositoryLoadImagesUri));

        SearchCriteria searchCriteria = new SearchCriteria();

        ViewBounds viewBounds = new ViewBounds(latLngBounds.northeast.latitude,
                                               latLngBounds.northeast.longitude,
                                               latLngBounds.southwest.latitude,
                                               latLngBounds.southwest.longitude);

        searchCriteria.setViewBounds(viewBounds);

        Gson gson = new Gson();
        String json = gson.toJson(searchCriteria);

        HttpPost httpPost = new HttpPost(loadImagesUrl.toString());
        httpPost.setEntity(new ByteArrayEntity(json.getBytes("UTF8")));
        httpPost.setHeader(HTTP.CONTENT_TYPE,"application/json; charset=UTF-8");
        HttpResponse response = httpClient.execute(httpPost, localContext);

        Bundle bundle = new Bundle();

        // put result to intent bundle
        JSONArray result = WebUtil.parseHttpResponse(response);
        ArrayList<ClientImagePreview> images = new ArrayList<ClientImagePreview>();
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
        ClientImagePreview imagePreview = (ClientImagePreview) intent.getSerializableExtra(ImageService.IMAGE_KEY);
        return loadImageFromPath(intent, resources, imagePreview.getThumbnailPath());
    }

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadImage(Intent intent, Resources resources) throws Exception  {
        ClientImage image = (ClientImage) intent.getSerializableExtra(ImageService.IMAGE_KEY);
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

        ClientImage clientImage = (ClientImage) intent.getSerializableExtra(ImageService.IMAGE_KEY);

        StringBuilder loadImageThumbnailUrl = new StringBuilder();
        loadImageThumbnailUrl.append(resources.getString(R.config.repositoryUrl))
                .append(imagePath);

        // Temporary workaround. Need to be fixed on server side
        String finalUrl = loadImageThumbnailUrl.toString().replace("\\","/");

        HttpGet httpGet = new HttpGet(finalUrl);
        HttpResponse response = httpClient.execute(httpGet, localContext);

        Bundle bundle = new Bundle();

        // put result to intent bundle
        bundle.putSerializable(ImageService.IMAGE_KEY, clientImage);
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

        Bundle imageBundle = (Bundle) intent.getParcelableExtra(ImageService.REQUEST_KEY);

        /**
         String url = "http://192.168.0.103:8080/fishing/spring/images/upload";

         request.add(new BasicNameValuePair("userId", "123132"));
         request.add(new BasicNameValuePair("image", imageName));
         request.add(new BasicNameValuePair("description", "Test description"));
         request.add(new BasicNameValuePair("latitude", Float.toString(latitude)));
         request.add(new BasicNameValuePair("longitude", Float.toString(longitude)));
         request.add(new BasicNameValuePair("date", "13023121212128"));
         request.add(new BasicNameValuePair("rating", "130231"));

        **/

//        entity.addPart(ImageUploadKeys.USER_ID,
//                new StringBody("1"));
//        entity.addPart(ImageUploadKeys.DESCRIPTION,
//                new StringBody(imageBundle.getString(ImageUploadKeys.DESCRIPTION)));
//        entity.addPart(ImageUploadKeys.LATITUDE,
//                new StringBody(imageBundle.getString(ImageUploadKeys.LATITUDE)));
//        entity.addPart(ImageUploadKeys.LONGITUDE,
//                new StringBody(imageBundle.getString(ImageUploadKeys.LONGITUDE)));
//        entity.addPart(ImageUploadKeys.DATE,
//                new StringBody(imageBundle.getString(ImageUploadKeys.DATE)));
//        entity.addPart(ImageUploadKeys.RATING,
//                new StringBody("0"));
//        entity.addPart(ImageUploadKeys.FILE,
//                new StringBody(imageBundle.getString(ImageUploadKeys.FILE)));


        entity.addPart(ImageUploadKeys.USER_ID,
                new StringBody("1"));
        entity.addPart(ImageUploadKeys.DESCRIPTION,
                new StringBody(imageBundle.getString(ImageUploadKeys.DESCRIPTION)));
        entity.addPart(ImageUploadKeys.LATITUDE,
                new StringBody(imageBundle.getString(ImageUploadKeys.LATITUDE)));
        entity.addPart(ImageUploadKeys.LONGITUDE,
                new StringBody(imageBundle.getString(ImageUploadKeys.LONGITUDE)));
        entity.addPart(ImageUploadKeys.DATE,
                new StringBody(imageBundle.getString(ImageUploadKeys.DATE)));
        entity.addPart(ImageUploadKeys.RATING,
                new StringBody("0"));
        entity.addPart(ImageUploadKeys.FILE,
                new FileBody(new File(imageBundle.getString(ImageUploadKeys.FILE))));


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
