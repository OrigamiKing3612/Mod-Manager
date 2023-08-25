package net.origamiking.mcmods.mod_manager.screen.project;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.ModManager;

public class ModScreen extends Screen implements AutoCloseable {
    private final String modName;
    private final String author;
    private final String description;
    private final String iconUrl;
    private final Screen parent;
    public ModScreen(Screen parent, String modName, String author, String description, String iconUrl) {
        super(Text.of(modName));
        this.modName = modName;
        this.author = author;
        this.description= description;
        this.iconUrl = iconUrl;
        this.parent = parent;
    }

    @Override
    protected void init() {
        ModManager.LOGGER.debug(modName);
        ModManager.LOGGER.debug(author);
        ModManager.LOGGER.debug(description);
        ModManager.LOGGER.debug(iconUrl);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
