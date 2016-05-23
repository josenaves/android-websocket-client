package com.josenaves.android.pb.restful;

import android.content.Context;
import android.content.SharedPreferences;

public final class PreferencesUtils {

    public final static String PREFS_NAME = "pref_server_host";

    public static String getServerHost(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(PREFS_NAME, context.getString(R.string.default_host));
    }

}
