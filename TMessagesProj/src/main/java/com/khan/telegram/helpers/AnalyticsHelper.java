package com.khan.telegram.helpers;

import android.app.Application;
import android.content.SharedPreferences;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.ActionBar.BaseFragment;

import java.util.HashMap;

public class AnalyticsHelper {
    private static SharedPreferences preferences;

    public static boolean sendBugReport = true;
    public static boolean analyticsDisabled = true;
    public static String userId = null;

    public static void start(Application application) {
        preferences = application.getSharedPreferences("nekoanalytics", Application.MODE_PRIVATE);
        analyticsDisabled = true;
    }

    public static void trackFragmentLifecycle(String lifecycle, BaseFragment fragment) {
    }

    public static void trackEvent(String event, HashMap<String, String> map) {
    }

    public static boolean isSettingsAvailable() {
        return false;
    }

    public static void setAnalyticsDisabled() {
        AnalyticsHelper.analyticsDisabled = true;
    }

    public static void toggleSendBugReport() {
    }
}
