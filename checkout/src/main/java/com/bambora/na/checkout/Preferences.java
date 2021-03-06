/*
 * Copyright (c) 2017 Bambora
 */

package com.bambora.na.checkout;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dlight on 2016-08-19.
 */
public class Preferences {
    public static final String CARD_TYPE = "cardType";
    public static final String TOKEN_REQUEST_TIMEOUT_IN_SECONDS = "tokenRequestTimeoutInSeconds";

    private static volatile Preferences preferences;
    private final SharedPreferences sharedPreferences;

    private Preferences(Context context) {
        sharedPreferences = context.getSharedPreferences("com.bambora.na.checkout", Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(Context context) {
        if (preferences == null) {
            synchronized (Preferences.class) {
                if (preferences == null) {
                    preferences = new Preferences(context);
                }
            }
        }
        return preferences;
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(key, value);
        prefsEditor.apply();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
