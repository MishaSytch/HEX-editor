package hex.editor.model;

import java.util.List;
import java.io.File;
import java.nio.file.Path;

public class CacheFile {
    private final long numberOfFirstRow;
    private List<List<String>> data;
    private boolean isModified = false;
    private final Path path;
    private final long index;
    private final File file;

    public File getFile() {
        return file;
    }

    public CacheFile(long numberOfFirstRow, long index, List<List<String>> data, Path path, File file) {
        this.numberOfFirstRow = numberOfFirstRow;
        this.data = data;
        this.path = path;
        this.index = index;
        this.file = file;
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
