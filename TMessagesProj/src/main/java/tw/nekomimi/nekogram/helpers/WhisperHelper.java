package tw.nekomimi.nekogram.helpers;

import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;

import java.util.function.BiConsumer;

public class WhisperHelper {

    public static boolean useWorkersAi(int account) {
        return false;
    }

    public static void requestWorkersAi(String path, boolean video, BiConsumer<String, Exception> callback) {
        callback.accept(null, new Exception(LocaleController.getString(R.string.CloudflareCredentialsNotSet)));
    }
}
