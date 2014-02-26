package com.jrew.geocatch.mobile.activity;

import android.app.Application;
import com.google.android.gms.R;
import com.jrew.geocatch.mobile.util.HttpClientHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.download.HttpClientImageDownloader;

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

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();

        System.setProperty("http.keepAlive", "false");

        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .imageDownloader(new HttpClientImageDownloader(getApplicationContext(), HttpClientHolder.getHttpClient()))
                .build();

        ImageLoader.getInstance().init(config);
    }

}
