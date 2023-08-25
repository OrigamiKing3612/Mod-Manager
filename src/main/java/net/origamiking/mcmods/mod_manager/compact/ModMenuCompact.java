package net.origamiking.mcmods.mod_manager.compact;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.origamiking.mcmods.mod_manager.screen.ModManagerScreen;

public class ModMenuCompact implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModManagerScreen::createScreen;
    }
}
