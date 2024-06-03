package hex.editor.model;

import java.util.List;
import java.nio.file.Path;

public class CacheFile {
    private final long numberOfFirstRow;
    private List<List<String>> data;
    private boolean isModified = false;
    private final Path path;
    private final long index;

    public CacheFile(long numberOfFirstRow, List<List<String>> data, Path path, long index) {
        this.numberOfFirstRow = numberOfFirstRow;
        this.data = data;
        this.path = path;
        this.index = index;
    }

    public long getNumberOfFirstRow() {
        return numberOfFirstRow;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void updateData(List<List<String>> data) {
        isModified = true;
        this.data = data;
    }

    public boolean isModified() {
        return isModified;
    }

    public Path getPath() {
        return path;
    }

    public long getIndex() {
        return index;
    }
}
