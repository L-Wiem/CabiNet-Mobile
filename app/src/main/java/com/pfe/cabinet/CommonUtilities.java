package com.pfe.cabinet;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	static final String SERVER_URL = "http://YOUR_PROJECT_HOST/api/mobile-user-set-device-id";
    public static final String SENDER_ID = "121845083433";


    public static final String DISPLAY_MESSAGE_ACTION = "com.androidhive.pushnotifications.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";

    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
