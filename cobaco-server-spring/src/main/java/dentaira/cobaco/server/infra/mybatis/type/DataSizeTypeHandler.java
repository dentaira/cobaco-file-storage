package dentaira.cobaco.server.infra.mybatis.type;

import dentaira.cobaco.server.file.DataSize;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.BIGINT)
public class DataSizeTypeHandler extends BaseTypeHandler<DataSize> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DataSize parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, parameter.toLong());
    }

    @Override
    public DataSize getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return DataSize.of(rs.getLong(columnName));
    }

    @Override
    public DataSize getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return DataSize.of(rs.getLong(columnIndex));
    }

    @Override
    public DataSize getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return DataSize.of(cs.getLong(columnIndex));
    }
}
