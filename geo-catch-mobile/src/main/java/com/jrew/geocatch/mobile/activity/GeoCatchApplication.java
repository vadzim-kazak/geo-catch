package com.jrew.geocatch.mobile.activity;

import android.app.Application;
import com.google.android.gms.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

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

        int connectionTimeOut = getResources().getInteger(R.config.serverConnectionTimeout);

        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .imageDownloader(new BaseImageDownloader(getApplicationContext(), connectionTimeOut * 1000, connectionTimeOut * 1000))
                .build();


        ImageLoader.getInstance().init(config);
    }

}
