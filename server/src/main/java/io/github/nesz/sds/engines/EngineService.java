package io.github.nesz.sds.engines;

import io.github.nesz.sds.engines.impl.EngineServiceImpl;

import java.util.Set;

public interface EngineService {

    EngineService INSTANCE = new EngineServiceImpl();

    void addEngine(Engine... engines);

    void removeEngine(Engine engine);

    Set<Engine> getEngines();

}
