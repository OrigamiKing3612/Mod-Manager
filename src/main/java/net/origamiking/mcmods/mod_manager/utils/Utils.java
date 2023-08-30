package net.origamiking.mcmods.mod_manager.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import java.util.Objects;

public class Utils {
    public static void showToast(String line1Key, String line2Key) {
        Objects.requireNonNull(MinecraftClient.getInstance()).getToastManager().add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, Text.translatable(line1Key), Text.translatable(line2Key)));
    }
}
