package net.origamiking.mcmods.mod_manager.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

import java.util.List;

public class DrawingUtils {
    public static void drawWrappedString(DrawContext context, String string, int x, int y, int wrapWidth, int lines, int color) {
        MinecraftClient client = MinecraftClient.getInstance();
        while (string != null && string.endsWith("\n")) {
            string = string.substring(0, string.length() - 1);
        }
        List<StringVisitable> strings = client.textRenderer.getTextHandler().wrapLines(Text.literal(string), wrapWidth, Style.EMPTY);
        for (int i = 0; i < strings.size(); i++) {
            if (i >= lines) {
                break;
            }
            StringVisitable renderable = strings.get(i);
            if (i == lines - 1 && strings.size() > lines) {
                renderable = StringVisitable.concat(strings.get(i), StringVisitable.plain("..."));
            }
            OrderedText line = Language.getInstance().reorder(renderable);
            int x1 = x;
            if (client.textRenderer.isRightToLeft()) {
                int width = client.textRenderer.getWidth(line);
                x1 += (float) (wrapWidth - width);
            }
            context.drawText(client.textRenderer, line, x1, y + i * client.textRenderer.fontHeight, color, false);
        }
    }
}
