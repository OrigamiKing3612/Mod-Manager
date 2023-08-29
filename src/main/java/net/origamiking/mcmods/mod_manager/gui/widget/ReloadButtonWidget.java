package net.origamiking.mcmods.mod_manager.gui.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ReloadButtonWidget extends ButtonWidget {
    public static final int BUTTON_SIZE = 20;

    public ReloadButtonWidget(int x, int y, Text message, PressAction onPress) {
        super(x, y, BUTTON_SIZE, BUTTON_SIZE, message, onPress, ButtonWidget.DEFAULT_NARRATION_SUPPLIER);
    }

    protected Text getIconText() {
        return Text.literal("\u21BB"); // Clockwise arrow ↻;
    }

    @Override
    protected void drawScrollableText(DrawContext context, TextRenderer textRenderer, int xOffset, int color) {
        int scale = 2;
        int left = (this.getX() + xOffset) / scale;
        int right = (this.getX() + this.getWidth() - xOffset) / scale;

        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        drawScrollableText(context, textRenderer, this.getIconText(), left, this.getY() / scale, right, (this.getY() + this.getHeight()) / scale, color);
        context.getMatrices().pop();
    }
}
