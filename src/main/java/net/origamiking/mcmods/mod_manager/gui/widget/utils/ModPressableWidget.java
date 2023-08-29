package net.origamiking.mcmods.mod_manager.gui.widget.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public abstract class ModPressableWidget extends ClickableWidget {
    private final String icon_url;
    private final String slug;
    private boolean downloadedIcon = false;
    private boolean iconExists;
//    private final Identifier icon;


    protected final MinecraftClient client = MinecraftClient.getInstance();

    public ModPressableWidget(int i, int j, int k, int l, Text text, String icon_url, String slug) {
        super(i, j, k, l, text);
        this.icon_url = icon_url;
//        this.icon = ImageUtils.getIconId(icon_url, slug);
        this.slug = slug;
//        this.iconExists = this.client.getTextureManager().getOrDefault(this.icon, null) != null;
    }

    public abstract void onPress();

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient client = MinecraftClient.getInstance();
        context.setShaderColor(1.0f, 1.0f, 1.0f, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
//        renderIcon(context, 0,0, 25);
        context.drawNineSlicedTexture(WIDGETS_TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());
        context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int i = this.active ? 0xFFFFFF : 0xA0A0A0;
        this.drawMessage(context, client.textRenderer, i | MathHelper.ceil(this.alpha * 255.0f) << 24);
    }

    public void drawMessage(DrawContext context, TextRenderer textRenderer, int color) {
        this.drawScrollableText(context, textRenderer, 2, color);
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isSelected()) {
            i = 2;
        }
        return 46 + i * 20;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onPress();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (KeyCodes.isToggle(keyCode)) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.onPress();
            return true;
        }
        return false;
    }

//    private void downloadIcon() {
//        if (this.downloadedIcon || this.iconExists) return;
//
//        this.downloadedIcon = true;
//
//        ImageUtils.downloadIcon(icon_url, slug).whenCompleteAsync((success, throwable) -> {
//            if (throwable != null) {
//                ModManager.LOGGER.error("Failed to download icon for project with icon_url {}", icon_url, throwable);
//                return;
//            }
//
//            if (success) {
//                this.iconExists = this.client.getTextureManager().getOrDefault(this.icon, null) != null;
//            } else {
//                ModManager.LOGGER.error("Failed to download icon for project with icon_url {}", icon_url);
//            }
//        });
//    }
//    private void renderIcon(DrawContext context, int x, int y, int size) {
//        downloadIcon();
//        if (!this.iconExists) return;
//
//        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
//
//        context.drawTexture(this.icon, x, y, 0.0F, 0.0F, size, size, size, size);
//    }
}
