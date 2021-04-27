package dentaira.cobaco.server.file.web.api;

import dentaira.cobaco.server.file.DataSize;
import dentaira.cobaco.server.file.FileType;
import dentaira.cobaco.server.file.Owner;
import dentaira.cobaco.server.file.StoredFile;
import dentaira.cobaco.server.file.app.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class FileRestController {

    private FileService fileService;

    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("api/folder/root/{ownerId}")
    public FolderResource getRootFolder(@PathVariable String ownerId) {
        Owner owner = new Owner(UUID.fromString(ownerId));

        List<StoredFile> children = fileService.searchRoot(owner);
        StoredFile root = new StoredFile(UUID.randomUUID(), "home", Path.of("/"), FileType.DIRECTORY, DataSize.of(0L));

        List<FileResource> fileResources = children.stream().map(FileResource::of).collect(Collectors.toList());
        return FolderResource.of(root, fileResources);
    }

    @GetMapping("api/folder/{fileId}/{ownerId}")
    public FolderResource getFolder(@PathVariable String fileId, @PathVariable String ownerId) {
        Owner owner = new Owner(UUID.fromString(ownerId));

        StoredFile folder = fileService.findById(fileId, owner);
        List<StoredFile> children = fileService.search(fileId, owner);
        List<StoredFile> ancestors = fileService.findAncestors(fileId, owner);

        List<FileResource> fileResources = children.stream().map(FileResource::of).collect(Collectors.toList());
        return FolderResource.of(folder, fileResources, ancestors);
    }
}
