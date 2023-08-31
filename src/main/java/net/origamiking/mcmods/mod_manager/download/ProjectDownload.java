package net.origamiking.mcmods.mod_manager.download;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.origamiking.mcmods.mod_manager.ModManager;
import net.origamiking.mcmods.mod_manager.utils.ProjectFolders;
import net.origamiking.mcmods.mod_manager.utils.Utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ProjectDownload {
    //todo handle missing folder
    public static void download(String url, String fileName, String folder) {
        if (url == null || url.isEmpty()) {
            ModManager.LOGGER.error("Invalid URL provided. " + url);
            Utils.showToast("mod_manager.toast.error.line1", "mod_manager.toast.error.line2");
            return;
        }
        String destinationDirectory = FabricLoader.getInstance().getGameDir() + "/" + folder + "/";
        String savePath = destinationDirectory + fileName;

        try {
            downloadAndMoveFile(url, savePath, destinationDirectory);
        } catch (IOException e) {
            Utils.showToast("mod_manager.toast.error.line1", "mod_manager.toast.error.line2");
            ModManager.LOGGER.error(String.valueOf(e));
        }
    }

    public static void downloadDataPack(String url, String fileName, String levelName) {
        if (url == null || url.isEmpty()) {
            ModManager.LOGGER.error("Invalid URL provided. " + url);
            Utils.showToast("mod_manager.toast.error.line1", "mod_manager.toast.error.line2");
            return;
        }
        String destinationDirectory = MinecraftClient.getInstance().getLevelStorage().getSavesDirectory() + "/" + levelName + "/" + ProjectFolders.DATAPACKS.getFolder() + "/";

        String savePath = destinationDirectory + fileName;

        try {
            downloadAndMoveFile(url, savePath, destinationDirectory);
        } catch (IOException e) {
            Utils.showToast("mod_manager.toast.error.line1", "mod_manager.toast.error.line2");
            ModManager.LOGGER.error(String.valueOf(e));
        }
    }

    private static void downloadAndMoveFile(String fileURL, String savePath, String destinationDirectory) throws IOException {
        URL url = new URL(fileURL);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        fileOutputStream.close();
        bufferedInputStream.close();
        inputStream.close();

        Path source = Path.of(savePath);
        Path destination = Path.of(destinationDirectory, source.getFileName().toString());
        Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        Utils.showToast("mod_manager.toast.success.line1", "mod_manager.toast.success.line2");
    }
}
