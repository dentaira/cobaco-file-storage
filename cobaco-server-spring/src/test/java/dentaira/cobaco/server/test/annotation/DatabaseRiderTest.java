package dentaira.cobaco.server.test.annotation;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.junit5.api.DBRider;
import dentaira.cobaco.server.test.config.TestDataSourceConfig;
import dentaira.cobaco.server.test.infra.dbunit.postgresql.ExtendPostgresqlDataTypeFactory;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DBRider
@DBUnit(cacheConnection = false,
        caseInsensitiveStrategy = Orthography.LOWERCASE,
        dataTypeFactoryClass = ExtendPostgresqlDataTypeFactory.class)
@ImportAutoConfiguration(TestDataSourceConfig.class)
public @interface DatabaseRiderTest {
}
