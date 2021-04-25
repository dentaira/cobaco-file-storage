package dentaira.cobaco.server.test.builder;


import dentaira.cobaco.server.file.DataSize;
import dentaira.cobaco.server.file.FileType;
import dentaira.cobaco.server.file.StoredFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.UUID;

public class TestStoredFileBuilder {

    private UUID id = UUID.randomUUID();

    private String name = "default-name";

    private Path path = Path.of("default-path");

    private FileType type = FileType.FILE;

    private InputStream content;

    private DataSize size = DataSize.of(0L);

    public StoredFile build() {
        var file = new StoredFile(id, name, path, type, size);
        file.setContent(content);
        return file;
    }

    public TestStoredFileBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public TestStoredFileBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TestStoredFileBuilder withPath(Path path) {
        this.path = path;
        return this;
    }

    public TestStoredFileBuilder withType(FileType type) {
        this.type = type;
        return this;
    }

    public TestStoredFileBuilder withContent(InputStream content) {
        this.content = content;
        return this;
    }

    public TestStoredFileBuilder withSize(DataSize size) {
        this.size = size;
        return this;
    }
}
