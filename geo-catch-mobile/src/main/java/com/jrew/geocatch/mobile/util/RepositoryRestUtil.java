package com.jrew.geocatch.mobile.util;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.gson.Gson;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.model.UploadImage;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.ReviewService;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.ImageReview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/4/13
 * Time: 3:45 PM
 */
public class RepositoryRestUtil {

    /** **/
    private final static String UTF_8_ENCODING = "UTF8";

    /** **/
    private final static String CONTENT_TYPE_JSON_UTF_8 = "application/json; charset=UTF-8";

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadImages(Intent intent, Resources resources) throws Exception {

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        StringBuilder loadImagesUrl = new StringBuilder();
        loadImagesUrl.append(resources.getString(R.config.repositoryUrl))
                     .append(resources.getString(R.config.repositoryPath))
                     .append(resources.getString(R.config.repositorySearchUri));

        SearchCriteria searchCriteria = (SearchCriteria) intent.getSerializableExtra(ImageService.REQUEST_KEY);
        Gson gson = new Gson();
        String searchCriteriaJson = gson.toJson(searchCriteria);

        HttpPost httpPost = new HttpPost(loadImagesUrl.toString());
        httpPost.setEntity(new ByteArrayEntity(searchCriteriaJson.getBytes(UTF_8_ENCODING)));
        httpPost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_UTF_8);

        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpPost, localContext);

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
            releaseConnection(response);
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

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        long imageId = (Long) intent.getSerializableExtra(ImageService.REQUEST_KEY);

        StringBuilder loadImageUrl = new StringBuilder();
        loadImageUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryImagesUri))
                .append("/")
                .append(imageId);

        HttpGet httpGet = new HttpGet(loadImageUrl.toString());

        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpGet, localContext);

            // Parse response
            JSONObject result = WebUtil.parseHttpResponseAsObject(response);
            ClientImage clientImage = WebUtil.convertToClientImage(result, resources);

            // put result to intent bundle

            bundle.putSerializable(ImageService.RESULT_KEY, clientImage);

        } finally {
            releaseConnection(response);
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
    public static Bundle loadThumbnail(Intent intent, Resources resources) throws Exception  {
        ClientImagePreview imagePreview = (ClientImagePreview) intent.getSerializableExtra(ImageService.IMAGE_KEY);
        Bundle bundle = loadImageFromPath(resources, imagePreview.getThumbnailPath());
        ClientImagePreview clientImagePreview = (ClientImagePreview) intent.getSerializableExtra(ImageService.IMAGE_KEY);
        bundle.putSerializable(ImageService.IMAGE_KEY, clientImagePreview);
        return bundle;
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
        return loadImageFromPath(resources, image.getPath());
    }


    /**
     *
     * @param resources
     * @param imagePath
     * @return
     * @throws Exception
     */
    public static Bundle loadImageFromPath(Resources resources, String imagePath) throws Exception {

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        HttpGet httpGet = new HttpGet(imagePath);
        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpGet, localContext);
            // put result to intent bundle
            bundle.putParcelable(ImageService.RESULT_KEY, WebUtil.getImageFromWeb(response));

        } finally {
            releaseConnection(response);
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

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        StringBuilder uploadUrl = new StringBuilder();
        uploadUrl.append(resources.getString(R.config.repositoryUrl))
                 .append(resources.getString(R.config.repositoryPath))
                 .append(resources.getString(R.config.repositoryImagesUri));

        Bundle imageBundle = (Bundle) intent.getParcelableExtra(ImageService.REQUEST_KEY);

        UploadImage imageToUpload = (UploadImage) imageBundle.getSerializable(ImageService.IMAGE_KEY);

        Gson gson = new Gson();
        String imageToUploadJson = gson.toJson(imageToUpload);

        StringEntity jsonRequest = new StringEntity(imageToUploadJson, UTF_8_ENCODING);

        HttpPost httpPost = new HttpPost(uploadUrl.toString());
        httpPost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_UTF_8);
        httpPost.setEntity(jsonRequest);
       HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpPost, localContext);

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
            releaseConnection(response);
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
    public static Bundle loadDomainInfo(Intent intent, Resources resources) throws Exception {

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        Bundle requestBundle = (Bundle) intent.getParcelableExtra(DomainInfoService.REQUEST_KEY);
        String locale = requestBundle.getString(DomainInfoService.LOCALE_KEY);

        StringBuilder loadDomainInfoUrl = new StringBuilder();
        loadDomainInfoUrl.append(resources.getString(R.config.repositoryUrl))
                         .append(resources.getString(R.config.repositoryPath))
                         .append(resources.getString(R.config.repositoryLoadDomainInfoUri))
                         .append(locale);

        HttpGet httpGet = new HttpGet(loadDomainInfoUrl.toString());
        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpGet, localContext);

            // put result to intent bundle
            JSONArray result = WebUtil.parseHttpResponseAsArray(response);
            ArrayList<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
            for (int i = 0; i < result.length(); i++) {
                domainProperties.add(WebUtil.convertToDomainProperty(result.getJSONObject(i)));
            }

            bundle.putSerializable(DomainInfoService.RESULT_KEY, domainProperties);
        } finally {
            releaseConnection(response);
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

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        Bundle bundle = (Bundle) intent.getParcelableExtra(ImageService.REQUEST_KEY);

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
        HttpResponse response = null;

        try {
            response = httpClient.execute(httpDelete, localContext);

            bundle.clear();
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                bundle.putBoolean(ImageService.RESULT_KEY, true);
            } else {
                bundle.putBoolean(ImageService.RESULT_KEY, false);
            }

        }  finally {
            releaseConnection(response);
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
    public static Bundle uploadReview(Intent intent, Resources resources) throws Exception {

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        StringBuilder uploadUrl = new StringBuilder();
        uploadUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryReviewsUri));

        ImageReview imageReview = (ImageReview) intent.getSerializableExtra(ReviewService.REVIEW_KEY);

        Gson gson = new Gson();
        String imageReviewJson = gson.toJson(imageReview);

        StringEntity jsonRequest = new StringEntity(imageReviewJson, UTF_8_ENCODING);

        HttpPost httpPost = new HttpPost(uploadUrl.toString());
        httpPost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON_UTF_8);
        httpPost.setEntity(jsonRequest);
        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpPost, localContext);

            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                bundle.putBoolean(ReviewService.RESULT_KEY, true);
                bundle.putSerializable(ReviewService.REVIEW_KEY, imageReview);
            } else {
                bundle.putBoolean(ReviewService.RESULT_KEY, false);
            }

        } finally {
            releaseConnection(response);
        }

        return bundle;
    }

    /**
     *
     * @return
     */
    private static HttpClient createHttpClient() {

        return HttpClientHolder.getHttpClient();
    }

    /**
     *
     * @param response
     * @throws Exception
     */
    private static void releaseConnection(HttpResponse response) throws Exception {
        if (response != null && response.getEntity() != null) {
            response.getEntity().consumeContent();
        }
    }
}
