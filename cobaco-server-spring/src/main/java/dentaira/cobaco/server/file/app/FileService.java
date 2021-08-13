package dentaira.cobaco.server.file.app;

import dentaira.cobaco.server.file.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    final FileRepository fileRepository;

    final FileOwnershipRepository fileOwnershipRepository;

    public FileService(FileRepository fileRepository, FileOwnershipRepository fileOwnershipRepository) {
        this.fileRepository = fileRepository;
        this.fileOwnershipRepository = fileOwnershipRepository;
    }

    @Transactional(readOnly = true)
    public StoredFile findById(String fileId, Owner owner) {
        return fileRepository.findById(fileId, owner);
    }

    @Transactional(readOnly = true)
    public List<StoredFile> searchRoot(Owner owner) {
        return fileRepository.searchRoot(owner);
    }

    @Transactional(readOnly = true)
    public List<StoredFile> search(String parentId, Owner owner) {
        return fileRepository.search(parentId, owner);
    }

    @Transactional(readOnly = true)
    public StoredFile findContentById(String fileId, Owner owner) {
        var file = findById(fileId, owner);
        file.setContent(fileRepository.findContentById(fileId, owner));
        return file;
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(StoredFile file, Owner owner) {
        fileRepository.save(file);
        fileOwnershipRepository.create(file.getId(), owner.getId(), "READ_AND_WRITE");
    }

    @Transactional(readOnly = true)
    public UUID generateId() {
        return fileRepository.generateId();
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

