package dentaira.cobaco.server.file.infra;

import dentaira.cobaco.server.file.FileOwnershipRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JdbcFileOwnershipRepository implements FileOwnershipRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFileOwnershipRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(UUID fileId, UUID ownedAt, String type) {
        jdbcTemplate.update(
                "INSERT INTO file_ownership(file_id, owned_at, type) VALUES(?, ?, ?)"
                , (ps) -> {
                    ps.setString(1, fileId.toString());
                    ps.setObject(2, ownedAt);
                    ps.setString(3, type);
                }
        );
    }
}
