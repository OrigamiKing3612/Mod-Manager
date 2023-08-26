package net.origamiking.mcmods.mod_manager.screen;

import net.minecraft.client.gui.screen.Screen;

public class ModManagerScreen {
    public static Screen createScreen(Screen parent) {
        return new ChooseScreen(parent);
    }
}
