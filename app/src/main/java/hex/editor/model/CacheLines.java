package hex.editor.model;

import java.nio.charset.StandardCharsets;
import java.util.List;

import hex.editor.services.FileWriter;

public class CacheLines {
    private List<List<String>> data;
    private boolean isModified;
    private boolean isSaved;
    private int part;
    
    
    public CacheLines(List<List<String>> data, int part) {
        this.data = data;
        this.part = part;
        isModified = false;
        isSaved = false;
    }
    
    public List<List<String>> getData() {
        return data;
    }
    
    public void updateData(List<List<String>> data) {
        this.data = data;
        isModified = true;
        isSaved = false;
    }
    
    public int getLength() {
        return getSize() * (data.get(0).get(0).length() + FileWriter.getSeparator().getBytes(StandardCharsets.UTF_8).length);  
    }
    
    public long getNextIndex() {
        return part * getLength();
    }

    public long getPreviousIndex() {
        return Math.max(0, (part - 2) * getLength());
    }
    
    public long getIndex() {
        return (part - 1) * getLength();
    }
    
    public boolean isModified() {
        return isModified;
    }
    
    public boolean isSaved() {
        return isSaved;
    }
    
    public void wasSaved() {
        isSaved = true;
        isModified = false;
    }
    
    public int getPart() {
        return part;
    }

    public int getSize() {
        return data.get(0).size() * data.size();
    }
   
}
