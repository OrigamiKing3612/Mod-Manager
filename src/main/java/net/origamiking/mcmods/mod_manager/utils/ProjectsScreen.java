package net.origamiking.mcmods.mod_manager.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.gui.widget.ReloadButtonWidget;

public abstract class ProjectsScreen extends Screen {
    protected OptionListWidget list;
    protected int currentPage = 0;
    protected int BUTTON_HEIGHT = 20;
    protected TextFieldWidget searchBox;
    protected boolean searching = false;
    protected String SearchString = "";

    protected ProjectsScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        int searchX = this.width - 265;
        int searchY = 35;
        int searchWidth = 150;
        this.searchBox = new TextFieldWidget(this.textRenderer, searchX, searchY, searchWidth, 15, Text.translatable("selectWorld.search"));
        this.addSelectableChild(this.searchBox);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.search"), button -> search())
                .position(searchX + searchWidth + 5, searchY - 2)
                .size(50, BUTTON_HEIGHT)
                .build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.clear"), button -> {
                    searching = false;
                    SearchString = "";
                    search();
                })
                .position(searchX + searchWidth + 5 + 50, searchY - 2)
                .size(50, BUTTON_HEIGHT)
                .build());

        this.addDrawableChild(new ReloadButtonWidget(this.width - 25, this.height - BUTTON_HEIGHT, Text.translatable("gui.reload"), button -> {
            reload();
        }));

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> close())
                .position(this.width - 315, this.height - BUTTON_HEIGHT)
                .size(140, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.next_page"), button -> nextPage())
                .position(this.width - 160, this.height - BUTTON_HEIGHT)
                .size(120, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back_page"), button -> previousPage())
                .position(this.width - 450, this.height - BUTTON_HEIGHT)
                .size(120, BUTTON_HEIGHT)
                .build());

        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
        this.setInitialFocus(this.searchBox);
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
        this.searchBox.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        this.searchBox.tick();
    }

    private void search() {
        searching = !getSearchInput().isEmpty();
        SearchString = getSearchInput();
        reload();
    }

    public String getSearchInput() {
        return searchBox.getText();
    }

    public void reload() {
        this.init(this.client, this.width, this.height);
    }
}
