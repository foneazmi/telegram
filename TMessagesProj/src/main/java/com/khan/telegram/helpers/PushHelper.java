package com.khan.telegram.helpers;

import org.telegram.messenger.FileLog;

public class PushHelper {

    public static void processRemoteMessage(String data) {
        try {
            FileLog.d("ignored remote message: " + data);
        } catch (Exception e) {
            FileLog.e("failed to process remote action", e);
        }
    }
}
