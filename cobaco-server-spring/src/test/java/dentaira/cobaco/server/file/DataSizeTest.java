package dentaira.cobaco.server.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataSizeTest {

    @ParameterizedTest
    @CsvSource({
            "1,             1B",
            "2,             2B",
            "1023,          1023B",
            "1024,          1KB",
            "1047552,       1023KB",
            "1048575,       1023KB", // TODO 1MB-1B 切り捨てより切り上げの方が良いのでは
            "1048576,       1MB",
            "1073741823,    1023MB",
            "1073741824,    1GB",
    })
    @DisplayName("toStringは単位を計算してフォーマットした文字列を返す")
    public void testToString(long size, String expected) {
        assertEquals(expected, DataSize.of(size).toString());
    }
}