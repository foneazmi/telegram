package com.khan.telegram.helpers.remote;

import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConfigHelper {
    private static final List<Long> DEFAULT_VERIFY_LIST = Arrays.asList(
            1349472891L,
            1339737452L,
            1302242053L,
            1715773134L
    );

    public static boolean isChatCat(TLRPC.Chat chat) {
        return DEFAULT_VERIFY_LIST.stream().anyMatch(id -> id == chat.id || id == -2000000000000L - chat.id);
    }

    public static List<Long> getVerify() {
        return DEFAULT_VERIFY_LIST;
    }

    public static List<Crypto> getCryptos() {
        return Collections.emptyList();
    }

    public static List<News> getNewsForProxy() {
        return Collections.emptyList();
    }

    public static List<News> getNewsForSettings() {
        return Collections.emptyList();
    }

    public static TLRPC.TL_pendingSuggestion getNewsSuggestion() {
        return null;
    }

    public static void removeNews(String id) {
    }

    public static class News {
        public String id;
        public String title;
        public BaseRemoteHelper.MessageEntity[] titleEntities;
        public String summary;
        public BaseRemoteHelper.MessageEntity[] summaryEntities;
        public Integer type;
        public String url;
        public String language;
        public Integer mcc;
        public Boolean direct;
        public String source;
        public Integer maxVersion;
        public Integer minVersion;
    }

    public static class Crypto {
        public String currency;
        public String chain;
        public String address;
    }
}
