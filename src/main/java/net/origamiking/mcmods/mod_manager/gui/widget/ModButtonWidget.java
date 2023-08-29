package net.origamiking.mcmods.mod_manager.gui.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.gui.widget.utils.ModPressableWidget;

import java.util.function.Supplier;

public class ModButtonWidget extends ModPressableWidget {
    protected final ModButtonWidget.PressAction onPress;
    protected final ModButtonWidget.NarrationSupplier narrationSupplier;

//    public static ModButtonWidget.Builder  builder(Text message, ModButtonWidget.PressAction onPress) {
//        return new ModButtonWidget.Builder(message, onPress);
//    }

    public ModButtonWidget(String icon_url, String slug, int x, int y, int width, int height, Text message, ModButtonWidget.PressAction onPress, ModButtonWidget.NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, icon_url, slug);
        this.onPress = onPress;
        this.narrationSupplier = narrationSupplier;
    }

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    @Override
    protected MutableText getNarrationMessage() {
        return this.narrationSupplier.createNarrationMessage(() -> super.getNarrationMessage());
    }

    @Override
    public void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

//    @Environment(value= EnvType.CLIENT)
//    public static class Builder {
//        private final Text message;
//        private final ModButtonWidget.PressAction onPress;
//        @Nullable
//        private Tooltip tooltip;
//        private int x;
//        private int y;
//        private int width = 150;
//        private int height = 20;
//        private ModButtonWidget.NarrationSupplier narrationSupplier = DEFAULT_NARRATION_SUPPLIER;
//
//        public Builder(Text message,  ModButtonWidget.PressAction onPress) {
//            this.message = message;
//            this.onPress = onPress;
//        }
//
//        public  ModButtonWidget.Builder position(int x, int y) {
//            this.x = x;
//            this.y = y;
//            return this;
//        }
//
//        public  ModButtonWidget.Builder width(int width) {
//            this.width = width;
//            return this;
//        }
//
//        public  ModButtonWidget.Builder size(int width, int height) {
//            this.width = width;
//            this.height = height;
//            return this;
//        }
//
//        public  ModButtonWidget.Builder dimensions(int x, int y, int width, int height) {
//            return this.position(x, y).size(width, height);
//        }
//
//        public  ModButtonWidget.Builder tooltip(@Nullable Tooltip tooltip) {
//            this.tooltip = tooltip;
//            return this;
//        }
//
//        public  ModButtonWidget.Builder narrationSupplier( ModButtonWidget.NarrationSupplier narrationSupplier) {
//            this.narrationSupplier = narrationSupplier;
//            return this;
//        }
//
//        public  ModButtonWidget build() {
//             ModButtonWidget  ModButtonWidget = new  ModButtonWidget(this.slug, this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
//             ModButtonWidget.setTooltip(this.tooltip);
//            return  ModButtonWidget;
//        }
//    }

    @Environment(value=EnvType.CLIENT)
    public static interface PressAction {
        public void onPress( ModButtonWidget var1);
    }

    @Environment(value=EnvType.CLIENT)
    public static interface NarrationSupplier {
        public MutableText createNarrationMessage(Supplier<MutableText> var1);
    }
}
