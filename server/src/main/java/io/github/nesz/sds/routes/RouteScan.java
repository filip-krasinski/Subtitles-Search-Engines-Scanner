package io.github.nesz.sds.routes;

import io.github.nesz.sds.engines.Engine;
import io.github.nesz.sds.engines.EngineResponse;
import io.github.nesz.sds.engines.EngineService;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

public class RouteScan implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        response.header("Content-Type", "application/json");
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Allow-Credentials", "true");
        long fileSize = Long.parseLong(request.queryParams("fs"));
        String language = request.queryParams("ln");
        Locale locale = new Locale(language);

        JSONObject json = new JSONObject();
        for (Engine engine : EngineService.INSTANCE.getEngines()) {
            String hash = request.queryParams(engine.getHost());
            if (hash == null) continue;
            if (!engine.supports(language)) continue;

            Optional<EngineResponse> maybeFetch = engine.fetch(locale, hash, fileSize);
            if (maybeFetch.isPresent()) {
                EngineResponse fetch = maybeFetch.get();
                json.put("host",  engine.getHost());
                json.put("ext", fetch.getExtension());
                json.put("size", fetch.getData().length);
                json.put("data", Base64.getEncoder().encodeToString(fetch.getData()));
                break;
            }
        }

        return json.toString();
    }
}