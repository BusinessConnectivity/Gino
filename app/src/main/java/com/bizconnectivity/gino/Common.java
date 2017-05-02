package com.bizconnectivity.gino;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Common {

    public static boolean isNetworkAvailable(Context context) {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void shortToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
