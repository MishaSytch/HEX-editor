package hex.editor.controller.Thread;

import java.io.File;
import java.util.concurrent.Exchanger;

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
                file = fileExchanger.exchange(file);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            if (file != null) {
                try {
                    hexEditor = new HexEditor(file.getAbsolutePath());


                    String[] hex = hexEditor.getHexString();
                    dataExchanger.exchange(hex);

                    String[] chars = hexEditor.getCharsString();
                    dataExchanger.exchange(chars);
                } catch (InterruptedException e) {
                   System.err.println(e.getMessage());
                }
            }
            break;
        }
        System.out.println("e.getMessage()");
    }
    
}
