package dentaira.cobaco.server.file;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileService {

    final FileRepository fileRepository;

    final FileOwnershipRepository fileOwnershipRepository;

    public FileService(FileRepository fileRepository, FileOwnershipRepository fileOwnershipRepository) {
        this.fileRepository = fileRepository;
        this.fileOwnershipRepository = fileOwnershipRepository;
    }

    @Transactional(readOnly = true)
    public List<StoredFile> searchRoot(Owner owner) {
        return fileRepository.searchRoot(owner);
    }

    @Transactional(readOnly = true)
    public List<StoredFile> search(String parentId, Owner owner) {
        return fileRepository.search(parentId, owner);
    }

    @Transactional(rollbackFor = Exception.class)
    public void register(MultipartFile multipartFile, Path parentPath, Owner owner) {
        // TODO MultipartFileに依存しないようにする

        try (InputStream in = multipartFile.getInputStream()) {
            var fileId = fileRepository.generateId();
            var file = new StoredFile(
                    fileId,
                    multipartFile.getOriginalFilename(),
                    parentPath.resolve(fileId.toString()),
                    FileType.FILE,
                    DataSize.of(multipartFile.getSize())
            );
            file.setContent(in);

            fileRepository.save(file);
            fileOwnershipRepository.create(fileId, owner.getId(), "READ_AND_WRITE");

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Transactional(readOnly = true)
    public StoredFile findById(String fileId, Owner owner) {
        return fileRepository.findById(fileId, owner);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createDirectory(String name, Path parentPath, Owner owner) {
        var fileId = fileRepository.generateId();
        var file = new StoredFile(
                fileId,
                name,
                parentPath.resolve(fileId.toString()),
                FileType.DIRECTORY,
                DataSize.of(0L)
        );

        fileRepository.save(file);
        fileOwnershipRepository.create(fileId, owner.getId(), "READ_AND_WRITE");
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String fileId, Owner owner) {
        StoredFile file = findById(fileId, owner);
        fileRepository.delete(file);
    }

    @Transactional(readOnly = true)
    public List<StoredFile> findAncestors(String fileId, Owner owner) {
        var file = findById(fileId, owner);
        return fileRepository.searchForAncestors(file);
    }
}

