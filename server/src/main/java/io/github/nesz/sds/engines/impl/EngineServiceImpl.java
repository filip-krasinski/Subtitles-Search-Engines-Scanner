package io.github.nesz.sds.engines.impl;

import io.github.nesz.sds.engines.Engine;
import io.github.nesz.sds.engines.EngineService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EngineServiceImpl implements EngineService {

    private final Set<Engine> engines;

    public EngineServiceImpl() {
        engines = new HashSet<>();
    }

    @Override
    public void addEngine(Engine... engines) {
        this.engines.addAll(Arrays.asList(engines));
    }

    @Override
    public void removeEngine(Engine engine) {
        engines.remove(engine);
    }

    @Override
    public Set<Engine> getEngines() {
        return engines;
    }

}
