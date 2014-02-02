package com.jrew.geocatch.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.util.RepositoryRestUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 17.12.13
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
public class DomainInfoService extends IntentService {

    /**  **/
    public static final String RECEIVER_KEY = "receiver";

    /**   **/
    public static final String REQUEST_KEY = "request";

    /**   **/
    public static final String LOCALE_KEY = "locale";

    /** **/
    public static final String RESULT_KEY = "result";

    /**
     *
     */
    public interface ResultStatus {

        /** **/
        public static final int LOADING = 1;

        /** **/
        public static final int LOADING_FINISHED = 2;

        /** **/
        public static final int ERROR = 3;
    }

    /**
     *
     */
    public interface DomainInfoType {

        /** **/
        public static final long FISH = 1;

        /** **/
        public static final long FISHING_TOOL = 2;

        /** **/
        public static final long BAIT = 3;
    }

    /**
     *
     * @param name
     */
    public DomainInfoService(String name) {
        super(name);
    }

    /**
     *
     */
    public DomainInfoService() {
        super(DomainInfoService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER_KEY);
        Resources resources = getResources();
        receiver.send(DomainInfoService.ResultStatus.LOADING, null);
        try {

            receiver.send(DomainInfoService.ResultStatus.LOADING_FINISHED,
                    RepositoryRestUtil.loadDomainInfo(intent, resources));

        } catch (Exception exception) {
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(DomainInfoService.ResultStatus.ERROR, bundle);
        }
    }
}
