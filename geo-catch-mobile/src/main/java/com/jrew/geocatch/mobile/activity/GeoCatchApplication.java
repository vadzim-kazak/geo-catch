package com.jrew.geocatch.mobile.activity;

import android.app.Application;
import android.os.Environment;
import com.jrew.geocatch.mobile.util.PicassoHolder;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/20/14
 * Time: 3:53 PM
 */
public class GeoCatchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        System.setProperty("http.keepAlive", "false");

        OkHttpClient client = new OkHttpClient();

        // disc cache
        File externalStorageDirectory =  Environment.getExternalStorageDirectory();
        String folderPath = externalStorageDirectory.getAbsolutePath() + File.separator + "geo-catch";
        File cacheFolder = new File(folderPath);
        if (!cacheFolder.exists()) {
            cacheFolder.mkdir();
        }
        HttpResponseCache responseCache = null;
        try {
            responseCache = new HttpResponseCache(cacheFolder, 100000l);
        } catch(IOException exc){

        }

        if (responseCache != null) {
            client.setResponseCache(responseCache);
        }

        // connection pool
        ConnectionPool connectionPool = new ConnectionPool(50, 300000l);
        client.setConnectionPool(connectionPool);

        Picasso picasso = new Picasso.Builder(this)
                               .downloader(new OkHttpDownloader(client))
                               .build();

        picasso.setDebugging(true);

        PicassoHolder.setPicasso(picasso);
    }

}
