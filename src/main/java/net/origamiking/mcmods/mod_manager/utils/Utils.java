package net.origamiking.mcmods.mod_manager.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;

import java.util.Objects;

public class Utils {
    public static void showToast(Text line1, Text line2) {
        Objects.requireNonNull(MinecraftClient.getInstance()).getToastManager()
                .add(new SystemToast(SystemToast.Type.TUTORIAL_HINT, line1, line2));
    }
}
