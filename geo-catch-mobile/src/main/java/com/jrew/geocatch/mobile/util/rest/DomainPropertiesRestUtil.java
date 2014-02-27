package com.jrew.geocatch.mobile.util.rest;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.util.HttpClientHolder;
import com.jrew.geocatch.mobile.util.WebUtil;
import com.jrew.geocatch.web.model.DomainProperty;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 27.02.14
 * Time: 19:03
 * To change this template use File | Settings | File Templates.
 */
public class DomainPropertiesRestUtil {

    /**
     *
     * @param intent
     * @param resources
     * @return
     * @throws Exception
     */
    public static Bundle loadDomainInfo(Intent intent, Resources resources) throws Exception {

        HttpClient httpClient = HttpClientHolder.getHttpClient();
        HttpContext localContext = new BasicHttpContext();

        Bundle requestBundle = (Bundle) intent.getParcelableExtra(DomainInfoService.REQUEST_KEY);
        String locale = requestBundle.getString(DomainInfoService.LOCALE_KEY);

        StringBuilder loadDomainInfoUrl = new StringBuilder();
        loadDomainInfoUrl.append(resources.getString(R.config.repositoryUrl))
                .append(resources.getString(R.config.repositoryPath))
                .append(resources.getString(R.config.repositoryLoadDomainInfoUri))
                .append(locale);

        HttpGet httpGet = new HttpGet(loadDomainInfoUrl.toString());

        HttpResponse response = null;
        Bundle bundle = new Bundle();

        try {
            response = httpClient.execute(httpGet, localContext);

            // put result to intent bundle
            JSONArray result = WebUtil.parseHttpResponseAsArray(response);
            ArrayList<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
            for (int i = 0; i < result.length(); i++) {
                domainProperties.add(WebUtil.convertToDomainProperty(result.getJSONObject(i)));
            }

            bundle.putSerializable(DomainInfoService.RESULT_KEY, domainProperties);
        } finally {
            WebUtil.releaseConnection(response);
        }

        return bundle;
    }

}
