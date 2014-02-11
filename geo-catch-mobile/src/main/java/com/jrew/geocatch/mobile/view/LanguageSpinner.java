package com.jrew.geocatch.mobile.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.LocalizationUtil;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 07.02.14
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public class LanguageSpinner extends Spinner implements AdapterView.OnItemSelectedListener {

    /** **/
    private String[] supportedLocales;

    /** **/
    private String[] supportedLanguages;

    /** **/
    private Activity activity;

    /**
     *
     * @param context
     */
    public LanguageSpinner(Context context) {
        super(context);
        init();
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public LanguageSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     *
     */
    private void init() {

        supportedLocales =  getResources().getStringArray(R.array.locales_array);
        supportedLanguages = getResources().getStringArray(R.array.languages_array);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, supportedLanguages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setAdapter(adapter);

        // set current locale to spinner
        String currentLocale = Locale.getDefault().getLanguage();
        int appLocaleIndex = -1;
        for (int i = 0; i < supportedLocales.length; i++) {
            String supportedLocale = supportedLocales[i];
            if (supportedLocale.equalsIgnoreCase(currentLocale)) {
                appLocaleIndex = i;
                break;
            }
        }
        if (appLocaleIndex != -1) {
            setSelection(appLocaleIndex);
        }

        setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String appLocale = Locale.getDefault().getLanguage();
        CharSequence[] supportedLocales = getResources().getTextArray(R.array.locales_array);
        String newLocale = supportedLocales[i].toString();
        if (!newLocale.equalsIgnoreCase(appLocale)) {
            LocalizationUtil.switchToLocale(newLocale, activity, true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {}

    /**
     *
     * @param activity
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
