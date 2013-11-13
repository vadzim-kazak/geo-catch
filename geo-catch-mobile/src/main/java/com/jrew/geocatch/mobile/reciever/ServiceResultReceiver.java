package com.jrew.geocatch.mobile.reciever;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class ServiceResultReceiver extends ResultReceiver {

    /**
     *
     */
    public interface Receiver {

        /**
         *
         * @param resultCode
         * @param resultData
         */
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    /** **/
    private Receiver receiver;

    /**
     *
     * @param handler
     */
    public ServiceResultReceiver(Handler handler) {
        super(handler);
    }

    /**
     *
     * @param receiver
     */
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}