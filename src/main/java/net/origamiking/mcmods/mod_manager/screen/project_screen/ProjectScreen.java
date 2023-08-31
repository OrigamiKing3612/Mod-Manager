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

public class ProjectScreen extends Screen implements AutoCloseable {
    private final int BUTTON_HEIGHT = 20;
    private final String projectName;
    private final String slug;
    private final String author;
    private final String description;
    private final String body;
    private final String id;
    private final Screen parent;
    private final boolean isMod;
    private final String folder;
    private OptionListWidget list;
    private String jsonData;
    private boolean isDataPack;
    private String levelName;

    public ProjectScreen(Screen parent, String slug, String id, String projectName, String folder, String levelName) {
        this(parent, slug, id, projectName, folder, false, true);
        this.levelName = levelName;
    }

    public ProjectScreen(Screen parent, String slug, String id, String projectName, String folder, boolean isMod, boolean isDataPack) {
        super(Text.of(projectName));
        this.projectName = projectName;
        this.parent = parent;
        this.slug = slug;
        this.id = id;
        this.folder = folder;
        this.isMod = isMod;
        this.isDataPack = isDataPack;

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
    }

    public ProjectScreen(Screen parent, String slug, String id, String projectName, String folder, boolean isMod) {
        this(parent, slug, id, projectName, folder, isMod, false);
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), button -> close())
                .position(this.width / 2 - 70, 190 + 40)
                .size(150, BUTTON_HEIGHT)
                .build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("gui.download"), button -> {
                    if (!isDataPack) {
                        this.client.setScreen(new DownloadScreen(this, this.projectName, this.slug, this.id, this.folder, this.isMod, this.isDataPack));
                    } else {
                        this.client.setScreen(new DownloadScreen(this, this.projectName, this.slug, this.id, this.folder, this.levelName));
                    }
                })
                .position(this.width / 2 - 70, 190)
                .size(150, BUTTON_HEIGHT)
                .build());

//        this.addDrawableChild(new TextFieldWidget(this.textRenderer,0, 50, this.width / 2, this.height / 2, Text.of(body)));

        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        this.list.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.projectName, this.width / 2, 5, 0xffffff);
        context.drawCenteredTextWithShadow(this.textRenderer, this.description, this.width / 2, 20, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}