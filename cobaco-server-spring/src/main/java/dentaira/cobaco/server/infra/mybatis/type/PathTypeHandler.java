package dentaira.cobaco.server.infra.mybatis.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PathTypeHandler extends BaseTypeHandler<Path> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Path parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString() + "/");
    }

    @Override
    public Path getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Path.of(rs.getString(columnName));
    }

    @Override
    public Path getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Path.of(rs.getString(columnIndex));
    }

    @Override
    public Path getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Path.of(cs.getString(columnIndex));
    }
}
