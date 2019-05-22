package com.team.hcdh.albumphoto.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SDCard {
    public static void saveSDCardUri(Activity activity, String uri) {

        SharedPreferences preferenceSD;
        preferenceSD = activity.getSharedPreferences("sdUri", Context.MODE_PRIVATE);


        preferenceSD.edit()
                .clear()
                .apply();

        preferenceSD.edit()
                .putString("selectedSD", uri)
                .apply();
    }

    static String getSDCardUri(Activity activity) {

        SharedPreferences preferenceSD = activity.getSharedPreferences("sdUri", Context.MODE_PRIVATE);

        return preferenceSD.getString("selectedSD", "");
    }
}
