package net.origamiking.mcmods.mod_manager.utils;

import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class TextUtils {
    public static MultilineText createMultilineText(TextRenderer textRenderer, Text text, int maxLines, int width) {
        return MultilineText.create(textRenderer, text, width, maxLines);
    }

    public static MultilineText createMultilineText(TextRenderer textRenderer, List<MultilineText.Line> lines, int maxLines) {
        if (lines.size() > maxLines) {
            lines = lines.subList(0, maxLines);
        }

        return MultilineText.create(textRenderer, lines);
    }

    public static List<Text> wrapText(TextRenderer textRenderer, String text, int maxWidth) {
        TextHandler textHandler = textRenderer.getTextHandler();
        List<StringVisitable> visitableLines = textHandler.wrapLines(text, maxWidth, Style.EMPTY);
        return visitableLines.stream().map(StringVisitable::getString).map(Text::of).toList();
    }
}
