package nl.wolfpack.wolfpackapp;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleManager {

    // Method use to set the language of the app
    public static void setLocale(Context context, Locale locale) {
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        configuration.setLocale(locale);
        configuration.locale = locale;
        res.updateConfiguration(configuration, res.getDisplayMetrics());
    }

    public static String getLanguage() {
        return Locale.getDefault().getDisplayLanguage();
    }
}