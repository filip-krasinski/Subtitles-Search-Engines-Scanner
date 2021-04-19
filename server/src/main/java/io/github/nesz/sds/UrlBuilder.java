package io.github.nesz.sds;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UrlBuilder {

    private final Map<String, String> map;
    private String path;
    private String host;
    private String scheme;

    private UrlBuilder() {
        this.map = new TreeMap<>();
        this.path = "";
    }

    public UrlBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public UrlBuilder host(String host) {
        this.host = host;
        return this;
    }

    public UrlBuilder path(String path) {
        this.path += "/" + path;
        return this;
    }

    public UrlBuilder param(Object key, Object val) {
        return param(key::toString, val::toString);
    }

    public UrlBuilder param(Supplier<String> key, Supplier<String> val) {
        map.put(key.get(), val.get());
        return this;
    }

    public HttpRequest.Builder toRequestBuilder() {
        return HttpRequest.newBuilder(toUri());
    }

    public URI toUri() {
        var sb = new StringBuilder()
            .append(scheme)
            .append("://")
            .append(host)
            .append(path);

        if (!map.isEmpty()) {
            sb.append("?");
        }

        String params = map.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        sb.append(params);
        return URI.create(sb.toString());
    }

    public static UrlBuilder newBuilder() {
        return new UrlBuilder();
    }

}
