package io.github.nesz.sds.engines.impl;

import io.github.nesz.sds.engines.EngineResponse;

public class EngineResponseImpl implements EngineResponse {

    private final byte[] data;
    private final String ext;

    public EngineResponseImpl(byte[] data, String ext) {
        this.data = data;
        this.ext = ext;
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public String getExtension() {
        return ext;
    }

}
