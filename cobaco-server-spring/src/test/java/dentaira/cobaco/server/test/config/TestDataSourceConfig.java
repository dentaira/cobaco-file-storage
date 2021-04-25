package dentaira.cobaco.server.test.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

/**
 * DataSource for test.
 */
@Configuration
public class TestDataSourceConfig {

    @Bean
    public DataSource dataSource(DataSourceProperties properties) {
        DataSource dataSource = new DriverManagerDataSource(properties.getUrl(), properties.getUsername(), properties.getPassword());
        // Database RiderをSpring管理のトランザクションに参加させるための設定。
        return new TransactionAwareDataSourceProxy(dataSource);
    }
}
