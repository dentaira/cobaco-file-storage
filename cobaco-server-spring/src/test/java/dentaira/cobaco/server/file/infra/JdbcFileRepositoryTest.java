package dentaira.cobaco.server.file.infra;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import dentaira.cobaco.server.file.DataSize;
import dentaira.cobaco.server.file.FileType;
import dentaira.cobaco.server.file.Owner;
import dentaira.cobaco.server.file.StoredFile;
import dentaira.cobaco.server.test.annotation.DatabaseRiderTest;
import dentaira.cobaco.server.test.builder.TestStoredFileBuilder;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.AutoConfigureMybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureMybatis
@DatabaseRiderTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JdbcFileRepositoryTest {

    @Autowired
    DataSource dataSource;

    JdbcFileRepository sut;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    FileMapper fileMapper;

    @BeforeEach
    void setUp() {
        sut = new JdbcFileRepository(jdbcTemplate, fileMapper);
    }

    @Nested
    @DisplayName("searchRoot(UserAccount)はUserAccountが所有するRoot直下のFileを取得する")
    class SearchRootTest {

        @Test
        @DataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/SearchRootTest/setup-testFindOne.yml")
        @DisplayName("Root配下のFileが1つの場合は1つ取得する")
        void testFindOne() {
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11"));
            List<StoredFile> actual = sut.searchRoot(owner);
            assertEquals(1, actual.size());
        }

        @Test
        @DataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/SearchRootTest/setup-testFindThree.yml")
        @DisplayName("Root配下のFileが3つの場合は3つ取得する")
        void testFindThree() {
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13"));
            List<StoredFile> actual = sut.searchRoot(owner);
            assertEquals(3, actual.size());
        }
    }

    @Nested
    @DataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/SearchTest/setup-search.yml")
    @DisplayName("search(String,UserAccount)はUserAccountが所有する指定したFolder直下にあるFileを取得する")
    class SearchTest {

        @Test
        @DisplayName("指定したFolder配下のFileが1つの場合は1つ取得する")
        void testFindOne() {
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13"));
            List<StoredFile> actual = sut.search("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B13", owner);
            assertEquals(1, actual.size());
        }

        @Test
        @DisplayName("指定したFolder配下のFileが3つの場合は3つ取得する")
        void testFindThree() {
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A14"));
            List<StoredFile> actual = sut.search("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11", owner);
            assertEquals(3, actual.size());
        }

        @Test
        @DisplayName("指定したFolder配下にFileが存在しない場合は空のListを返す")
        void testFindZero() {
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13"));
            List<StoredFile> actual = sut.search("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B12", owner);
            assertEquals(0, actual.size());
        }

        @Test
        @DisplayName("指定したFileのtypeがFileだった場合は例外が発生する")
        void testSearchFile() {
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13"));
            List<StoredFile> actual = sut.search("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12", owner);
            assertEquals(0, actual.size());
        }
    }

    @Nested
    @DataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/FindByIdTest/setup-findById.yml")
    @ExtendWith(SoftAssertionsExtension.class)
    @DisplayName("findById(String,Owner)はOwnerが所有するidが一致するFileを取得する")
    class FindByIdTest {

        @Test
        void testFindOneDirectory(SoftAssertions softly) {
            // given
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13"));
            UUID fileId = UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11");
            // when
            StoredFile actual = sut.findById(fileId.toString(), owner);
            // then
            softly.assertThat(actual.getId()).isEqualTo(fileId);
            softly.assertThat(actual.getName()).isEqualTo("フォルダ１");
            softly.assertThat(actual.getPath()).isEqualTo(Path.of("/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11/"));
            softly.assertThat(actual.getType()).isEqualTo(FileType.DIRECTORY);
            softly.assertThat(actual.getSize()).isEqualTo(DataSize.of(0L));
            softly.assertThat(actual.getContent()).isNull();
        }

        @Test
        void testFindOneFile(SoftAssertions softly) throws Exception {
            // given
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A14"));
            UUID fileId = UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B11");
            // when
            StoredFile actual = sut.findById(fileId.toString(), owner);
            // then
            softly.assertThat(actual.getId()).isEqualTo(fileId);
            softly.assertThat(actual.getName()).isEqualTo("ファイル４");
            softly.assertThat(actual.getPath()).isEqualTo(Path.of("/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B11/"));
            softly.assertThat(actual.getType()).isEqualTo(FileType.FILE);
            softly.assertThat(actual.getSize()).isEqualTo(DataSize.of(3L));
            try (var in = actual.getContent()) {
                softly.assertThat(in).hasContent("file4 content");
            }
        }

        @Test
        void testUserNotMatched() {
            // given
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13"));
            UUID fileId = UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B11");
            // when
            StoredFile actual = sut.findById(fileId.toString(), owner);
            // then
            assertThat(actual).isNull();
        }

        @Test
        void testFileNotFound() {
            // given
            var owner = new Owner(UUID.fromString("B0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A13"));
            UUID fileId = UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B99");
            // when
            StoredFile actual = sut.findById(fileId.toString(), owner);
            // then
            assertThat(actual).isNull();
        }
    }

    @Nested
    @DataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/SearchForAncestorsTest/setup-searchForAncestors.yml")
    @DisplayName("searchForAncestorsは祖先フォルダ全てのListを返す")
    class SearchForAncestorsTest {

        @Test
        void testFindTwo() {
            // given
            Path path = Path.of("/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B12/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B13/");
            StoredFile file = new TestStoredFileBuilder().withPath(path).build();
            // when
            List<StoredFile> actual = sut.searchForAncestors(file);
            // then
            assertEquals(2, actual.size());
            assertEquals(UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11"), actual.get(0).getId());
            assertEquals(UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380B12"), actual.get(1).getId());
        }

        @Test
        void whenFileUnderRootThenReturnEmptyList() {
            // given
            Path path = Path.of("/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11/");
            StoredFile file = new TestStoredFileBuilder().withPath(path).build();
            // when
            List<StoredFile> actual = sut.searchForAncestors(file);
            // then
            assertEquals(0, actual.size());
        }
    }

    @Nested
    @DataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/SaveTest/setup-save.yml")
    @DisplayName("saveはStoredFileを登録する")
    class SaveTest {

        @Test
        @ExpectedDataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/SaveTest/expected-testSaveBible.yml")
        void testSaveBible() {
            // given
            StoredFile file = new TestStoredFileBuilder()
                    .withId(UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11"))
                    .withName("Bible")
                    .withPath(Path.of("/parent/" + UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11").toString() + "/"))
                    .withType(FileType.FILE)
                    .withSize(DataSize.of(3L))
                    .withContent(new ByteArrayInputStream("content".getBytes(StandardCharsets.UTF_8)))
                    .build();
            // when
            sut.save(file);
        }

        @Test
        @ExpectedDataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/SaveTest/expected-testSavePandora.yml")
        void testSavePandora() {
            // given
            StoredFile file = new TestStoredFileBuilder()
                    .withId(UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12"))
                    .withName("Pandora")
                    .withPath(Path.of("/parent/" + UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12").toString() + "/"))
                    .withType(FileType.DIRECTORY)
                    .withSize(DataSize.of(0L))
                    .build();
            // when
            sut.save(file);
        }
    }

    @Nested
    @DataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/DeleteTest/setup-delete.yml")
    @DisplayName("deleteはStoredFileを削除する")
    class DeleteTest {

        @Test
        @ExpectedDataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/DeleteTest/expected-testDeleteFile.yml")
        @DisplayName("削除するFileのtypeがFileの場合は指定したFileのみ削除する")
        void testDeleteFile() {
            // given
            StoredFile file = new TestStoredFileBuilder()
                    .withId(UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A12"))
                    .withType(FileType.FILE)
                    .build();
            // when
            sut.delete(file);
        }

        @Test
        @ExpectedDataSet("dentaira/cobaco/server/file/infra/JdbcFileRepositoryTest-data/DeleteTest/expected-testDeleteFolder.yml")
        @DisplayName("削除するFileのtypeがFolderの場合は指定したFileと配下のFile全てを削除する")
        void testDeleteFolder() {
            // given
            StoredFile file = new TestStoredFileBuilder()
                    .withId(UUID.fromString("A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11"))
                    .withPath(Path.of("/A0EEBC99-9C0B-4EF8-BB6D-6BB9BD380A11/"))
                    .withType(FileType.DIRECTORY)
                    .build();
            // when
            sut.delete(file);
        }
    }
}