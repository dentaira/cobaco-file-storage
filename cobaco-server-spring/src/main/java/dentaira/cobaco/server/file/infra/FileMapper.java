package dentaira.cobaco.server.file.infra;

import dentaira.cobaco.server.file.Owner;
import dentaira.cobaco.server.file.StoredFile;
import dentaira.cobaco.server.infra.mybatis.type.ByteArrayInputStreamTypeHandler;
import dentaira.cobaco.server.infra.mybatis.type.DataSizeTypeHandler;
import dentaira.cobaco.server.infra.mybatis.type.UUIDCharTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.io.InputStream;

@Mapper
public interface FileMapper {

    @Results({
            @Result(property = "id", column = "id", id = true, typeHandler = UUIDCharTypeHandler.class),
            @Result(property = "name", column = "name"),
            @Result(property = "path", column = "path"),
            @Result(property = "type", column = "type"),
            @Result(property = "content", column = "id",
                    one = @One(select = "findContentById", fetchType = FetchType.LAZY), typeHandler = ByteArrayInputStreamTypeHandler.class),
            @Result(property = "size", column = "size", jdbcType = JdbcType.BIGINT, typeHandler = DataSizeTypeHandler.class),
    })
    @Select({"SELECT id, name, path, type, size",
            "FROM file",
            "WHERE LOWER(id) = LOWER(#{id})",
            "AND id IN(SELECT file_id FROM file_ownership",
            "WHERE owned_at = #{owner.id})"
    })
    public StoredFile findById(@Param("id") String id, @Param("owner") Owner owner);

    /**
     * content を取得する。
     * <p>
     *     Lazy Loading 用に定義。
     * </p>
     */
    @Deprecated
    @Select({"SELECT content",
            "FROM file",
            "WHERE LOWER(id) = LOWER(#{id})"
    })
    public InputStream findContentById(@Param("id") String id);
}
