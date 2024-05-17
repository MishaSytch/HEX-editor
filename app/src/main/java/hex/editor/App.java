package hex.editor;


import java.awt.EventQueue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hex.editor.controller.Thread.ServiceThread;
import hex.editor.controller.Thread.ViewThread;
import hex.editor.model.Types;


public class App {
    public static void main(String[] args) {
        startApp();
    }

    private static void startApp() {
        EventQueue.invokeLater(() -> setup());
    }

    private static void setup() {

        Map<Types, Exchanger<Object>> exchangers = new HashMap<>();
        {
            exchangers.put(Types.FILE, new Exchanger<>());
            exchangers.put(Types.CHARS, new Exchanger<>());
            exchangers.put(Types.HEX, new Exchanger<>());
            exchangers.put(Types.SEARCH_BY_STRING, new Exchanger<>());
            exchangers.put(Types.SEARCH_BY_HEX, new Exchanger<>());
            exchangers.put(Types.INTEGER, new Exchanger<>());
            exchangers.put(Types.UPDATE_BY_HEX, new Exchanger<>());
        }

        ViewThread view = new ViewThread(exchangers);
        Thread viewThread = new Thread(view);
        viewThread.start();
        ServiceThread service = new ServiceThread(exchangers);
        Thread serviceThread = new Thread(service);
        serviceThread.start();

        System.out.println("Main");
    }
}
