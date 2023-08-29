package net.origamiking.mcmods.mod_manager.screen.project_screen;

import com.google.gson.Gson;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.ModManager;
import net.origamiking.mcmods.mod_manager.modrinth.ModrinthApi;
import net.origamiking.mcmods.mod_manager.utils.ModData;
import net.origamiking.mcmods.mod_manager.utils.ProjectFolders;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Supplier;

public class ModScreen extends Screen implements AutoCloseable {
    private final String modName;
    private final String slug;
    private final String author;
    private final String description;
    private final String body;
    private final String id;
    private final Screen parent;
    private OptionListWidget list;
    private String jsonData;

    public ModScreen(Screen parent, String slug, String id, String modName) {
        super(Text.of(modName));
        this.modName = modName;
        this.parent = parent;
        this.slug = slug;
        this.id = id;

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URI uri = new URIBuilder(ModrinthApi.MODRINTH_PROJECT + this.slug).build();

            HttpGet httpGet = new HttpGet(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                jsonData = EntityUtils.toString(entity);
            }
        } catch (IOException | URISyntaxException e) {
            ModManager.LOGGER.error(String.valueOf(e));
        }
        Gson gson = new Gson();
        ModData modData = gson.fromJson(jsonData, ModData.class);

        this.description = modData.getDescription();
        this.author = modData.getAuthor();
        this.body = modData.getBody();

//        System.out.println(jsonData);
    }

    @Override
    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 90, this.height / 2 + 160, 200, 20, Text.translatable("gui.back"), button -> close(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 90, this.height / 2 + 100, 200, 20, Text.translatable("gui.download"), button -> this.client.setScreen(new DownloadScreen(this, this.modName, this.slug, this.id, ProjectFolders.MODS.getFolder(), true)), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });

        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        this.list.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.modName, this.width / 2, 5, 0xffffff);
        context.drawCenteredTextWithShadow(this.textRenderer, this.description, this.width / 2, 20, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
