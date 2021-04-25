package dentaira.cobaco.server.file.web.api;

import dentaira.cobaco.server.file.StoredFile;

import java.util.List;
import java.util.UUID;

public class FolderResource {

    private static final long serialVersionUID = 1L;

    private UUID fileId;

    private String name;

    private List<FileResource> children;

    public static FolderResource of(StoredFile folder, List<FileResource> children) {
        return new FolderResource(folder.getId(), folder.getName(), children);
    }

    public FolderResource(UUID fileId, String name, List<FileResource> children) {
        this.fileId = fileId;
        this.name = name;
        this.children = children;
    }

    public UUID getFileId() {
        return fileId;
    }

    public String getName() {
        return name;
    }

    public List<FileResource> getChildren() {
        return children;
    }
}
