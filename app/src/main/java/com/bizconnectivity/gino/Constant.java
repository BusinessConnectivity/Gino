package com.bizconnectivity.gino;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constant {

    public static final String SHARED_PREF_KEY = "GINO";
    public static final String SHARED_PREF_IS_SIGNED_IN = "IS_SIGNED_IN";

    public static final String FB_GRAPH_API_URL = "https://graph.facebook.com/";
    public static final String EVENTBRITE_API_URL = "https://www.eventbriteapi.com/v3/";
    public static final String EVENTBRITE_TOKEN = "7QUQUSAKIY5PTGCTE4GM";
    public static final String EVENTBRITE_SORT_BY = "best";
    public static final String EVENTBRITE_LOCATION = "Singapore";
    public static final String EVENTBRITE_START_DATE = "today";
    public static final String EVENTBRITE_EXPAND = "organizer,venue,category";

    public static final String TAB_PULSE = "  PULSE";
    public static final String TAB_OFFER = "  OFFER";
    public static final String TAB_AVAILABLE = "  AVAILABLE";
    public static final String TAB_HISTORY = "  HISTORY";

    public static String MSG_SOMETHING_WENT_WRONG = "Something went wrong.";
    public static String MSG_CANNOT_ACCESS_DEVICE_STORAGE = "Can't access device storage";
    public static String MSG_CANNOT_ACCESS_DEVICE_LOCATION = "Can't access device location";
    public static String ERR_MSG_NO_INTERNET_CONNECTION = "No Internet Connection";
    public static String ERR_MSG_USER_SIGN_IN = "Please Sign In Your Account";

    public static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    public static SimpleDateFormat format2 = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault());
    public static SimpleDateFormat format3 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
}
