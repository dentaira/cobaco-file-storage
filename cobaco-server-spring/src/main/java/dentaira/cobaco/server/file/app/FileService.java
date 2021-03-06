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

    @Transactional(rollbackFor = Exception.class)
    public void save(StoredFile file, Owner owner) {
        fileRepository.save(file);
        fileOwnershipRepository.create(file.getId(), owner.getId(), "READ_AND_WRITE");
    }

    @Transactional(rollbackFor = Exception.class)
    public StoredFile createFolder(String name, String parentId, Owner owner) {

        UUID fileId = fileRepository.generateId();

        Path parentPath;
        if (parentId == null) {
            parentPath = Path.of("/");
        } else {
            StoredFile parent = fileRepository.findById(parentId, owner);
            parentPath = parent.getPath();
        }

        var folder = new StoredFile(
                fileId,
                name,
                parentPath.resolve(fileId.toString()),
                FileType.DIRECTORY,
                DataSize.of(0L)
        );

        save(folder, owner);

        return folder;
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String fileId, Owner owner) {
        StoredFile file = fileRepository.findById(fileId, owner);
        fileRepository.delete(file);
    }

    @Transactional(readOnly = true)
    public List<StoredFile> findAncestors(String fileId, Owner owner) {
        var file = fileRepository.findById(fileId, owner);
        return fileRepository.searchForAncestors(file);
    }
}

