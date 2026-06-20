package com.khan.telegram.helpers;

import android.net.Uri;
import android.text.TextUtils;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.LaunchActivity;

import java.util.Locale;
import java.util.function.Consumer;

import com.khan.telegram.settings.BaseNekoSettingsActivity;
import com.khan.telegram.settings.NekoAppearanceSettingsActivity;
import com.khan.telegram.settings.NekoChatSettingsActivity;
import com.khan.telegram.settings.NekoDonateActivity;
import com.khan.telegram.settings.NekoEmojiSettingsActivity;
import com.khan.telegram.settings.NekoExperimentalSettingsActivity;
import com.khan.telegram.settings.NekoGeneralSettingsActivity;
import com.khan.telegram.settings.NekoPasscodeSettingsActivity;

public class SettingsHelper {
    
    public static void processDeepLink(Uri uri, Consumer<BaseFragment> callback, Runnable unknown, Browser.Progress progress) {
        if (uri == null) {
            unknown.run();
            return;
        }
        var segments = uri.getPathSegments();
        if (segments.isEmpty() || segments.size() > 2) {
            unknown.run();
            return;
        }
        BaseNekoSettingsActivity fragment;
        var segment = segments.get(1);
        if (PasscodeHelper.getSettingsKey().equals(segment)) {
            fragment = new NekoPasscodeSettingsActivity();
        } else {
            switch (segment.toLowerCase(Locale.US)) {
                case "appearance":
                case "a":
                    fragment = new NekoAppearanceSettingsActivity();
                    break;
                case "chat":
                case "chats":
                case "c":
                    fragment = new NekoChatSettingsActivity();
                    break;
                case "donate":
                case "d":
                    fragment = new NekoDonateActivity();
                    break;
                case "experimental":
                case "e":
                    fragment = new NekoExperimentalSettingsActivity();
                    break;
                case "emoji":
                    fragment = new NekoEmojiSettingsActivity();
                    break;
                case "general":
                case "g":
                    fragment = new NekoGeneralSettingsActivity();
                    break;
                case "update":
                    LaunchActivity.instance.checkAppUpdate(true, progress);
                    return;
                default:
                    unknown.run();
                    return;
            }
        }
        callback.accept(fragment);
        var row = uri.getQueryParameter("r");
        if (TextUtils.isEmpty(row)) {
            row = uri.getQueryParameter("row");
        }
        if (!TextUtils.isEmpty(row)) {
            var rowFinal = row;
            AndroidUtilities.runOnUIThread(() -> fragment.scrollToRow(rowFinal, unknown));
        }
    }

}
