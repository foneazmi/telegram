package org.telegram.messenger;

import android.content.Context;

public class EmuDetector {

    public interface OnEmulatorDetectorListener {
        void onResult(boolean isEmulator);
    }

    public static EmuDetector with(Context pContext) {
        return new EmuDetector(pContext.getApplicationContext());
    }

    private EmuDetector(Context pContext) {
    }

    public EmuDetector setCheckTelephony(boolean telephony) {
        return this;
    }

    public EmuDetector setCheckPackage(boolean chkPackage) {
        return this;
    }

    public boolean detect() {
        return false;
    }
}
