package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class WebUtil {

    /** **/
    public final static String UTF_8_ENCODING = "UTF8";

    /** **/
    public final static String CONTENT_TYPE_JSON_UTF_8 = "application/json; charset=UTF-8";


    /**
     *
     * @param activity
     * @return
     */
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }

        return false;
    }

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
    public static JSONArray parseHttpResponseAsArray(HttpResponse response) throws Exception {
        return new JSONArray(parseHttpResponse(response));
    }

    /**
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static JSONObject parseHttpResponseAsObject(HttpResponse response) throws Exception {
        return new JSONObject(parseHttpResponse(response));
    }

    /**
     *
     * @param response
     * @return
     * @throws Exception
     */
    public static String parseHttpResponse(HttpResponse response) throws Exception {
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

        return jsonString;
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws org.json.JSONException
     */
    public static ClientImage convertToClientImage(JSONObject jsonObject, Resources resource)
            throws JSONException, ParseException {
        ClientImage clientImage = new ClientImage();

        // Id
        clientImage.setId(jsonObject.getLong("id"));

        clientImage.setDescription(jsonObject.getString("description"));

        // Latitude & Longitude
        clientImage.setLatitude(jsonObject.getDouble("latitude"));
        clientImage.setLongitude(jsonObject.getDouble("longitude"));

        // Path
        clientImage.setPath(jsonObject.getString("path"));

        // Date
        String dateFormatTemplate = resource.getString(R.config.repositoryUploadImagesDateFormat);
        DateFormat dateFormat = new SimpleDateFormat(dateFormatTemplate);
        Date imageDate = dateFormat.parse(jsonObject.getString("date"));
        clientImage.setDate(imageDate);

        // Privacy level
        String privacyLevel = jsonObject.getString("privacyLevel");
        ClientImage.PrivacyLevel imagePrivacyLevel = ClientImage.PrivacyLevel.PRIVATE;
        if (privacyLevel != null &&
                ClientImage.PrivacyLevel.PUBLIC.toString().equalsIgnoreCase(privacyLevel)) {
            imagePrivacyLevel = ClientImage.PrivacyLevel.PUBLIC;
        }
        clientImage.setPrivacyLevel(imagePrivacyLevel);

        // Domain properties
        List<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
        JSONArray domainPropertiesJSON = jsonObject.getJSONArray("domainProperties");
        for (int i = 0; i < domainPropertiesJSON.length(); i++) {
            JSONObject domainPropertyJSON = domainPropertiesJSON.getJSONObject(i);
            DomainProperty domainProperty = convertToDomainProperty(domainPropertyJSON);
            domainProperties.add(domainProperty);
        }
        clientImage.setDomainProperties(domainProperties);

        // likes count
        int likesCount = jsonObject.getInt("likesCount");
        clientImage.setLikesCount(likesCount);
        boolean isLikeSelected = jsonObject.getBoolean("likeSelected");
        clientImage.setLikeSelected(isLikeSelected);

        // dislikes count
        int dislikesCount = jsonObject.getInt("dislikesCount");
        clientImage.setDislikesCount(dislikesCount);
        boolean isDislikeSelected = jsonObject.getBoolean("dislikeSelected");
        clientImage.setDislikeSelected(isDislikeSelected);

        // reports count
        int reportsCount = jsonObject.getInt("reportsCount");
        clientImage.setReportsCount(reportsCount);
        boolean isReportSelected = jsonObject.getBoolean("reportSelected");
        clientImage.setReportSelected(isReportSelected);

        return clientImage;
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws org.json.JSONException
     */
    public static ClientImagePreview convertToClientImagePreview(JSONObject jsonObject) throws JSONException {
        ClientImagePreview imagePreview = new ClientImagePreview();
        imagePreview.setId(jsonObject.getLong("id"));

        imagePreview.setLatitude(jsonObject.getDouble("latitude"));
        imagePreview.setLongitude(jsonObject.getDouble("longitude"));
        imagePreview.setThumbnailPath(jsonObject.getString("thumbnailPath"));

        // Domain properties
        List<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
        JSONArray domainPropertiesJSON = jsonObject.getJSONArray("domainProperties");
        for (int i = 0; i < domainPropertiesJSON.length(); i++) {
            JSONObject domainPropertyJSON = domainPropertiesJSON.getJSONObject(i);
            DomainProperty domainProperty = convertToDomainProperty(domainPropertyJSON);
            domainProperties.add(domainProperty);
        }
        imagePreview.setDomainProperties(domainProperties);

        imagePreview.setOwn(jsonObject.getBoolean("own"));

        return imagePreview;
    }

    /**
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static DomainProperty convertToDomainProperty(JSONObject jsonObject) throws JSONException {

        DomainProperty domainProperty = new DomainProperty();
        domainProperty.setId(jsonObject.getLong("id"));
        domainProperty.setType(jsonObject.getLong("type"));
        domainProperty.setItem(jsonObject.getLong("item"));
        domainProperty.setLocale(jsonObject.getString("locale"));
        domainProperty.setValue(jsonObject.getString("value"));

        return domainProperty;
    }

    /**
     *
     * @param response
     * @throws Exception
     */
    public static void releaseConnection(HttpResponse response) throws Exception {
        if (response != null && response.getEntity() != null) {
            response.getEntity().consumeContent();
        }
    }

}
