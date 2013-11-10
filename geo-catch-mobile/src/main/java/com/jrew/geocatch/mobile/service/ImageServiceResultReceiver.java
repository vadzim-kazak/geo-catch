package com.jrew.geocatch.mobile.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 7:17 AM
 */
public class ImageServiceResultReceiver extends ResultReceiver {

    /** **/
    private Receiver receiver;

    public ImageServiceResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }

}
