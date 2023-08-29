package net.origamiking.mcmods.mod_manager.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.origamiking.mcmods.mod_manager.ModManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ImageUtils {//Most of this code is from [VTDownloader](https://github.com/IotaBread/VTDownloader) Thanks!
    private static HttpClient httpClient;
    private static final ThreadFactory DOWNLOAD_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat("Mod Manager").build();
    private static final ExecutorService DOWNLOAD_EXECUTOR = Executors.newCachedThreadPool(DOWNLOAD_THREAD_FACTORY);

    public static CompletableFuture<Boolean> downloadIcon(String icon_url, String slug) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return executeRequest(createHttpPost(icon_url));
            } catch (IOException e) {
                throw new RuntimeException("Failed to execute icon download request", e);
            }
        }, DOWNLOAD_EXECUTOR).thenApplyAsync(response -> {
            int code = response.getStatusLine().getStatusCode();
//
//            if (code == HttpStatus.SC_MOVED_TEMPORARILY || code == HttpStatus.SC_MOVED_PERMANENTLY) {
//                Header locationHeader = response.getFirstHeader("Location");
//                if (locationHeader != null) {
//                    String newIconUrl = locationHeader.getValue();
//                    return downloadIcon(newIconUrl, slug).join();
//                }
//            }

            if (code / 100 != 2) {
                throw new IllegalStateException("Icon download request returned status code " + code);
            }

            try /*(InputStream stream = response.getEntity().getContent())*/ {
                TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
                Identifier id = getIconId(icon_url, slug);
                NativeImageBackedTexture icon = new NativeImageBackedTexture(NativeImage.read(new URL(icon_url).openStream()));

                textureManager.registerTexture(id, icon);
                textureManager.bindTexture(id);
                return true;
            } catch (IOException e) {
                throw new RuntimeException("Failed to read icon download response", e);
            }
        });
    }

    public static <R extends HttpRequestBase> HttpResponse executeRequest(R request) throws IOException {
        request.addHeader("User-Agent", "Mod-Manager v" + ModManager.VERSION);
        return getClient().execute(request);
    }

    private static HttpClient getClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createDefault();
        }

        return httpClient;
    }

    @Contract("_ -> new")
    private static HttpGet createHttpGet(String icon_url) {
        return new HttpGet(icon_url);
    }

    @Contract("_ -> new")
    private static HttpPost createHttpPost(String icon_url) {
        return new HttpPost(icon_url);
    }

    public static Identifier getIconId(String icon_url, String slug) {
        if (icon_url == null) {
            throw new RuntimeException("icon_url is: null");
        } else {
            return new Identifier(ModManager.MOD_ID, slug + "_icon");
        }
    }
}
