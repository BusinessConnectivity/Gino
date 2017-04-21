package com.bizconnectivity.gino;

import android.content.Context;
import android.net.ConnectivityManager;

public class Common {

    public static boolean isNetworkAvailable(Context context) {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
