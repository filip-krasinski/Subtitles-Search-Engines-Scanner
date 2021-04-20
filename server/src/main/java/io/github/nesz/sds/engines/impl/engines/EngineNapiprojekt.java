package io.github.nesz.sds.engines.impl.engines;

import io.github.nesz.sds.UrlBuilder;
import io.github.nesz.sds.engines.Engine;
import io.github.nesz.sds.engines.EngineResponse;
import io.github.nesz.sds.util.BytesUtils;
import io.github.nesz.sds.util.archive.ArchiveEntry;
import io.github.nesz.sds.util.archive.ArchiveUtils;
import org.tinylog.Logger;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class EngineNapiprojekt implements Engine {

    private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>(Arrays.asList("en", "pl"));
    private static final byte[] EMPTY_RESPONSE = "NPc".getBytes();
    private static final String ZIP_PASSWORD = "iBlm8NTigvru0Jr0";
    private static final String HOST = "napiprojekt.pl";

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
        String ciph = cipher(hash);
        String lang = localeToParameter(locale);

        HttpRequest request = UrlBuilder.newBuilder()
                .scheme("https")
                .host(HOST)
                .path("unit_napisy")
                .path("dl.php")
                .param("l", lang)
                .param("f", hash)
                .param("t", ciph)
                .param("v", "other")
                .toRequestBuilder()
                .timeout(Duration.of(5, ChronoUnit.SECONDS))
                .GET()
                .build();

        try {
            HttpResponse<byte[]> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (HttpURLConnection.HTTP_OK != response.statusCode()) {
                return Optional.empty();
            }

            if (BytesUtils.startsWith(response.body(), EMPTY_RESPONSE)) {
                return Optional.empty();
            }

            List<ArchiveEntry> archiveEntries = ArchiveUtils.readArchive(response.body(), 0, ZIP_PASSWORD);
            if (archiveEntries.isEmpty()) {
                return Optional.empty();
            }

            return Optional.of(EngineResponse.of(archiveEntries.get(0)));

        } catch (IOException | InterruptedException e) {
            Logger.error(e);
        }

        return Optional.empty();
    }

    private String localeToParameter(Locale locale) {
        String lang = locale.getLanguage();
        return lang.equals("en") ? "ENG" : lang.toUpperCase();
    }

    private static String cipher(String input) {
        int[] idx = new int[] { 0xe, 0x3, 0x6, 0x8, 0x2 };
        int[] mul = new int[] { 2, 2, 5, 4, 3};
        int[] add = new int[] { 0, 0xd, 0x10, 0xb, 0x5 };
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < idx.length; j++) {
            int a = add[j];
            int m = mul[j];
            int i = idx[j];
            int t = a + Integer.parseInt(input.substring(i, i + 1), 16);
            int v = Integer.parseInt(input.substring(t, Math.min(t + 2, input.length())), 16);
            String tmp = String.format("%x", v * m);
            result.append(tmp.substring(tmp.length() - 1));
        }
        return result.toString();
    }
}
