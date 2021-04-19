package io.github.nesz.sds.util.archive;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import net.sf.sevenzipjbinding.util.ByteArrayStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public final class ArchiveUtils {

    private ArchiveUtils() {}

    public static List<ArchiveEntry> readArchive(byte[] archive) throws IOException {
        return readArchive(archive, 0, "");
    }

    public static List<ArchiveEntry> readArchive(byte[] archive, int offset) throws IOException {
        return readArchive(archive, offset, "");
    }

    public static List<ArchiveEntry> readArchive(byte[] archive, int offset, String password) throws IOException {
        List<ArchiveEntry> entries = new ArrayList<>();
        try (ByteArrayStream archiveStream = new ByteArrayStream(archive, false, Integer.MAX_VALUE)) {
            archiveStream.seek(offset, 0);
            try (IInArchive inArchive = SevenZip.openInArchive(null, archiveStream);
                 ByteArrayOutputStream out = new ByteArrayOutputStream()) {

                ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();

                for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                    item.extractSlow(data -> {
                        try {
                            out.write(data);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                        return data.length;
                    }, password);

                    entries.add(new ArchiveEntry(item.getPath(), out.toByteArray()));
                    out.reset();
                }

                return entries;
            }
        }
    }

}
