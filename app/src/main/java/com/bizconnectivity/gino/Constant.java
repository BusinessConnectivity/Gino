package com.bizconnectivity.gino;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constant {

    public static final String SHARED_PREF_KEY = "GINO";
    public static final String SHARED_PREF_IS_SIGNED_IN = "IS_SIGNED_IN";
    public static final String SHARED_PREF_USER_ID = "USER_ID";
    public static final String SHARED_PREF_USER_EMAIL = "USER_EMAIL";
    public static final String SHARED_PREF_USER_SIGN_IN_TYPE = "USER_SIGN_IN_TYPE";
    public static final String SHARED_PREF_OFFER_TAB = "OFFER_TAB";
    public static final String SHARED_PREF_HISTORY_TAB = "HISTORY_TAB";

    public static final String TAB_PULSE = "  PULSE";
    public static final String TAB_OFFER = "  OFFER";
    public static final String TAB_AVAILABLE = "  AVAILABLE";
    public static final String TAB_HISTORY = "  HISTORY";

    public static final String LOGIN_FACEBOOK = "FACEBOOK";
    public static final String LOGIN_GOOGLE = "GOOGLE";
    public static final String LOGIN_EMAIL = "EMAIL";

    public static String MSG_SOMETHING_WENT_WRONG = "Something went wrong.";
    public static String MSG_CANNOT_ACCESS_DEVICE_STORAGE = "Can't access device storage";
    public static String ERR_MSG_NO_INTERNET_CONNECTION = "No Internet Connection";
    public static String ERR_MSG_USER_SIGN_IN = "Please sign in to Gino";

    public static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat format2 = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault());
    public static SimpleDateFormat format3 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
}
