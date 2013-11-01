package com.jrew.geocatch.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.jrew.geocatch.mobile.R;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 10/15/13
 * Time: 2:57 PM
 */
public class StartActivity extends Activity implements LocationListener {

    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView imageView;
    private TextView textView;
    private float latitude, longitude;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView = (TextView)  findViewById(R.id.location);

        imageView = (ImageView) findViewById(R.id.photoImageView);

        Button button = (Button) findViewById(R.id.takePhotoButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_CODE);
            }
        });

        Button uploadButton = (Button) findViewById(R.id.uploadImageButton);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String imageName = "/mnt/sdcard/image.png";

                try {
                    FileOutputStream out = new FileOutputStream(imageName);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String url = "http://192.168.0.103:8080/fishing/spring/images/upload";

                List<NameValuePair> request = new ArrayList<NameValuePair>();

                request.add(new BasicNameValuePair("userId", "123132"));
                request.add(new BasicNameValuePair("image", imageName));
                request.add(new BasicNameValuePair("description", "Test description"));
                request.add(new BasicNameValuePair("latitude", Float.toString(latitude)));
                request.add(new BasicNameValuePair("longitude", Float.toString(longitude)));
                request.add(new BasicNameValuePair("date", "13023121212128"));
                request.add(new BasicNameValuePair("rating", "130231"));

                post(url, request);
            }
        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);
    }

    public void post(String url, List<NameValuePair> nameValuePairs) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);

        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart("file", new FileBody(new File(nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            response.getEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderEnabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onProviderDisabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        textView.setText(location.getLatitude() + " " + location.getLongitude());
        latitude = (float) location.getLatitude();
        longitude = (float) location.getLongitude();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Bundle extras = intent.getExtras();
        bitmap = (Bitmap) extras.get("data");
        imageView.setImageBitmap(bitmap);
    }
}