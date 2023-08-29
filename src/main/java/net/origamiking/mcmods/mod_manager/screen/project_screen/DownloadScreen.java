package net.origamiking.mcmods.mod_manager.screen.project_screen;

import com.google.gson.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.ModManager;
import net.origamiking.mcmods.mod_manager.gui.widget.ModButtonWidget;
import net.origamiking.mcmods.mod_manager.modrinth.ModrinthApi;
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

import static net.origamiking.mcmods.mod_manager.download.ProjectDownload.download;

public class DownloadScreen extends Screen implements AutoCloseable {
    private OptionListWidget list;
    private int currentPage = 0;
    private final Screen parent;
    private final String folder;
    private final String slug;
    private final String id;
    private final boolean isMod;

    public DownloadScreen(Screen parent, String projectName, String slug, String id, String folder, boolean isMod) {
        super(Text.of(projectName));
        currentPage = 0;
        this.parent = parent;
        this.folder = folder;
        this.slug = slug;
        this.id = id;
        this.isMod = isMod;
    }

    @Override
    protected void init() {
        String jsonData = "";
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URI uri;
            if (isMod) {
                uri = new URIBuilder(ModrinthApi.MODRINTH_PROJECT + this.slug + "/version")
                        .addParameter("loaders", "[\"fabric\"]")
                        //todo config parameter
                        .addParameter("game_versions", ModManager.MINECRAFT_VERSIONS)
                        .build();
            } else {
                uri = new URIBuilder(ModrinthApi.MODRINTH_PROJECT + this.slug + "/version").build();
            }
            HttpGet httpGet = new HttpGet(uri);

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                jsonData = EntityUtils.toString(entity);
            }
        } catch (IOException | URISyntaxException e) {
            ModManager.LOGGER.error(String.valueOf(e));
        }

        int buttonWidth = this.width / 2;
        int cappedButtonWidth = Math.min(buttonWidth, 200);
        try {
            JsonArray root = JsonParser.parseString(jsonData).getAsJsonArray();

            int a = 1;
            int b = 1;
            int c = 50;
            for (JsonElement element : root) {
                JsonObject versionJson = element.getAsJsonObject();
                JsonArray filesArray = versionJson.getAsJsonArray("files");
                if (filesArray != null && !filesArray.isEmpty()) {
                    JsonObject fileJson = filesArray.get(0).getAsJsonObject();

                    String url = fileJson.get("url").getAsString();
                    String fileName = fileJson.get("filename").getAsString();
                    String name = versionJson.get("name").getAsString();
                    this.addDrawableChild(new ModButtonWidget(id, slug,(this.width - (this.width / 2 - 8)) + (buttonWidth / 2) - (cappedButtonWidth / 2) - 390 + a, 40 + c, Math.min(buttonWidth, 200), 20, Text.of(name), button -> download(url, fileName, this.folder), Supplier::get) {
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
            }
//            for (JsonElement element : root) {
//                VersionData modData = gson.fromJson(element.getAsJsonObject(), VersionData.class);
//
//
//                String url = modData.getUrl();
//                String fileName = modData.getFilename();
//                String name = modData.getName();
//                this.addDrawableChild(new ModButtonWidget((this.width - (this.width / 2 - 8)) + (buttonWidth / 2) - (cappedButtonWidth / 2) - 390 + a, 40 + c, Math.min(buttonWidth, 200), 20, Text.of(name), button -> download(url, fileName, this.folder), Supplier::get) {
//                    @Override
//                    public void render(DrawContext DrawContext, int mouseX, int mouseY, float delta) {
//                        super.render(DrawContext, mouseX, mouseY, delta);
//                    }
//                });
//                if (b >= 3) {
//                    b = 1;
//                    c += 50;
//                    a = 1;
//                } else {
//                    b++;
//                    a += 220;
//                }
//            }
        } catch (JsonParseException e) {
            ModManager.LOGGER.error(String.valueOf(e));
        }

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 90, this.height / 2 + 160, 200, 20, ScreenTexts.DONE, button -> close(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 130, this.height / 2 + 160, 200, 20, Text.translatable("gui.next_page"), button -> nextPage(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 310, this.height / 2 + 160, 200, 20, Text.translatable("gui.back_page"), button -> previousPage(), Supplier::get) {
            @Override
            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
                super.render(context, mouseX, mouseY, delta);
            }
        });

        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
    }

    protected void nextPage() {
        currentPage++;
        this.init(this.client, this.width, this.height);
    }

    protected void previousPage() {
        currentPage--;
        if (currentPage < 0) {
            currentPage = 0;
        }
        this.init(this.client, this.width, this.height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
        this.list.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }
}
