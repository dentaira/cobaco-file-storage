package dentaira.cobaco.server.file.infra;

import dentaira.cobaco.server.file.*;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.sql.Types;
import java.util.*;

@Repository
public class JdbcFileRepository implements FileRepository {

    public static final Path ROOT_PATH = Path.of("/");

    private JdbcTemplate jdbcTemplate;

    private FileMapper fileMapper;

    public JdbcFileRepository(JdbcTemplate jdbcTemplate, FileMapper fileMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.fileMapper = fileMapper;
    }

    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }

    @Override
    public List<StoredFile> searchRoot(Owner owner) {
        return jdbcTemplate.query(
                "SELECT id, name, path, type, size " +
                        "FROM file " +
                        "WHERE cast(id as text) = replace(path, '/', '') " +
                        "AND id IN(SELECT file_id FROM file_ownership WHERE owned_at = ?)",
                ps -> ps.setObject(1, owner.getId()),
                (rs, rowNum) -> {
                    return new StoredFile(UUID.fromString(rs.getString("id"))
                            , rs.getString("name")
                            , Path.of(rs.getString("path"))
                            , FileType.valueOf(rs.getString("type"))
                            , DataSize.of(rs.getLong("size")));
                });
    }

    @Override
    public List<StoredFile> search(String parentDirId, Owner owner) {

        // TODO levelを使わない方法にリファクタリングする。
        int level = getLevel(parentDirId);

        return jdbcTemplate.query(
                "SELECT id, name, path, type, size FROM file " +
                        "WHERE path LIKE (SELECT path FROM file WHERE id = ?) || '_%' " +
                        "AND LENGTH(path) - LENGTH(REPLACE(path, '/', '')) = (? + 1) " +
                        "AND id IN(SELECT file_id FROM file_ownership WHERE owned_at = ?)",
                ps -> {
                    ps.setString(1, parentDirId);
                    ps.setInt(2, level);
                    ps.setObject(3, owner.getId());
                },
                (rs, rowNum) -> {
                    return new StoredFile(UUID.fromString(rs.getString("id"))
                            , rs.getString("name")
                            , Path.of(rs.getString("path"))
                            , FileType.valueOf(rs.getString("type"))
                            , DataSize.of(rs.getLong("size")));
                });
    }

    private int getLevel(String parentDirId) {
        return jdbcTemplate.queryForObject(
                "SELECT LENGTH(path) - LENGTH(REPLACE(path, '/', '')) FROM file WHERE id = ?",
                new Object[]{parentDirId},
                int.class);
    }

    @Override
    public StoredFile findById(String id, Owner owner) {
        return fileMapper.findById(id, owner);
    }

    @Override
    public List<StoredFile> searchForAncestors(StoredFile file) {

        if (file.getPath().getParent().equals(ROOT_PATH)) {
            return Collections.emptyList();
        }
        // パラメータとプレースホルダーを作成
        var params = new ArrayList<String>();
        var placeholders = new ArrayList<String>();
        for (Iterator<Path> itr = file.getPath().getParent().iterator(); itr.hasNext(); ) {
            Path p = itr.next();
            params.add(p.toString());
            placeholders.add("?");
        }

        // SQLを作成
        var sql = new StringBuilder("SELECT id, name, path, type, size FROM file WHERE id IN (");
        String in = StringUtils.join(placeholders, ',');
        sql.append(in).append(")");
        sql.append("ORDER BY char_length(path)");

        // SQL実行
        return jdbcTemplate.query(sql.toString(),
                ps -> {
                    for (int i = 0; i < params.size(); i++) {
                        ps.setString(i + 1, params.get(i));
                    }
                },
                (rs, rowNum) -> {
                    return new StoredFile(
                            UUID.fromString(rs.getString("id")),
                            rs.getString("name"),
                            Path.of(rs.getString("path")),
                            FileType.valueOf(rs.getString("type")),
                            DataSize.of(rs.getLong("size"))
                    );
                });
    }

    @Override
    public void save(StoredFile file) {
        jdbcTemplate.update(
                "INSERT INTO file(id, name, content, size, path, type) VALUES(?, ?, ?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, file.getId().toString());
                    ps.setString(2, file.getName());
                    ps.setBinaryStream(3, file.getContent());
                    ps.setLong(4, file.getSize().toLong());
                    ps.setString(5, file.getPath().toString() + "/");
                    ps.setObject(6, file.getType(), Types.OTHER);
                }
        );
    }

    @Override
    public void delete(StoredFile file) {
        if (file.getType() == FileType.FILE) {
            jdbcTemplate.update("DELETE FROM file WHERE LOWER(id) = LOWER(?)", file.getId().toString());
        } else {
            jdbcTemplate.update("DELETE FROM file WHERE path LIKE ? || '%'", file.getPath().toString() + "/");
        }
    }
}
