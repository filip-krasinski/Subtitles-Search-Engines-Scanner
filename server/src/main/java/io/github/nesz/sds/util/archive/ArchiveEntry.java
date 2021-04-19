package io.github.nesz.sds.util.archive;

public class ArchiveEntry {

    private final String inArchivePath;
    private final byte[] data;

    public ArchiveEntry(String inArchivePath, byte[] data) {
        this.inArchivePath = inArchivePath;
        this.data = data;
    }

    public String getInArchivePath() {
        return inArchivePath;
    }

    public byte[] getData() {
        return data;
    }

}