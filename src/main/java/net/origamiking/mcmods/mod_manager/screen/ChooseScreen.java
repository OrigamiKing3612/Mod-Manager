package net.origamiking.mcmods.mod_manager.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.origamiking.mcmods.mod_manager.screen.browse.ModsScreen;
import net.origamiking.mcmods.mod_manager.screen.browse.ResourcePacksScreen;
import net.origamiking.mcmods.mod_manager.screen.browse.ShaderPacksScreen;


public class ChooseScreen extends Screen {
    private final Screen parent;
    protected TextFieldWidget searchBox;
    private final ModsScreen modsScreen;
    private final ResourcePacksScreen resourcePacksScreen;
    private final ShaderPacksScreen shaderPacksScreen;
    private OptionListWidget list;
    private final int BUTTON_HEIGHT = 20;

    public ChooseScreen(Screen parent) {
        super(Text.translatable("mod_manager.title"));
        this.parent = parent;
        this.modsScreen = new ModsScreen(this);
        this.resourcePacksScreen = new ResourcePacksScreen(this);
        this.shaderPacksScreen = new ShaderPacksScreen(this);
    }

//    @Override
//    public void tick() {
//        this.searchBox.tick();
//    }

    @Override
    protected void init() {
        int x = this.width / 2 + 75;
        int y = 12;
//        this.searchBox = new TextFieldWidget(this.textRenderer,
//                width - 100, 22, 200, 20, this.searchBox, Text.translatable("selectWorld.search"));
//        this.addSelectableChild(this.searchBox);
        this.addDrawableChild(ButtonWidget.builder(Text.of("Mods"), button -> this.client.setScreen(this.modsScreen))
                .position(x - 230, this.height - 125 - y)
                .size(310, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Resource Packs"), button -> this.client.setScreen(this.resourcePacksScreen))
                .position(x - 70, this.height - 100 - y)
                .size(150, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Shader Packs"), button -> this.client.setScreen(this.shaderPacksScreen))
                .position(x - 230, this.height - 100 - y)
                .size(150, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Modrinth"), button -> this.client.setScreen(new ConfirmLinkScreen((bool) -> {
                    if (bool) {
                        Util.getOperatingSystem().open("https://modrinth.com/mod/mod-manager-by-origamiking3612");
                    }
                    this.client.setScreen(this);
                }, "https://modrinth.com/mod/mod-manager-by-origamiking3612", true)))
                .position(x - 70, this.height - 75 - y)
                .size(150, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Github"), button -> this.client.setScreen(new ConfirmLinkScreen((bool) -> {
                    if (bool) {
                        Util.getOperatingSystem().open("https://github.com/OrigamiKing3612/Mod-Manager");
                    }
                    this.client.setScreen(this);
                }, "https://github.com/OrigamiKing3612/Mod-Manager", true)))
                .position(x - 230, this.height - 75 - y)
                .size(150, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.of("Issues"), button -> this.client.setScreen(new ConfirmLinkScreen((bool) -> {
                    if (bool) {
                        Util.getOperatingSystem().open("https://github.com/OrigamiKing3612/Mod-Manager/issues");
                    }
                    this.client.setScreen(this);
                }, "https://github.com/OrigamiKing3612/Mod-Manager/issues", true)))
                .position(x - 230, this.height - 50 - y)
                .size(310, BUTTON_HEIGHT)
                .build());


        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.BACK, button -> close())
                .position(x - 175, this.height - 27)
                .size(200, BUTTON_HEIGHT)
                .build());
        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        this.list.render(context, mouseX, mouseY, delta);
//        this.searchBox.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("mod_manager.title").formatted(Formatting.BOLD, Formatting.AQUA), this.width / 2, 5, 0xffffff);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("mod_manager.description").formatted(Formatting.DARK_AQUA), this.width / 2, 20, 0xffffff);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
