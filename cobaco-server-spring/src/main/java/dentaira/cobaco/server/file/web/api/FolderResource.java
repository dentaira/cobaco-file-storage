package dentaira.cobaco.server.file.web.api;

import dentaira.cobaco.server.file.StoredFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FolderResource {

    private static final long serialVersionUID = 1L;

    private UUID fileId;

    private String name;

    private List<FileResource> children;

    private List<String> ancestorIds;

    private List<String> ancestorNames;

    public static FolderResource of(StoredFile folder, List<FileResource> children) {
        return new FolderResource(folder.getId(), folder.getName(), children, Collections.emptyList());
    }

    public static FolderResource of(StoredFile folder, List<FileResource> children, List<StoredFile> ancestors) {
        return new FolderResource(folder.getId(), folder.getName(), children, ancestors);
    }

    public FolderResource(UUID fileId, String name, List<FileResource> children, List<StoredFile> ancestors) {
        this.fileId = fileId;
        this.name = name;
        this.children = children;
        this.ancestorIds = new ArrayList<>(ancestors.size());
        this.ancestorNames = new ArrayList<>(ancestors.size());
        for (StoredFile file : ancestors) {
            ancestorIds.add(file.getId().toString());
            ancestorNames.add(file.getName());
        }
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

    public List<String> getAncestorIds() {
        return ancestorIds;
    }

    public List<String> getAncestorNames() {
        return ancestorNames;
    }
}
