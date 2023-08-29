package net.origamiking.mcmods.mod_manager.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.utils.ProjectsScreen;
import net.origamiking.mcmods.mod_manager.utils.TextUtils;

import java.util.List;

public class ProjectSelectionListWidget<S extends ProjectsScreen> extends EntryListWidget<ProjectSelectionListWidget.AbstractEntry> {
    private final S screen;
    private final String projectName;

    public ProjectSelectionListWidget(MinecraftClient client, S screen, int width, int height, int top, int bottom, String projectName) {
        super(client, width, height, top, bottom, 32);
        this.screen = screen;
        this.projectName = projectName;

//        this.client.setScreen(new ModScreen(this, slug, modName));

//        this.children().addAll(getPackEntries());
    }

    //    private List<AbstractEntry> getPackEntries() {
//        List<AbstractEntry> entries = new ArrayList<>();
//
//        return entries;
//    }
    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public static abstract class AbstractEntry extends EntryListWidget.Entry<AbstractEntry> {
        protected final MinecraftClient client;

        protected AbstractEntry(MinecraftClient client) {
            this.client = client;
        }

        protected final List<Text> wrapEscapedText(String text, int maxWidth) {
            return TextUtils.wrapText(this.client.textRenderer, text, maxWidth);
        }

        protected final MultilineText createMultilineText(List<MultilineText.Line> lines) {
            return TextUtils.createMultilineText(this.client.textRenderer, lines, 2);
        }

        protected abstract List<Text> getTooltipText(int width);

        // region baseEntryRender
        protected boolean renderTooltip(DrawContext context, int mouseX, int mouseY, int width) {
            if (this.isMouseOver(mouseX, mouseY)) {
                context.drawTooltip(this.client.textRenderer, this.getTooltipText(width), mouseX, mouseY);
                return true;
            }

            return false;
        }
    }
}
