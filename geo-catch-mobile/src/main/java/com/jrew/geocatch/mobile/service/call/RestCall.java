package com.jrew.geocatch.mobile.service.call;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 3:04 PM
 */
public interface RestCall {

    /**
     *
     * @return
     */
    public Bundle process(Intent intent, Resources resources) throws Exception;

}
