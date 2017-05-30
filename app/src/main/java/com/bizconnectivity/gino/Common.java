package com.bizconnectivity.gino;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common{

    // Check Internet Connection
    public static boolean isNetworkAvailable(Context context) {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    // Snack Bar
    public static void snackBar(View view, String message) {

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    // Short Toast
    public static void shortToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Validate Email Input
    public static boolean isEmailValid(String email) {

        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    // Check Web Service Property
    public static boolean isValidateProperty(SoapObject soapObject, String propertyName) {
        return !soapObject.getPropertySafelyAsString(propertyName).equals("anyType{}") && !soapObject.getPropertySafelyAsString(propertyName).isEmpty();
    }

    // Return Web Service Data
    public static String dataReturn(SoapObject soapObject, String propertyName) {
        return soapObject.getPropertySafelyAsString(propertyName);
    }
}
