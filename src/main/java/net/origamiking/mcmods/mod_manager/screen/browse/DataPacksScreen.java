package net.origamiking.mcmods.mod_manager.screen.browse;

import com.google.gson.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.ModManager;
import net.origamiking.mcmods.mod_manager.gui.widget.ModButtonWidget;
import net.origamiking.mcmods.mod_manager.modrinth.ModrinthApi;
import net.origamiking.mcmods.mod_manager.screen.project_screen.ProjectScreen;
import net.origamiking.mcmods.mod_manager.utils.ProjectData;
import net.origamiking.mcmods.mod_manager.utils.ProjectFolders;
import net.origamiking.mcmods.mod_manager.utils.ProjectsScreen;
import net.origamiking.mcmods.mod_manager.utils.Utils;
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

public class DataPacksScreen extends ProjectsScreen {
    private final Screen parent;
    private static final int PACKS_PER_PAGE = 15;
    private final String levelName;

    public DataPacksScreen(Screen parent, String levelName) {
        super(Text.of("Data Packs"));
        this.parent = parent;
        this.levelName = levelName;
    }

    @Override
    protected void init() {
        String jsonData = "";

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URI uri = new URIBuilder(ModrinthApi.MODRINTH_SEARCH)
                    .addParameter("limit", "100")
                    .addParameter("facets", "[[\"project_type:mod\"]]")
                    .addParameter("filters", "categories=\"datapack\"")
                    .addParameter("query", SearchString)
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

            if (hitsArray == null) {
                ModManager.LOGGER.error("An error occurred while getting Data Packs");
                Utils.showToast(Text.translatable("mod_manager.toast.error_getting.line1"), Text.translatable("mod_manager.toast.error.line2"));
                return;
            }

            int startingIndex = currentPage * PACKS_PER_PAGE;
            int endIndex = Math.min(startingIndex + PACKS_PER_PAGE, hitsArray.size());

            int xOffsetInRow = 0;
            int buttonsPerRow = 1;
            int rowY = 50;
            int startX = 480;

            for (int i = startingIndex; i < endIndex; i++) {
                JsonObject hitObject = hitsArray.get(i).getAsJsonObject();
                ProjectData projectData = gson.fromJson(hitObject, ProjectData.class);

                String dataPackName = projectData.getTitle();
                String icon_url = projectData.getIconUrl();
                String slug = projectData.getSlug();

                this.addDrawableChild(ModButtonWidget.builder(icon_url, slug, Text.of(dataPackName), button -> this.client.setScreen(new ProjectScreen(this, slug, icon_url, dataPackName, ProjectFolders.DATAPACKS.getFolder(), this.levelName)))
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(this.parent);
    }

}
