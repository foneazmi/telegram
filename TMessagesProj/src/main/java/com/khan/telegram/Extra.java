package com.khan.telegram;

import org.telegram.messenger.BuildConfig;

import com.khan.telegram.helpers.UserHelper;

public class Extra {

    public static int APP_ID = BuildConfig.API_ID;
    public static String APP_HASH = BuildConfig.API_HASH;

    public static UserHelper.BotInfo getHelperBot() {
        return null;
    }

    public static boolean isDirectApp() {
        return "release".equals(BuildConfig.BUILD_TYPE) || "debug".equals(BuildConfig.BUILD_TYPE);
    }

    public static boolean isTrustedBot(long id) {
        return false;
    }
}
