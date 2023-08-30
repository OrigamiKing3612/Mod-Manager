package net.origamiking.mcmods.mod_manager;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModManager implements ClientModInitializer {
    public static final String MOD_ID = "mod_manager";
    public static final String VERSION = "0.0.2-1.20.1";
    public static final String MINECRAFT_VERSIONS = "[\"1.20\",\"1.20.1\"]";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    //TODO Icon?
    //TODO better browse
    //TODO Datapacks
    //TODO other ways to get into the ChooseScreen V
        //keybind?
        //resource packs screen
        //datapacks screen
        //shaders screen w/sodium and better sodium menu


    @Override
    public void onInitializeClient() {
        ModManager.LOGGER.info("Starting Mod-Manager " + ModManager.VERSION);
    }
}
