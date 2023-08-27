package net.origamiking.mcmods.mod_manager.screen.browse;

import com.google.gson.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.ModManager;
import net.origamiking.mcmods.mod_manager.gui.widget.ModButtonWidget;
import net.origamiking.mcmods.mod_manager.modrinth.ModrinthApi;
import net.origamiking.mcmods.mod_manager.screen.project_screen.ResourcePackScreen;
import net.origamiking.mcmods.mod_manager.utils.ProjectsScreen;
import net.origamiking.mcmods.mod_manager.utils.ResourcePackData;
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

public class ResourcePacksScreen extends ProjectsScreen {
    private Screen parent;
    private static final int PACKS_PER_PAGE = 12;


    public ResourcePacksScreen(Screen parent) {
        super(Text.of("Resource Packs"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        String jsonData = "";

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URI uri = new URIBuilder(ModrinthApi.MODRINTH_SEARCH)
//                    .addParameter("limit", String.valueOf(MODS_PER_PAGE))
                    .addParameter("limit", "100")
//                    .addParameter("offset", String.valueOf(MODS_PER_PAGE * currentPage))
                    .addParameter("facets", "[[\"project_type:resourcepack\"]]")
                    .build();

            HttpGet httpGet = new HttpGet(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                jsonData = EntityUtils.toString(entity);
            }
        } catch (IOException | URISyntaxException e) {
            ModManager.LOGGER.error(String.valueOf(e));
        }

        Gson gson = new Gson();
        int buttonWidth = this.width / 2;
        int cappedButtonWidth = Math.min(buttonWidth, 200);

        try {
            JsonObject root = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray hitsArray = root.getAsJsonArray("hits");

            int startingIndex = currentPage * PACKS_PER_PAGE;
            int endIndex = Math.min(startingIndex + PACKS_PER_PAGE, hitsArray.size());

            int a = 1;
            int b = 1;
            int c = 50;

            for (int i = startingIndex; i < endIndex; i++) {
                JsonObject hitObject = hitsArray.get(i).getAsJsonObject();
                ResourcePackData resourcePackData = gson.fromJson(hitObject, ResourcePackData.class);

                String modName = resourcePackData.getTitle();
                String slug = resourcePackData.getSlug();
                String author = resourcePackData.getAuthor();
                String description = resourcePackData.getDescription();
                String icon_url = resourcePackData.getIconUrl();

                this.addDrawableChild(new ModButtonWidget((this.width - (this.width / 2 - 8)) + (buttonWidth / 2) - (cappedButtonWidth / 2) - 390 + a, 40 + c, Math.min(buttonWidth, 200), 20, Text.of(modName), button -> {
                    ModManager.LOGGER.debug(modName);
                    this.client.setScreen(new ResourcePackScreen(this, modName, slug, author, description, icon_url));
                }, Supplier::get) {
                    @Override
                    public void render(DrawContext DrawContext, int mouseX, int mouseY, float delta) {
                        super.render(DrawContext, mouseX, mouseY, delta);
                    }
                });
                if (b >= 3) {
                    b = 1;
                    c += 50;
                    a = 1;
                } else {
                    b++;
                    a += 220;
                }
            }
        } catch (JsonParseException e) {
            ModManager.LOGGER.error(String.valueOf(e));
        }
        super.init();
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }
}