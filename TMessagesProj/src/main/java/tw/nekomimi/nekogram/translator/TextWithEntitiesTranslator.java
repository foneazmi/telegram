package tw.nekomimi.nekogram.translator;

import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.TranslateAlert2;

import java.util.HashMap;
import java.util.List;

import tw.nekomimi.nekogram.NekoConfig;
import tw.nekomimi.nekogram.translator.html.HTMLKeeper;

public class TextWithEntitiesTranslator implements Translator.ITranslator {

    private static final HashMap<String, TextWithEntitiesTranslator> wrappedTranslators = new HashMap<>();

    public static TextWithEntitiesTranslator of(String type) {
        return wrappedTranslators.computeIfAbsent(type, type1 ->
            new TextWithEntitiesTranslator(TelegramTranslator.getInstance())
        );
    }

    private final Translator.ITranslator translator;

    private TextWithEntitiesTranslator(Translator.ITranslator translator) {
        this.translator = translator;
    }

    @Override
    public Translator.TranslationResult translate(TLRPC.TL_textWithEntities query, String fl, String tl) throws Exception {

        if (NekoConfig.keepFormatting) {
            var html = HTMLKeeper.entitiesToHtml(query.text, query.entities, false);
            var htmlQuery = new TLRPC.TL_textWithEntities();
            htmlQuery.text = html;
            var result = translator.translate(htmlQuery, fl, tl);
            var textAndEntitiesTranslated = HTMLKeeper.htmlToEntities(result.translation().text, query.entities, false);
            return Translator.TranslationResult.of(
                    TranslateAlert2.preprocess(query, textAndEntitiesTranslated),
                    result.sourceLanguage()
            );
        } else {
            var textQuery = new TLRPC.TL_textWithEntities();
            textQuery.text = query.text;
            var result = translator.translate(textQuery, fl, tl);
            return Translator.TranslationResult.of(Translator.textWithEntities(result.translation().text, null), result.sourceLanguage());
        }
    }

    @Override
    public boolean supportLanguage(String language) {
        return translator.supportLanguage(language);
    }

    @Override
    public List<String> getTargetLanguages() {
        return translator.getTargetLanguages();
    }
}
