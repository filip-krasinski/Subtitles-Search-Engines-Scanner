package io.github.nesz.sds.engines;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public interface Engine {

    String getHost();

    Set<String> getSupportedLanguages();

    boolean supports(String lang);

    Optional<EngineResponse> fetch(Locale locale, String hash, long fileSize);

}
