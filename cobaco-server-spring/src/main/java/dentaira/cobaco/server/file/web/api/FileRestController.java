package dentaira.cobaco.server.file.web.api;

import dentaira.cobaco.server.file.DataSize;
import dentaira.cobaco.server.file.FileType;
import dentaira.cobaco.server.file.Owner;
import dentaira.cobaco.server.file.StoredFile;
import dentaira.cobaco.server.file.app.FileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("api/folder/root")
    public FolderResource getRootFolder(Owner owner) {

        List<StoredFile> children = fileService.searchRoot(owner);
        StoredFile root = new StoredFile(UUID.randomUUID(), "home", Path.of("/"), FileType.DIRECTORY, DataSize.of(0L));

        List<FileResource> fileResources = children.stream().map(FileResource::of).collect(Collectors.toList());
        return FolderResource.of(root, fileResources);
    }

    @GetMapping("api/folder/{fileId}")
    public FolderResource getFolder(@PathVariable String fileId, Owner owner) {

        StoredFile folder = fileService.findById(fileId, owner);
        List<StoredFile> children = fileService.search(fileId, owner);
        List<StoredFile> ancestors = fileService.findAncestors(fileId, owner);

        List<FileResource> fileResources = children.stream().map(FileResource::of).collect(Collectors.toList());
        return FolderResource.of(folder, fileResources, ancestors);
    }

    @GetMapping("api/file/download/{fileId}")
    public Resource downloadFile(@PathVariable String fileId, Owner owner, HttpServletResponse response) {

        StoredFile file = fileService.findById(fileId, owner);

        String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        return new InputStreamResource(file.getContent());
    }

    @PostMapping("api/file/upload/{parentId}")
    public FileResource upload(@RequestParam MultipartFile uploadFile, @PathVariable String parentId, Owner owner) {

        try (InputStream in = uploadFile.getInputStream()) {

            UUID fileId = fileService.generateId();
            StoredFile parent = fileService.findById(parentId, owner);

            var file = new StoredFile(
                    fileId,
                    uploadFile.getOriginalFilename(),
                    parent.getPath().resolve(fileId.toString()),
                    FileType.FILE,
                    DataSize.of(uploadFile.getSize())
            );
            file.setContent(in);

            fileService.save(file, owner);

            return FileResource.of(file);

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }

    @PostMapping("api/folder/create/{name}")
    public void createFolder(@PathVariable String name, @RequestParam(required = false) String parentId, Owner owner) {
        fileService.createFolder(name, parentId, owner);
    }
}
