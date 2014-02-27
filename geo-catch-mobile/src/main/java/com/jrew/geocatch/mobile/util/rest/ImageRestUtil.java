package com.jrew.geocatch.mobile.util.rest;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.gson.Gson;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.model.UploadImage;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.ReviewService;
import com.jrew.geocatch.mobile.util.HttpClientHolder;
import com.jrew.geocatch.mobile.util.WebUtil;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.ImageReview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/4/13
 * Time: 3:45 PM
 */
public class ImageRestUtil {

    /** **/
    private static HttpRequestBase httpMethod;

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadImages(Intent intent, Resources resources) throws Exception {

        httpMethod = new HttpPost();

        HttpContext localContext = new BasicHttpContext();
        HttpClient httpClient = HttpClientHolder.getHttpClient();

        StringBuilder loadImagesUrl = new StringBuilder();
        loadImagesUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositorySearchUri));

        SearchCriteria searchCriteria = (SearchCriteria) intent.getSerializableExtra(ImageService.REQUEST_KEY);
        Gson gson = new Gson();
        String searchCriteriaJson = gson.toJson(searchCriteria);

        httpMethod.setURI(URI.create(loadImagesUrl.toString()));
        ((HttpPost) httpMethod).setEntity(new ByteArrayEntity(searchCriteriaJson.getBytes(WebUtil.UTF_8_ENCODING)));
        httpMethod.setHeader(HTTP.CONTENT_TYPE, WebUtil.CONTENT_TYPE_JSON_UTF_8);

        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpMethod, localContext);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                // Parse response
                JSONArray result = WebUtil.parseHttpResponseAsArray(response);
                ArrayList<ClientImagePreview> images = new ArrayList<ClientImagePreview>();
                for (int i = 0; i < result.length(); i++) {
                    images.add(WebUtil.convertToClientImagePreview(result.getJSONObject(i)));
                }

                // put result to intent bundle
                bundle.putSerializable(ImageService.RESULT_KEY, images);
            }

        } finally {
            httpMethod = null;
            WebUtil.releaseConnection(response);
        }

        return bundle;
    }

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadImageData(Intent intent, Resources resources) throws Exception {

        httpMethod = new HttpGet();

        HttpClient httpClient = HttpClientHolder.getHttpClient();
        HttpContext localContext = new BasicHttpContext();

        long imageId = (Long) intent.getSerializableExtra(ImageService.REQUEST_KEY);

        StringBuilder loadImageUrl = new StringBuilder();
        loadImageUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryImagesUri))
                .append("/")
                .append(imageId);

        httpMethod.setURI(URI.create(loadImageUrl.toString()));

        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpMethod, localContext);

            // Parse response
            JSONObject result = WebUtil.parseHttpResponseAsObject(response);
            ClientImage clientImage = WebUtil.convertToClientImage(result, resources);

            // put result to intent bundle

            bundle.putSerializable(ImageService.RESULT_KEY, clientImage);

        } finally {
            httpMethod = null;
            WebUtil.releaseConnection(response);
        }

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

        httpMethod = new HttpPost();

        HttpClient httpClient = HttpClientHolder.getHttpClient();
        HttpContext localContext = new BasicHttpContext();

        StringBuilder uploadUrl = new StringBuilder();
        uploadUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryImagesUri));

        Bundle imageBundle = (Bundle) intent.getParcelableExtra(ImageService.REQUEST_KEY);

        UploadImage imageToUpload = (UploadImage) imageBundle.getSerializable(ImageService.IMAGE_KEY);

        Gson gson = new Gson();
        String imageToUploadJson = gson.toJson(imageToUpload);

        StringEntity jsonRequest = new StringEntity(imageToUploadJson, WebUtil.UTF_8_ENCODING);

        httpMethod.setURI(URI.create(uploadUrl.toString()));
        ((HttpPost) httpMethod).setEntity(jsonRequest);
        httpMethod.setHeader(HTTP.CONTENT_TYPE, WebUtil.CONTENT_TYPE_JSON_UTF_8);

        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpMethod, localContext);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                bundle.putBoolean(ImageService.RESULT_KEY, true);
                if (imageBundle.containsKey(ImageService.POSTPONED_IMAGE_ID_KEY)) {
                    bundle.putLong(ImageService.POSTPONED_IMAGE_ID_KEY, imageBundle.getLong(ImageService.POSTPONED_IMAGE_ID_KEY));
                }
            } else {
                bundle.putBoolean(ImageService.RESULT_KEY, false);
            }

        } finally {
            httpMethod = null;
            WebUtil.releaseConnection(response);
        }

        return bundle;
    }

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle deleteImage(Intent intent, Resources resources) throws Exception {

        HttpClient httpClient = HttpClientHolder.getHttpClient();
        HttpContext localContext = new BasicHttpContext();

        Bundle bundle = intent.getParcelableExtra(ImageService.REQUEST_KEY);

        long imageId = bundle.getLong(ImageService.IMAGE_ID_KEY);
        String deviceId = bundle.getString(ImageService.DEVICE_ID_KEY);

        StringBuilder deleteImageUrl = new StringBuilder();
        deleteImageUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryImagesUri))
                .append("/")
                .append(imageId)
                .append("/")
                .append(deviceId);

        HttpDelete httpDelete = new HttpDelete(deleteImageUrl.toString());
        httpMethod = httpDelete;

        HttpResponse response = null;

        try {
            response = httpClient.execute(httpDelete, localContext);

            bundle.clear();
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                bundle.putBoolean(ImageService.RESULT_KEY, true);
                bundle.putLong(ImageService.IMAGE_ID_KEY, imageId);
            } else {
                bundle.putBoolean(ImageService.RESULT_KEY, false);
            }

        }  finally {
            httpMethod = null;
            WebUtil.releaseConnection(response);
        }

        return bundle;
    }

    /**
     *
     */
    public static void abort() {

        if (httpMethod != null) {
            httpMethod.abort();
            httpMethod = null;
        }
    }
}
