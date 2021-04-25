package dentaira.cobaco.server.file.web.api;

import dentaira.cobaco.server.file.FileService;
import dentaira.cobaco.server.file.Owner;
import dentaira.cobaco.server.file.StoredFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class FileRestController {

    private FileService fileService;

    public FileRestController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("api/folder/{fileId}/{userId}")
    public FolderResource getFolder(@PathVariable String fileId, @PathVariable String userId) {
        Owner owner = new Owner(UUID.fromString(userId));

        StoredFile folder = fileService.findById(fileId, owner);
        List<StoredFile> children = fileService.search(fileId, owner);

        List<FileResource> fileResources = children.stream().map(FileResource::of).collect(Collectors.toList());
        return FolderResource.of(folder, fileResources);
    }
}
