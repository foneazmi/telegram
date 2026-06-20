package com.khan.telegram.helpers;

import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;

public class CloudSettingsHelper {
    private static final class InstanceHolder {
        private static final CloudSettingsHelper instance = new CloudSettingsHelper();
    }

    public static CloudSettingsHelper getInstance() {
        return InstanceHolder.instance;
    }

    public void showDialog(BaseFragment parentFragment) {
        if (parentFragment == null) return;
        var context = parentFragment.getParentActivity();
        if (context == null) return;
        var builder = new org.telegram.ui.ActionBar.AlertDialog.Builder(context, parentFragment.getResourceProvider());
        builder.setTitle(LocaleController.getString(R.string.CloudConfig));
        builder.setMessage("Cloud settings are not available in this version of Nekogram. Please use the official Telegram app for cloud settings.");
        builder.setPositiveButton(LocaleController.getString(R.string.OK), null);
        parentFragment.showDialog(builder.create());
    }

    public void doAutoSync() {
    }
}
