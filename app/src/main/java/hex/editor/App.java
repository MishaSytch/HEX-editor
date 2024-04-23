package hex.editor;


import java.awt.EventQueue;

import java.io.File;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hex.editor.controller.Thread.ServiceThread;
import hex.editor.controller.Thread.ViewThread;


public class App {
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        EventQueue.invokeLater(() -> {
            ExecutorService threadPool = Executors.newFixedThreadPool(2);
            Exchanger<File> fileExchanger = new Exchanger<File>();
            Exchanger<String[]> dataExchanger = new Exchanger<String[]>();
            
            ViewThread view = new ViewThread(fileExchanger, dataExchanger);
            ServiceThread service = new ServiceThread(fileExchanger, dataExchanger);
            threadPool.submit(service);
            System.out.println("Main");
            threadPool.submit(view);
        });
    }
}
