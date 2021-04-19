package io.github.nesz.sds.engines.impl.engines;

import com.neovisionaries.i18n.LanguageAlpha3Code;
import io.github.nesz.sds.FormDataBuilder;
import io.github.nesz.sds.UrlBuilder;
import io.github.nesz.sds.engines.Engine;
import io.github.nesz.sds.engines.EngineResponse;
import io.github.nesz.sds.util.BytesUtils;
import io.github.nesz.sds.util.archive.ArchiveEntry;
import io.github.nesz.sds.util.archive.ArchiveUtils;
import org.eclipse.jetty.http.HttpHeader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class EngineOpenSubtitles implements Engine {

    private static final String AGENT = "TemporaryUserAgent";
    private static final String HOST = "opensubtitles.org";

    @Override
    public String getHost() {
        return HOST;
    }

    @Override
    public Set<String> getSupportedLanguages() {
        return null;
    }

    @Override
    public boolean supports(String lang) {
        return true;
    }


    @Override
    public Optional<EngineResponse> fetch(Locale locale, String hash, long fileSize) {
        HttpRequest request = UrlBuilder.newBuilder()
                .scheme("https")
                .host("rest.opensubtitles.org")
                .path("search")
                .path("dl.php")
                .path("moviehash-" + hash)
                .path("moviebytesize-" + fileSize)
                .path("sublanguageid-" + LanguageAlpha3Code.getByCode(locale.getLanguage()).toString())
                .toRequestBuilder()
                .setHeader("User-Agent", AGENT)
                .timeout(Duration.of(5, ChronoUnit.SECONDS))
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (HttpURLConnection.HTTP_OK != response.statusCode()) {
                return Optional.empty();
            }

            JSONArray array = new JSONArray(response.body());
            if (array.isEmpty()) {
                return Optional.empty();
            }

            JSONObject entry = array.getJSONObject(0);

            URI downloadUri = URI.create(entry.getString("SubDownloadLink").replace(".gz", ""));

            HttpRequest downloadRequest = HttpRequest.newBuilder(downloadUri)
                    .build();

            HttpResponse<byte[]> downloadResponse = HttpClient.newBuilder()
                    .build()
                    .send(downloadRequest, HttpResponse.BodyHandlers.ofByteArray());

            if (HttpURLConnection.HTTP_OK != downloadResponse.statusCode()) {
                return Optional.empty();
            }

            return Optional.of(EngineResponse.of(downloadResponse.body(), entry.getString("SubFormat")));

        } catch (IOException | InterruptedException e) {
            Logger.error(e);
        }

        return Optional.empty();
    }

}