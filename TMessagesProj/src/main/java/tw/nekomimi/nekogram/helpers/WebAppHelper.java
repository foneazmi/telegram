package tw.nekomimi.nekogram.helpers;

import org.telegram.tgnet.SerializedData;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.bots.WebViewRequestProps;
import org.telegram.ui.web.BotWebViewContainer;

import java.util.function.Consumer;

public class WebAppHelper {
    public static final int INTERNAL_BOT_TLV = 1;

    public static boolean isInternalBot(WebViewRequestProps props) {
        return props.internalType > 0;
    }

    public static String getInternalBotName(WebViewRequestProps props) {
        return "";
    }

    public static void openTLViewer(BaseFragment fragment, org.telegram.tgnet.TLObject object) {
    }

    // serialized message without attach path
    public static class CleanSerializedData extends SerializedData {
        public CleanSerializedData(int size) {
            super(size);
        }
    }

    public static void processBotEvents(BotWebViewContainer.Delegate delegate, String eventData, Consumer<String> eventCallback) {
    }
}
