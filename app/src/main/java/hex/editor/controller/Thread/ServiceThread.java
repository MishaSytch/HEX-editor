package hex.editor.controller.Thread;

import java.io.File;
import java.util.concurrent.Exchanger;
import hex.editor.controller.HexEditor;

public class ServiceThread implements Runnable {
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
            File file = null;
            String[] data = null;
            try {
                System.out.println("Service: wait");
                file = fileExchanger.exchange(file);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            if (file != null) {
                try {
                    System.out.println("Service: File added");
                    hexEditor = new HexEditor(file.getAbsolutePath());
    
                    
                    System.out.println("Service: Data to hex");
                    String[] hex = hexEditor.getHexString();
                    dataExchanger.exchange(hex);
                    System.out.println("Service: Hex sent");
                } catch (InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }    
}
