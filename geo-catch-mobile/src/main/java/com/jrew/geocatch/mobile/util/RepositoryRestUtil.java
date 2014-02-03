package com.jrew.geocatch.mobile.util;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.gson.Gson;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.model.UploadImage;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
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

    /** **/
    private static final int CONNECTION_TIMEOUT = 5000;

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadImages(Intent intent, Resources resources) throws Exception{

      //

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
        HttpResponse response = httpClient.execute(httpPost, localContext);

        // Parse response
        JSONArray result = WebUtil.parseHttpResponseAsArray(response);
        ArrayList<ClientImagePreview> images = new ArrayList<ClientImagePreview>();
        for (int i = 0; i < result.length(); i++) {
            images.add(WebUtil.convertToClientImagePreview(result.getJSONObject(i)));
        }

        // put result to intent bundle
        Bundle bundle = new Bundle();
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
        HttpResponse response = httpClient.execute(httpGet, localContext);

        // Parse response
        JSONObject result = WebUtil.parseHttpResponseAsObject(response);
        ClientImage clientImage = WebUtil.convertToClientImage(result, resources);

        // put result to intent bundle
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImageService.RESULT_KEY, clientImage);

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
        Bundle bundle = loadImageFromPath(intent, resources, imagePreview.getThumbnailPath());
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

        HttpClient httpClient = createHttpClient();
        HttpContext localContext = new BasicHttpContext();

        StringBuilder loadImageThumbnailUrl = new StringBuilder();
        loadImageThumbnailUrl.append(resources.getString(R.config.repositoryUrl))
                .append(imagePath);

        // Temporary workaround. Need to be fixed on server side
        String finalUrl = loadImageThumbnailUrl.toString().replace("\\","/");

        HttpGet httpGet = new HttpGet(finalUrl);
        HttpResponse response = httpClient.execute(httpGet, localContext);

        Bundle bundle = new Bundle();

        // put result to intent bundle
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

        StringEntity jsonRequest = new StringEntity(imageToUploadJson, "UTF8");

        HttpPost httpPost = new HttpPost(uploadUrl.toString());
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(jsonRequest);
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
        HttpResponse response = httpClient.execute(httpGet, localContext);

        // put result to intent bundle
        JSONArray result = WebUtil.parseHttpResponseAsArray(response);

        Bundle bundle = new Bundle();

        ArrayList<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
        for (int i = 0; i < result.length(); i++) {
            domainProperties.add(WebUtil.convertToDomainProperty(result.getJSONObject(i)));
        }

        bundle.putSerializable(DomainInfoService.RESULT_KEY, domainProperties);

        return bundle;
    }

    /**
     *
     * @return
     */
    private static HttpClient createHttpClient() {

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);

//        SchemeRegistry registry = new SchemeRegistry();
//        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
//
//        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(params);
    }
}
