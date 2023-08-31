package net.origamiking.mcmods.mod_manager.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.EditWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.world.level.storage.LevelStorage;
import net.origamiking.mcmods.mod_manager.screen.browse.DataPacksScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditWorldScreen.class)
public abstract class AddDataPackScreenMixin extends Screen {
    protected AddDataPackScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void init();

    @Shadow
    @Final
    private LevelStorage.Session storageSession;

    @Shadow
    private TextFieldWidget levelNameTextField;

    @Inject(at = @At("HEAD"), method = "init")
    private void init(CallbackInfo info) {
        if ((Class<?>) getClass() != EditWorldScreen.class) return;

        try {
            String worldName = this.storageSession.getDirectoryName();

            this.addDrawableChild(ButtonWidget.builder(Text.literal("⛭"), button ->
                            this.client.setScreen(new DataPacksScreen(this, worldName)))
                    .position(10, 10)
                    .size(20, 20)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}