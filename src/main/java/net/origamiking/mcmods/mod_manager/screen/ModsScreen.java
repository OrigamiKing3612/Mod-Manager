package net.origamiking.mcmods.mod_manager.screen;

import com.google.gson.*;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.origamiking.mcmods.mod_manager.ModManager;
import net.origamiking.mcmods.mod_manager.modrinth.ModrinthApi;
import net.origamiking.mcmods.mod_manager.screen.project.ModScreen;
import net.origamiking.mcmods.mod_manager.utils.ModData;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.util.function.Supplier;

public class ModsScreen extends Screen {
    private final Screen parent;
    private OptionListWidget list;
    private int currentPage = 0;
    private static final int MODS_PER_PAGE = 9;

    protected ModsScreen(Screen parent) {
        super(Text.of("Mods"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        String jsonData = "";

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(ModrinthApi.MODRINTH_SEARCH + "?limit=100");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                jsonData = EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        int buttonWidth = this.width / 2;
        int cappedButtonWidth = Math.min(buttonWidth, 200);

        try {
            JsonObject root = JsonParser.parseString(jsonData).getAsJsonObject();
            JsonArray hitsArray = root.getAsJsonArray("hits");

            int startingIndex = currentPage * MODS_PER_PAGE;
            int endIndex = Math.min(startingIndex + MODS_PER_PAGE, hitsArray.size());

            int a = 1;
            int b = 1;
            int c = 50;

//            for (JsonElement hitElement : hitsArray) {
            for (int i = startingIndex; i < endIndex; i++) {
//                JsonObject hitObject = hitElement.getAsJsonObject();
                JsonObject hitObject = hitsArray.get(i).getAsJsonObject();
                ModData modData = gson.fromJson(hitObject, ModData.class);

                String modName = modData.getTitle();
                String author = modData.getAuthor();
                String description = modData.getDescription();
                String icon_url = modData.getIconUrl();

                this.addDrawableChild(new ButtonWidget((this.width - (this.width / 2 - 8)) + (buttonWidth / 2) - (cappedButtonWidth / 2) - 390 + a, 40 + c, Math.min(buttonWidth, 200), 20, Text.of(modName), button -> {
                    ModManager.LOGGER.debug(modName);
                    this.client.setScreen(new ModScreen(this, modName, author, description, icon_url));
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
            e.printStackTrace();
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
//        this.addDrawableChild(new ButtonWidget(this.width / 2 - 310, this.height / 2 + 160, 200, 20, Text.translatable("gui.back_page"), button -> close(), Supplier::get) {
//            @Override
//            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//                super.render(context, mouseX, mouseY, delta);
//            }
//        });
//        this.addDrawableChild(new ButtonWidget(this.width / 2 + 130, this.height / 2 + 160, 200, 20, Text.translatable("gui.next_page"), button -> close(), Supplier::get) {
//            @Override
//            public void render(DrawContext context, int mouseX, int mouseY, float delta) {
//                super.render(context, mouseX, mouseY, delta);
//            }
//        });
        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
        this.addSelectableChild(this.list);
    }

    private void nextPage() {
        currentPage++;
        this.init(this.client, this.width, this.height);
    }

    private void previousPage() {
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
        this.client.setScreen(this.parent);
    }
}
