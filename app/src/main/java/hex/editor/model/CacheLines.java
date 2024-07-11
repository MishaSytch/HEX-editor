package hex.editor.model;

import java.nio.charset.StandardCharsets;
import java.util.List;

import hex.editor.services.FileWriter;

public class CacheLines {
    private List<List<String>> data;
    private boolean isModified;
    private final int part;
    
    
    public CacheLines(List<List<String>> data, int part) {
        this.data = data;
        this.part = part;
        isModified = false;
    }
    
    public List<List<String>> getData() {
        return data;
    }
    
    public void updateData(List<List<String>> data) {
        this.data = data;
        isModified = true;
    }
    
    public int getLength() {
        return getSize() * (2 + FileWriter.getSeparator().getBytes(StandardCharsets.UTF_8).length);  
    }
    
    public long getNextIndex() {
        return (long) part * getLength();
    }

    public long getPreviousIndex() {
        return Math.max(0, (part - 2) * getLength());
    }
    
    public long getIndex() {
        return (long) (part - 1) * getLength();
    }
    
    public boolean isModified() {
        return isModified;
    }
    
    public int getPart() {
        return part;
    }

    public int getSize() {
        return data.get(0).size() * data.size();
    }
   
}
