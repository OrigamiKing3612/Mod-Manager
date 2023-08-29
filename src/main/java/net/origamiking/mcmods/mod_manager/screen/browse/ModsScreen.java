package net.origamiking.mcmods.mod_manager.screen.browse;

import com.google.gson.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.ModManager;
import net.origamiking.mcmods.mod_manager.gui.widget.ModButtonWidget;
import net.origamiking.mcmods.mod_manager.modrinth.ModrinthApi;
import net.origamiking.mcmods.mod_manager.screen.project_screen.ModScreen;
import net.origamiking.mcmods.mod_manager.utils.ProjectData;
import net.origamiking.mcmods.mod_manager.utils.ProjectsScreen;
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

public class ModsScreen extends ProjectsScreen {
    private final Screen parent;
    private static final int MODS_PER_PAGE = 15;

    public ModsScreen(Screen parent) {
        super(Text.of("Mods"));
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
                    .addParameter("facets", "[[\"project_type:mod\"]]")
                    .addParameter("filters", "categories=\"fabric\"")
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
        int buttonWidth = 150;

        try {
            JsonObject root = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray hitsArray = root.getAsJsonArray("hits");

            int startingIndex = currentPage * MODS_PER_PAGE;
            int endIndex = Math.min(startingIndex + MODS_PER_PAGE, hitsArray.size());

            int xOffsetInRow = 0;
            int buttonsPerRow = 1;
            int rowY = 50;
            int startX = 480;

            for (int i = startingIndex; i < endIndex; i++) {
                JsonObject hitObject = hitsArray.get(i).getAsJsonObject();
                ProjectData projectData = gson.fromJson(hitObject, ProjectData.class);

                String modName = projectData.getTitle();
                String icon_url = projectData.getIconUrl();
                String slug = projectData.getSlug();

                this.addDrawableChild(ModButtonWidget.builder(icon_url, slug, Text.of(modName), button -> this.client.setScreen(new ModScreen(this, slug, icon_url, modName)))
                        .position((this.width) - startX + xOffsetInRow, 10 + rowY)
                        .size(buttonWidth, BUTTON_HEIGHT)
                        .build());

                if (buttonsPerRow >= 3) {
                    buttonsPerRow = 1;
                    rowY += BUTTON_HEIGHT + 5;
                    xOffsetInRow = 0;
                } else {
                    buttonsPerRow++;
                    xOffsetInRow += buttonWidth + 10;
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
