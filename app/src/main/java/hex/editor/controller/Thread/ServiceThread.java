package hex.editor.controller.Thread;

import java.io.File;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hex.editor.controller.HexEditor;

public class ServiceThread implements Runnable {
    private File file = null;
    private Exchanger<File> fileExchanger;
    private Exchanger<String[]> dataExchanger;
    private HexEditor hexEditor;

    public ServiceThread(Exchanger<File> fileExchanger, Exchanger<String[]> dataExchanger) {
        this.fileExchanger = fileExchanger;
        this.dataExchanger = dataExchanger;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Service wait file");
                file = fileExchanger.exchange(file);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            if (file != null) {
                try {
                    System.out.println("File added");
                    hexEditor = new HexEditor(file.getAbsolutePath());
    
                    
                    System.out.println("Data to hex");
                    String[] hex = hexEditor.getHexString();
                    dataExchanger.exchange(hex);
                    System.out.println("Hex sent");
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }
    
}
