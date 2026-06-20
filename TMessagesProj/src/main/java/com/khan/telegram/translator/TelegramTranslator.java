package com.khan.telegram.translator;

import com.google.common.util.concurrent.SettableFuture;

import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.TranslateAlert2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TelegramTranslator implements Translator.ITranslator {


    private static final class InstanceHolder {
        private static final TelegramTranslator instance = new TelegramTranslator();
    }

    public static TelegramTranslator getInstance() {
        return InstanceHolder.instance;
    }

    @Override
    public Translator.TranslationResult translate(TLRPC.TL_textWithEntities query, String fl, String tl) throws Exception {
        SettableFuture<TLRPC.TL_textWithEntities> future = SettableFuture.create();
        var req = new TLRPC.TL_messages_translateText();
        req.flags |= 2;
        req.to_lang = tl;
        req.text.add(query);
        ConnectionsManager.getInstance(UserConfig.selectedAccount).sendRequest(req, (res, error) -> {
            if (error != null) {
                future.setException(new IOException(error.text));
                return;
            }
            if (res instanceof TLRPC.TL_messages_translateResult tr && !tr.result.isEmpty()) {
                var text = TranslateAlert2.preprocess(query, tr.result.get(0));
                future.set(text);
            } else {
                future.setException(new IOException("not translated"));
            }
        });
        return Translator.TranslationResult.of(future.get(), null);
    }

    private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList(
        "af", "sq", "am", "ar", "hy", "az", "eu", "be", "bn", "bs", "bg", "ca",
        "ceb", "zh", "zh-cn", "zh-tw", "co", "hr", "cs", "da", "nl", "en", "eo",
        "et", "fi", "fr", "fy", "gl", "ka", "de", "el", "gu", "ht", "ha", "haw",
        "he", "hi", "hmn", "hu", "is", "ig", "id", "ga", "it", "ja", "jv",
        "kn", "kk", "km", "rw", "ko", "ku", "ky", "lo", "la", "lv", "lt", "lb",
        "mk", "mg", "ms", "ml", "mt", "mi", "mr", "mn", "my", "ne", "no", "ny",
        "or", "ps", "fa", "pl", "pt", "pa", "ro", "ru", "sm", "gd", "sr", "st",
        "sn", "sd", "si", "sk", "sl", "so", "es", "su", "sw", "sv", "tl", "tg",
        "ta", "tt", "te", "th", "tr", "tk", "uk", "ur", "ug", "uz", "vi", "cy",
        "xh", "yi", "yo", "zu"
    );

    @Override
    public boolean supportLanguage(String language) {
        if (language == null) return false;
        String lang = language.contains("-") ? language.substring(0, language.indexOf("-")) : language;
        return SUPPORTED_LANGUAGES.contains(lang) || SUPPORTED_LANGUAGES.contains(language);
    }

    @Override
    public List<String> getTargetLanguages() {
        return new ArrayList<>(SUPPORTED_LANGUAGES);
    }
}
