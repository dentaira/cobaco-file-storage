package dentaira.cobaco.server.file.web.api;

import dentaira.cobaco.server.file.*;
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

    private FileRepository fileRepository;

    public FileRestController(FileService fileService, FileRepository fileRepository) {
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }

    @GetMapping("api/folder/root")
    public FolderResource getRootFolder(Owner owner) {

        List<StoredFile> children = fileRepository.searchRoot(owner);
        StoredFile root = new StoredFile(UUID.randomUUID(), "home", Path.of("/"), FileType.DIRECTORY, DataSize.of(0L));

        List<FileResource> fileResources = children.stream().map(FileResource::of).collect(Collectors.toList());
        return FolderResource.of(root, fileResources);
    }

    @GetMapping("api/folder/{fileId}")
    public FolderResource getFolder(@PathVariable String fileId, Owner owner) {

        StoredFile folder = fileRepository.findById(fileId, owner);
        List<StoredFile> children = fileRepository.search(fileId, owner);
        List<StoredFile> ancestors = fileService.findAncestors(fileId, owner);

        List<FileResource> fileResources = children.stream().map(FileResource::of).collect(Collectors.toList());
        return FolderResource.of(folder, fileResources, ancestors);
    }

    @GetMapping("api/file/{fileId}")
    public Resource downloadFile(@PathVariable String fileId, Owner owner, HttpServletResponse response) {

        StoredFile file = fileRepository.findById(fileId, owner);

        String filename = URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        return new InputStreamResource(file.getContent());
    }

    @PutMapping("api/file")
    public FileResource upload(@RequestParam MultipartFile uploadFile, @RequestParam(required = false) String parentId, Owner owner) {

        UUID fileId = fileRepository.generateId();

        Path path = null;
        if (parentId != null) {
            StoredFile parent = fileRepository.findById(parentId, owner);
            path = parent.getPath().resolve(fileId.toString());
        } else {
            path = Path.of("/").resolve(fileId.toString());
        }

        try (InputStream in = uploadFile.getInputStream()) {

            var file = new StoredFile(
                    fileId,
                    uploadFile.getOriginalFilename(),
                    path,
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

    @PutMapping("api/folder/{name}")
    public void createFolder(@PathVariable String name, @RequestParam(required = false) String parentId, Owner owner) {
        fileService.createFolder(name, parentId, owner);
    }

    @DeleteMapping("api/file/{fileId}")
    public void delete(@PathVariable String fileId, Owner owner) {
        fileService.delete(fileId, owner);
    }
}
