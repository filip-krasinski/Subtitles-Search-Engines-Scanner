package io.github.nesz.sds.engines.impl.engines;

import io.github.nesz.sds.FormDataBuilder;
import io.github.nesz.sds.UrlBuilder;
import io.github.nesz.sds.engines.Engine;
import io.github.nesz.sds.engines.EngineResponse;
import io.github.nesz.sds.util.BytesUtils;
import io.github.nesz.sds.util.archive.ArchiveEntry;
import io.github.nesz.sds.util.archive.ArchiveUtils;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class EngineNapisy24 implements Engine {


    private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>(Arrays.asList("pl"));
    private static final String UA = "tantalosus";
    private static final String AP = "susolatnat";
    private static final String HOST = "napisy24.pl";

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
        HttpRequest request = UrlBuilder.newBuilder()
                .scheme("https")
                .host(HOST)
                .path("run")
                .path("CheckSubAgent.php")
                .toRequestBuilder()
                .timeout(Duration.of(5, ChronoUnit.SECONDS))
                .POST(FormDataBuilder.newBuilder()
                        .add("postAction", "CheckSub")
                        .add("fh", hash)
                        .add("fs", String.valueOf(fileSize))
                        .add("ua", UA)
                        .add("ap", AP)
                        .toBody())
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try {
            HttpResponse<byte[]> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (HttpURLConnection.HTTP_OK != response.statusCode()) {
                return Optional.empty();
            }

            if (BytesUtils.startsWith(response.body(), "OK-0".getBytes()) ||
                BytesUtils.startsWith(response.body(), "OK-1".getBytes())) {
                return Optional.empty();
            }

            int zipStartIndex = BytesUtils.indexOf(response.body(), "||".getBytes()) + 2;
            List<ArchiveEntry> archiveEntries = ArchiveUtils.readArchive(response.body(), zipStartIndex);

            if (archiveEntries.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(EngineResponse.of(archiveEntries.get(0)));

        } catch (IOException | InterruptedException e) {
            Logger.error(e);
        }

        return Optional.empty();
    }

}