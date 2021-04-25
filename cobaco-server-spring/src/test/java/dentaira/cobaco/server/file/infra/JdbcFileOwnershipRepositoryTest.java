package dentaira.cobaco.server.file.infra;

import com.github.database.rider.core.api.dataset.DataSet;
import dentaira.cobaco.server.test.annotation.DatabaseRiderTest;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.UUID;

import static org.assertj.db.api.Assertions.assertThat;

@JdbcTest
@DatabaseRiderTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcFileOwnershipRepositoryTest {

    JdbcFileOwnershipRepository sut;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @BeforeEach
    void setUp() {
        sut = new JdbcFileOwnershipRepository(jdbcTemplate);
    }

    @Nested
    @JdbcTest
    @DatabaseRiderTest
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    @DataSet("dentaira/cobaco/server/file/infra/JdbcFileOwnershipRepositoryTest-data/setup-create.yml")
    @DisplayName("createはFileOwnershipを登録する")
    class CreateTest {

        @ParameterizedTest
        @CsvSource({
                "2c451a6f-8734-4a03-a135-a4c3ee8f5161,2c451a6f-8734-4a03-a135-a4c3ee8f5162,READ_ONLY",
                "3c451a6f-8734-4a03-a135-a4c3ee8f5161,3c451a6f-8734-4a03-a135-a4c3ee8f5162,READ_AND_WRITE",
        })
        void testCreate1(UUID fileId, UUID ownedAt, String type) {
            // given
            Table table = new Table(dataSource, "file_ownership");
            // when
            sut.create(fileId, ownedAt, type);
            // then
            assertThat(table).hasNumberOfRows(1)
                    .row(0)
                    .value("file_id").isEqualTo(fileId.toString())
                    .value("owned_at").isEqualTo(ownedAt)
                    .value("type").isEqualTo(type);
        }
    }
}