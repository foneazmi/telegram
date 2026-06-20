package com.khan.telegram.helpers.remote;

import android.app.Activity;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.LocaleController;
import org.telegram.tgnet.TLRPC;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.khan.telegram.NekoConfig;

public class BaseRemoteHelper {
    public static final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences("nekoremoteconfig", Activity.MODE_PRIVATE);
    public static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static String getRequestExtra() {
        return " " +
                BuildConfig.VERSION_CODE +
                " " +
                BuildConfig.BUILD_TYPE +
                " " +
                LocaleController.getSystemLocaleStringIso639() +
                " " +
                NekoConfig.userMcc;
    }

    public static String getTextFromInlineResult(TLRPC.BotInlineResult result) {
        return result.send_message != null ? result.send_message.message : result.description;
    }

    public static List<TLRPC.MessageEntity> parseBotAPIEntities(MessageEntity[] botEntities, boolean emojiOnly) {
        if (botEntities == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(botEntities)
                .filter(e -> !emojiOnly || e.customEmojiId != null)
                .map(e -> {
                    var entity = switch (e.type) {
                        case "mention" -> new TLRPC.TL_messageEntityMention();
                        case "hashtag" -> new TLRPC.TL_messageEntityHashtag();
                        case "cashtag" -> new TLRPC.TL_messageEntityCashtag();
                        case "bot_command" -> new TLRPC.TL_messageEntityBotCommand();
                        case "url" -> new TLRPC.TL_messageEntityUrl();
                        case "email" -> new TLRPC.TL_messageEntityEmail();
                        case "phone_number" -> new TLRPC.TL_messageEntityPhone();
                        case "bold" -> new TLRPC.TL_messageEntityBold();
                        case "italic" -> new TLRPC.TL_messageEntityItalic();
                        case "underline" -> new TLRPC.TL_messageEntityUnderline();
                        case "strikethrough" -> new TLRPC.TL_messageEntityStrike();
                        case "spoiler" -> new TLRPC.TL_messageEntitySpoiler();
                        case "text_link" -> new TLRPC.TL_messageEntityTextUrl();
                        case "custom_emoji" -> {
                            var emoji = new TLRPC.TL_messageEntityCustomEmoji();
                            emoji.document_id = e.customEmojiId;
                            yield emoji;
                        }
                        default -> new TLRPC.TL_messageEntityUnknown();
                    };
                    entity.offset = e.offset;
                    entity.length = e.length;
                    entity.url = e.url;
                    return entity;
                })
                .toList();
    }

    public static class MessageEntity {
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("offset")
        @Expose
        public Integer offset;
        @SerializedName("length")
        @Expose
        public Integer length;
        @SerializedName("url")
        @Expose
        public String url;
        @SerializedName("custom_emoji_id")
        @Expose
        public Long customEmojiId;
    }
}
