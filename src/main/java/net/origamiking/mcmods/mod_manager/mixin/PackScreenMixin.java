package net.origamiking.mcmods.mod_manager.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.screen.browse.DataPacksScreen;
import net.origamiking.mcmods.mod_manager.screen.browse.ResourcePacksScreen;
import net.origamiking.mcmods.mod_manager.utils.PackScreenAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Path;

@Mixin(PackScreen.class)
public class PackScreenMixin extends Screen implements PackScreenAccess {
    @Shadow
    @Final
    private Path file;
    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At(value = "HEAD"), method = "init")
    private void init(CallbackInfo info) {
        int x = this.width / 2 - 200;
        int y = this.height / 4 - 55;
        if (this.modManager$isResourcePackScreen()) {
            if ((Class<?>) getClass() != PackScreen.class) return;

            this.addDrawableChild(ButtonWidget.builder(Text.translatable("mod_manager.gui.plus"), button ->
                            this.client.setScreen(new ResourcePacksScreen(this)))
                    .position(x, y)
                    .size(20, 20)
                    .build());
        } else {
            if ((Class<?>) getClass() != PackScreen.class) return;

            System.out.println(this.file.toString());

            this.addDrawableChild(ButtonWidget.builder(Text.translatable("mod_manager.gui.plus"), button ->
                            this.client.setScreen(new DataPacksScreen(this, this.file.toString())))
                    .position(x, y)
                    .size(20, 20)
                    .build());
        }
    }

    @Override
    public boolean modManager$isResourcePackScreen() {
        return this.file == this.client.getResourcePackDir();
    }
}
