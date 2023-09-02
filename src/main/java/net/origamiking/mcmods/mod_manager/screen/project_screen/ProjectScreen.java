package net.origamiking.mcmods.mod_manager.screen.project_screen;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
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
import org.jetbrains.annotations.Nullable;

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
//    private OptionListWidget list;
    private String jsonData;
    private final boolean isDataPack;
    private String levelName;
    @Nullable
    private TabNavigationWidget tabNavigation;
    private final TabManager tabManager = new TabManager(this::addDrawableChild, child -> this.remove((Element) child));


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
                .position(170, 250)
                .size(150, BUTTON_HEIGHT)
                .build());
        this.tabNavigation = TabNavigationWidget.builder(this.tabManager, this.width).tabs(new InfoTab(this.projectName, this.description), new DownloadTab(this.client, this)).build();
        this.addDrawableChild(this.tabNavigation);
        this.tabNavigation.selectTab(0, false);
//        int y = 30;
//        for (MutableText text : new Markdown(this.body).toText()) {
//            this.addDrawableChild(new ScrollableTextWidget(10, y, this.width - 10, 30, text, this.textRenderer));
//
//            y += 10;
//        }

//        this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
//        this.addSelectableChild(this.list);
        this.initTabNavigation();
    }

    @Override
    public void initTabNavigation() {
        if (this.tabNavigation == null/* || this.grid == null*/) {
            return;
        }
        this.tabNavigation.setWidth(this.width);
        this.tabNavigation.init();
        int i = this.tabNavigation.getNavigationFocus().getBottom();
        ScreenRect screenRect = new ScreenRect(0, i, this.width, this.height - i);
        this.tabManager.setTabArea(screenRect);
    }

    @Override
    public void tick() {
        this.tabManager.tick();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackgroundTexture(context);
//        this.list.render(context, mouseX, mouseY, delta);
//        context.drawCenteredTextWithShadow(this.textRenderer, this.projectName, this.width / 2, 10, 0xffffff);
//        context.drawCenteredTextWithShadow(this.textRenderer, this.description, this.width / 2, 25, 0xffffff);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    class InfoTab extends GridScreenTab {
        //        private static final Text ALLOW_COMMANDS_TEXT = Text.translatable("selectWorld.allowCommands");
//        private final TextFieldWidget worldNameField;

        InfoTab(String projectName, String description) {
            super(Text.of(projectName));
//            GridWidget.Adder adder = this.grid.setRowSpacing(8).createAdder(1);
//            Positioner positioner = adder.copyPositioner();
//            GridWidget.Adder adder2 = new GridWidget().setRowSpacing(4).createAdder(1);
//            adder2.add(new TextWidget(ENTER_NAME_TEXT, ((CreateWorldScreen) CreateWorldScreen.this).client.textRenderer), adder2.copyPositioner().marginLeft(1));
//            this.worldNameField = adder2.add(new TextFieldWidget(CreateWorldScreen.this.textRenderer, 0, 0, 208, 20, Text.translatable("selectWorld.enterName")), adder2.copyPositioner().margin(1));
//            this.worldNameField.setText(CreateWorldScreen.this.worldCreator.getWorldName());
//            this.worldNameField.setChangedListener(CreateWorldScreen.this.worldCreator::setWorldName);
//            ProjectScreen.this.worldCreator.addListener(creator -> this.worldNameField.setTooltip(Tooltip.of(Text.translatable("selectWorld.targetFolder", Text.literal(creator.getWorldDirectoryName()).formatted(Formatting.ITALIC)))));
//            ProjectScreen.this.setInitialFocus(this.worldNameField);
//            adder.add(adder2.getGridWidget(), adder.copyPositioner().alignHorizontalCenter());
//            CyclingButtonWidget<WorldCreator.Mode> cyclingButtonWidget = adder.add(CyclingButtonWidget.builder(value -> value.name).values((WorldCreator.Mode[]) new WorldCreator.Mode[]{WorldCreator.Mode.SURVIVAL, WorldCreator.Mode.HARDCORE, WorldCreator.Mode.CREATIVE}).build(0, 0, 210, 20, GAME_MODE_TEXT, (button, value) -> CreateWorldScreen.this.worldCreator.setGameMode((WorldCreator.Mode) ((Object) value))), positioner);
//            ProjectScreen.this.worldCreator.addListener(creator -> {
//                cyclingButtonWidget.setValue(creator.getGameMode());
//                cyclingButtonWidget.active = !creator.isDebug();
//                cyclingButtonWidget.setTooltip(Tooltip.of(creator.getGameMode().getInfo()));
//            });
//            CyclingButtonWidget<Difficulty> cyclingButtonWidget2 = adder.add(CyclingButtonWidget.builder(Difficulty::getTranslatableName).values((Difficulty[]) Difficulty.values()).build(0, 0, 210, 20, Text.translatable("options.difficulty"), (button, value) -> CreateWorldScreen.this.worldCreator.setDifficulty((Difficulty) value)), positioner);
//            ProjectScreen.this.worldCreator.addListener(creator -> {
//                cyclingButtonWidget2.setValue(CreateWorldScreen.this.worldCreator.getDifficulty());
//                cyclingButtonWidget.active = !CreateWorldScreen.this.worldCreator.isHardcore();
//                cyclingButtonWidget2.setTooltip(Tooltip.of(CreateWorldScreen.this.worldCreator.getDifficulty().getInfo()));
//            });
//            CyclingButtonWidget<Boolean> cyclingButtonWidget3 = adder.add(CyclingButtonWidget.onOffBuilder().tooltip(value -> Tooltip.of(ALLOW_COMMANDS_INFO_TEXT)).build(0, 0, 210, 20, ALLOW_COMMANDS_TEXT, (button, value) -> CreateWorldScreen.this.worldCreator.setCheatsEnabled((boolean) value)));
//            ProjectScreen.this.worldCreator.addListener(creator -> {
//                cyclingButtonWidget3.setValue(CreateWorldScreen.this.worldCreator.areCheatsEnabled());
//                cyclingButtonWidget.active = !CreateWorldScreen.this.worldCreator.isDebug() && !CreateWorldScreen.this.worldCreator.isHardcore();
//            });
//            if (!SharedConstants.getGameVersion().isStable()) {
//                adder.add(ButtonWidget.builder(EXPERIMENTS_TEXT, button -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder().dataConfiguration())).width(210).build());
//            }
        }
    }

    class DownloadTab extends GridScreenTab {
        private static final Text DOWNLOAD_TAB_TITLE_TEXT = Text.translatable("mod_manager.gui.project_screen.tab.download");
        DownloadTab(MinecraftClient client, Screen parent) {
            super(DOWNLOAD_TAB_TITLE_TEXT);
            int y1 = 250;
            int x1 = 250;
            GridWidget.Adder adder = this.grid.setRowSpacing(8).createAdder(2);
            adder.add(ButtonWidget.builder(Text.translatable("gui.download"), button -> {
                        if (!isDataPack) {
                            client.setScreen(new DownloadScreen(parent, projectName, slug, id, folder, isMod, false));
                        } else {
                            client.setScreen(new DownloadScreen(parent, projectName, slug, id, folder, levelName));
                        }
                    })
                    .position(x1, y1)
                    .size(75, BUTTON_HEIGHT)
                    .build());

//            adder.add(ButtonWidget.builder(GAME_RULES_TEXT, button -> this.openGameRulesScreen()).width(210).build());
//            adder.add(ButtonWidget.builder(EXPERIMENTS_TEXT, button -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder().dataConfiguration())).width(210).build());
//            adder.add(ButtonWidget.builder(DATA_PACKS_TEXT, button -> CreateWorldScreen.this.openPackScreen(CreateWorldScreen.this.worldCreator.getGeneratorOptionsHolder().dataConfiguration())).width(210).build());
        }
    }
}