package dentaira.cobaco.server.file;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface FileRepository {

    public UUID generateId();

    public List<StoredFile> searchRoot(Owner owner);

    public List<StoredFile> search(String parentDirId, Owner owner);

    public StoredFile findById(String id, Owner owner);

    public InputStream findContentById(String id, Owner owner);

    public List<StoredFile> searchForAncestors(StoredFile file);

    public void save(StoredFile file);

    public void delete(StoredFile file);
}
