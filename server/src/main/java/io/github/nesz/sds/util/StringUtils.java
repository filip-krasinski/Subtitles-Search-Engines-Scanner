package io.github.nesz.sds.util;

import java.util.Optional;

public final class StringUtils {

    private StringUtils() {}

    public static String getExtensionOrDefault(String filename, String def) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse(def);
    }

    public static Optional<Long> longFromString(String parse) {
        try {
            return Optional.of(Long.parseLong(parse));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}