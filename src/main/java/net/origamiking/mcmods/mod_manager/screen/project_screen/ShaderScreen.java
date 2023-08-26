package net.origamiking.mcmods.mod_manager.screen.project_screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public class ShaderScreen extends Screen implements AutoCloseable {
    private final String shaderName;
    private final String author;
    private final String description;
    private final String iconUrl;
    private final Screen parent;
    private OptionListWidget list;
    public ShaderScreen(Screen parent, String name, String author, String description, String iconUrl) {
        super(Text.of(name));
        this.shaderName = name;
        this.author = author;
        this.description = description;
        this.iconUrl = iconUrl;
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 90, this.height / 2 + 160, 200, 20, Text.translatable("gui.back"), button -> close(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        this.list.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.shaderName, this.width / 2, 5, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
