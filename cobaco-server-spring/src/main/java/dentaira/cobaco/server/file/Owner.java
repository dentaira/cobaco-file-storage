package dentaira.cobaco.server.file;

import java.util.UUID;

public class Owner {

    private UUID id;

    public Owner(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
