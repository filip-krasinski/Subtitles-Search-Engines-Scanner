package io.github.nesz.sds;

import io.github.nesz.sds.engines.EngineService;
import io.github.nesz.sds.engines.impl.engines.EngineNapiprojekt;
import io.github.nesz.sds.engines.impl.engines.EngineNapisy24;
import io.github.nesz.sds.engines.impl.engines.EngineOpenSubtitles;
import io.github.nesz.sds.routes.RouteScan;
import org.tinylog.Logger;

import static spark.Spark.*;

public class SDS {

    public static void main(String[] args) {
        EngineService.INSTANCE.addEngine(
            new EngineNapiprojekt(),
            new EngineOpenSubtitles(),
            new EngineNapisy24()
        );

        port(8080);
        path("/api", () -> {

            before("/*", (req, res) ->
                    Logger.info("request at '{}', type: '{}'", req.pathInfo(), req.requestMethod()));

            get("/scan", new RouteScan());

            notFound((req, res) -> {
                res.status(404);
                return "Not found";
            });

            internalServerError((req, res) -> {
                res.status(500);
                return "Internal server error";
            });

            exception(Exception.class, (e, request, response) -> Logger.error(e));
        });
    }

}
