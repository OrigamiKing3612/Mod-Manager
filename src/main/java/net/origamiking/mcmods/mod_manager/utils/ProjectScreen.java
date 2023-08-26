package net.origamiking.mcmods.mod_manager.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Supplier;

public abstract class ProjectScreen extends Screen {
    protected OptionListWidget list;
    protected int currentPage = 0;
    protected ProjectScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 90, this.height / 2 + 160, 200, 20, ScreenTexts.DONE, button -> close(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 130, this.height / 2 + 160, 200, 20, Text.translatable("gui.next_page"), button -> nextPage(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 310, this.height / 2 + 160, 200, 20, Text.translatable("gui.back_page"), button -> previousPage(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });

        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
    }

    protected void nextPage() {
        currentPage++;
        this.init(this.client, this.width, this.height);
    }

    protected void previousPage() {
        currentPage--;
        if (currentPage < 0) {
            currentPage = 0;
        }
        this.init(this.client, this.width, this.height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        this.list.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }
}
