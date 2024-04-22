package hex.editor.controller.Thread;

import java.io.File;

import javax.swing.SwingWorker;

import hex.editor.controller.HexEditor;

public class MainSwingWorker {
    SwingWorker swingWorker;
    HexEditor hexEditor;
    String[] hex = hexEditor.getHexString();
    String[] chars = hexEditor.getCharsString();
    File file;

    public MainSwingWorker(File file) {
        swingWorker = new SwingWorker<Boolean,String[]>() {
            @Override
            protected Boolean doInBackground() throws Exception { 
                hexEditor = new HexEditor(file.getAbsolutePath());
                hex = hexEditor.getHexString();
                chars = hexEditor.getCharsString();

                return null;
            }

            @Override
            protected void done() {

            }            
        };
    }
    
}
