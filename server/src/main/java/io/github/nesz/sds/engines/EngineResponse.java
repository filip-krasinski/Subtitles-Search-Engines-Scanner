package io.github.nesz.sds.engines;

import io.github.nesz.sds.engines.impl.EngineResponseImpl;
import io.github.nesz.sds.util.StringUtils;
import io.github.nesz.sds.util.archive.ArchiveEntry;

public interface EngineResponse {

    static EngineResponse of(byte[] data, String ext) {
        return new EngineResponseImpl(data, ext);
    }

    static EngineResponse of(ArchiveEntry entry) {
        String ext = StringUtils.getExtensionOrDefault(entry.getInArchivePath(), "txt");
        return new EngineResponseImpl(entry.getData(), ext);
    }

    byte[] getData();

    String getExtension();

}
