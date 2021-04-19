package io.github.nesz.sds;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class FormDataBuilder {

    private Map<String, String> props;

    public FormDataBuilder() {
        this.props = new TreeMap<>();
    }

    public FormDataBuilder add(Object key, Object val) {
        return add(key::toString, val::toString);
    }

    public FormDataBuilder add(Supplier<String> key, Supplier<String> val) {
        props.put(key.get(), val.get());
        return this;
    }

    public HttpRequest.BodyPublisher toBody() {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : props.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }

        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public static FormDataBuilder newBuilder() {
        return new FormDataBuilder();
    }
}
