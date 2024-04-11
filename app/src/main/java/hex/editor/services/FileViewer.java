package hex.editor.services;

import java.io.File;
import java.util.Scanner;

public class FileViewer {
    private File file;
    private Scanner scanner;
    private String path;


    public FileViewer(String path) {
        this.path = path;
        file = new File(path);
    }

    public String getLine(int from, int to) {
        
    }


}
