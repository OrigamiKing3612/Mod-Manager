package net.origamiking.mcmods.mod_manager.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

import java.util.function.Supplier;


public class BrowseScreen extends Screen {
    private final Screen parent;
    protected TextFieldWidget searchBox;
    private final ModsScreen modsScreen;
    private final ResourcePacksScreen resourcePacksScreen;
    private final ShaderPacksScreen shaderPacksScreen;
    private OptionListWidget list;


    public BrowseScreen(Screen parent) {
        super(Text.of("Browse"));
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
        int width = this.width / 2;
//        this.searchBox = new TextFieldWidget(this.textRenderer,
//                width - 100, 22, 200, 20, this.searchBox, Text.translatable("selectWorld.search"));
//        this.addSelectableChild(this.searchBox);
        this.addDrawableChild(new ButtonWidget(width - 230, this.height - 150, 310, 20, Text.of("Mods"), button -> this.client.setScreen(this.modsScreen), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(width - 75, this.height - 100, 150, 20, Text.of("Resource Packs"), button -> this.client.setScreen(this.resourcePacksScreen), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(width - 235, this.height - 100, 150, 20, Text.of("Shader Packs"), button -> this.client.setScreen(this.shaderPacksScreen), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(width - 100, this.height - 27, 200, 20, ScreenTexts.DONE, button -> close(), Supplier::get) {
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
//        this.searchBox.render(context, mouseX, mouseY, delta);
//        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}
