package hex.editor.model;

import java.util.List;

public class CacheLines {
    private List<List<String>> data;
    private long index;
    private boolean isModified;
    private boolean isSaved;
    private int part;
    
    public CacheLines(List<List<String>> data, long index, int part) {
        this.data = data;
        this.index = index;
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
    
    public long getIndex() {
        return index;
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

    public long getLength() {
        return data.get(0).size() * data.size();
    }
   
}
