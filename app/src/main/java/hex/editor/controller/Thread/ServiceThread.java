package hex.editor.controller.Thread;

import java.io.File;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        boolean isWaiting = true;
        while (true) {
            File file = null;
            String[] data = null;
            try {
                if (isWaiting)
                    System.out.println("Service: wait");

                file = fileExchanger.exchange(file, 1000, TimeUnit.MILLISECONDS);
                data = dataExchanger.exchange(data, 1000, TimeUnit.MILLISECONDS);

            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } catch (TimeoutException e) {
                isWaiting = false;
            }

            if (file != null) {
                gotFile(file);
                isWaiting = true;
            };
            if (data != null) {
                gotData(data);
                isWaiting = true;
            };
            
        }
    }    

    private void gotFile(File file) {
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

    private void gotData(String[] data) {
        try {
            System.out.println("Service: Data added");
            if (data.length == 1) {  
                data = new String[]{hexEditor.getCharFromHex(data[0])};
            } else {
                data = hexEditor.getCharsFromHex(data);
            }
            dataExchanger.exchange(data);
            System.out.println("Service: Data sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
