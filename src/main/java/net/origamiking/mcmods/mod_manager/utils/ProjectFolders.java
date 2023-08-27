package net.origamiking.mcmods.mod_manager.utils;

public enum ProjectFolders {
    MODS("mods"),
    SHADERS("shaderpacks"),
    RESOURCEPACKS("resourcepacks");

    private final String folder;

    ProjectFolders(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
