package hex.editor.model;

import java.util.List;
import java.nio.file.Path;

public class CacheFile {
    private final long numberOfFirstRow;
    private final List<List<String>> data;
    private boolean isModified = false;
    private final Path path;
    private long inxed;

    public CacheFile(long numberOfFirstRow, List<List<String>> data, Path path, long index) {
        this.numberOfFirstRow = numberOfFirstRow;
        this.data = data;
        this.path = path;
        this.inxed = index;
    }

    public long getNumberOfFirstRow() {
        return numberOfFirstRow;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void wasModified() {
        isModified = true;
    }

    public boolean isModified() {
        return isModified;
    }

    public Path getPath() {
        return path;
    }

    public long getInxed() {
        return inxed;
    }
}
