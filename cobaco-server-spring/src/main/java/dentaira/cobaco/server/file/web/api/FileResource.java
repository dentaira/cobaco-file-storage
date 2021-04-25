package dentaira.cobaco.server.file.web.api;

import dentaira.cobaco.server.file.FileType;
import dentaira.cobaco.server.file.StoredFile;

import java.util.UUID;

public class FileResource {

    private static final long serialVersionUID = 1L;

    private final UUID fileId;

    private String name;

    private FileType type;

    private long size;

    public static FileResource of(StoredFile file) {
        return new FileResource(file.getId(), file.getName(), file.getType(), file.getSize().toLong());
    }

    public FileResource(UUID fileId, String name, FileType type, long size) {
        this.fileId = fileId;
        this.name = name;
        this.type = type;
        this.size = size;
    }

    public UUID getFileId() {
        return fileId;
    }

    public String getName() {
        return name;
    }

    public FileType getType() {
        return type;
    }

    public long getSize() {
        return size;
    }
}
