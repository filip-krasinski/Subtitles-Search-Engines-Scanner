package io.github.nesz.sds.engines.impl.engines;

import com.neovisionaries.i18n.LanguageAlpha3Code;
import io.github.nesz.sds.UrlBuilder;
import io.github.nesz.sds.engines.Engine;
import io.github.nesz.sds.engines.EngineResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class EngineOpenSubtitles implements Engine {

    private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>();
    private static final String AGENT = "TemporaryUserAgent";
    private static final String HOST = "opensubtitles.org";

    @Override
    public String getHost() {
        return HOST;
    }

    @Override
    public Set<String> getSupportedLanguages() {
        return new HashSet<>(SUPPORTED_LANGUAGES);
    }

    @Override
    public boolean supports(String lang) {
        return SUPPORTED_LANGUAGES.isEmpty() || SUPPORTED_LANGUAGES.contains(lang);
    }


    @Override
    public Optional<EngineResponse> fetch(Locale locale, String hash, long fileSize) {
        LanguageAlpha3Code alpha3 = LanguageAlpha3Code.getByCode(locale.getLanguage());
        if (alpha3 == null)
            return Optional.empty();

        HttpRequest request = UrlBuilder.newBuilder()
                .scheme("https")
                .host("rest.opensubtitles.org")
                .path("search")
                .path("dl.php")
                .path("moviehash-" + hash)
                .path("moviebytesize-" + fileSize)
                .path("sublanguageid-" + alpha3)
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