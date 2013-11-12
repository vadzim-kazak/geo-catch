package com.jrew.geocatch.mobile.service.call;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.jrew.geocatch.mobile.model.Image;
import com.jrew.geocatch.mobile.service.ImageService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 3:41 PM
 */
public class LoadImageThumbnailCall implements RestCall {

    @Override
    public Bundle process(Intent intent) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();

        Image image = (Image) intent.getSerializableExtra(ImageService.IMAGE_KEY);

        StringBuilder loadImageThumbnailUrl = new StringBuilder();
        loadImageThumbnailUrl.append("http://192.168.0.100:8080")
                .append(image.getThumbnailPath());

        // Temporary workaround. Need to be fixed on server side
        String finalUrl = loadImageThumbnailUrl.toString().replace("\\","/");

        HttpGet httpGet = new HttpGet(finalUrl);
        HttpResponse response = httpClient.execute(httpGet, localContext);

        Bundle bundle = new Bundle();

        // put result to intent bundle
        bundle.putSerializable(ImageService.IMAGE_KEY, image);
        bundle.putParcelable(ImageService.RESULT_KEY, getImageFromWeb(response));

        return bundle;
    }


    public Bitmap getImageFromWeb(HttpResponse response) throws IOException
    {
        Bitmap bitmap = null;
        InputStream inputStream = null;
        if (response != null && response.getStatusLine().getStatusCode() == 200)
        {

            bitmap = BitmapFactory.decodeStream((InputStream) response.getEntity().getContent());
        }
        return bitmap;

    }
}
