package io.github.nesz.sds.routes;

import io.github.nesz.sds.engines.Engine;
import io.github.nesz.sds.engines.EngineResponse;
import io.github.nesz.sds.engines.EngineService;
import io.github.nesz.sds.util.StringUtils;
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
        JSONObject jsonResponse = new JSONObject();
        response.header("Content-Type", "application/json");
        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Allow-Credentials", "true");

        String fsParam = request.queryParams("fs");
        String lnParam = request.queryParams("ln");

        if (fsParam == null)
            return jsonResponse.put("ERROR", "param 'fs' is mandatory").toString();

        if (lnParam == null)
            return jsonResponse.put("ERROR", "param 'ln' is mandatory").toString();

        Optional<Long> maybeFS = StringUtils.longFromString(fsParam);
        if (maybeFS.isEmpty() || maybeFS.get() < 1)
            return jsonResponse.put("ERROR", "param 'fs' is not valid number").toString();

        Locale locale = new Locale(lnParam);

        JSONObject json = new JSONObject();
        for (Engine engine : EngineService.INSTANCE.getEngines()) {
            String hash = request.queryParams(engine.getHost());
            if (hash == null) continue;
            if (!engine.supports(lnParam)) continue;

            Optional<EngineResponse> maybeFetch = engine.fetch(locale, hash, maybeFS.get());
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