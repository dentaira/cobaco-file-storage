package dentaira.cobaco.server.file;

import java.util.Objects;

public class DataSize {

    private static final long KILOBYTE = 1024L;
    private static final long MEGABYTE = KILOBYTE * KILOBYTE;
    private static final long GIGABYTE = MEGABYTE * KILOBYTE;

    private long value;

    private DataSize(long value) {
        this.value = value;
    }

    public static DataSize of(long value) {
        return new DataSize(value);
    }

    public long toLong() {
        return value;
    }

    @Override
    public String toString() {
        // TODO 少数桁まで出力する？
        if (value < KILOBYTE) {
            return value + "B";
        } else if (KILOBYTE <= value && value < MEGABYTE) {
            return value / KILOBYTE + "KB";
        } else if (MEGABYTE <= value && value < GIGABYTE) {
            return value / MEGABYTE + "MB";
        } else {
            return value / GIGABYTE + "GB";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSize dataSize = (DataSize) o;
        return value == dataSize.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
