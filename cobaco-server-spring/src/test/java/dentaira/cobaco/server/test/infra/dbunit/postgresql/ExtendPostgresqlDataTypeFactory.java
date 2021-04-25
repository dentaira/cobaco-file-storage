package dentaira.cobaco.server.test.infra.dbunit.postgresql;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

import java.sql.Types;

public class ExtendPostgresqlDataTypeFactory extends PostgresqlDataTypeFactory {

    @Override
    public boolean isEnumType(String sqlTypeName) {
        return "file_type".equalsIgnoreCase(sqlTypeName);
    }

    @Override
    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        if (isEnumType(sqlTypeName)) {
            return super.createDataType(Types.OTHER, sqlTypeName);
        }
        return super.createDataType(sqlType, sqlTypeName);
    }
}
