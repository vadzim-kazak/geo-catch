package com.jrew.geocatch.mobile.util.rest;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.gson.Gson;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.service.ReviewService;
import com.jrew.geocatch.mobile.util.HttpClientHolder;
import com.jrew.geocatch.mobile.util.WebUtil;
import com.jrew.geocatch.web.model.ImageReview;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 27.02.14
 * Time: 19:24
 * To change this template use File | Settings | File Templates.
 */
public class ReviewRestUtil {

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle uploadReview(Intent intent, Resources resources) throws Exception {

        HttpClient httpClient = HttpClientHolder.getHttpClient();
        HttpContext localContext = new BasicHttpContext();

        StringBuilder uploadUrl = new StringBuilder();
        uploadUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryReviewsUri));

        ImageReview imageReview = (ImageReview) intent.getSerializableExtra(ReviewService.REVIEW_KEY);

        Gson gson = new Gson();
        String imageReviewJson = gson.toJson(imageReview);

        StringEntity jsonRequest = new StringEntity(imageReviewJson, WebUtil.UTF_8_ENCODING);

        HttpPost httpPost = new HttpPost(uploadUrl.toString());
        httpPost.setHeader(HTTP.CONTENT_TYPE, WebUtil.CONTENT_TYPE_JSON_UTF_8);
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
            WebUtil.releaseConnection(response);
        }

        return bundle;
    }

}
