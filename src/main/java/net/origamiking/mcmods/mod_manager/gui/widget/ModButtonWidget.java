package net.origamiking.mcmods.mod_manager.gui.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModButtonWidget extends ButtonWidget {

    public ModButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }
}
