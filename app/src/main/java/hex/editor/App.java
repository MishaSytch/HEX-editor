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
        ExecutorService threadPool = Executors.newFixedThreadPool(2);

        Map<Types, Exchanger<Object>> exchangers = new HashMap<>();
        exchangers.put(Types.FILE, new Exchanger<Object>());
        exchangers.put(Types.CHARS, new Exchanger<Object>());
        exchangers.put(Types.HEX, new Exchanger<Object>());
        exchangers.put(Types.SEARCH_BY_STRING, new Exchanger<Object>());
        exchangers.put(Types.SEARCH_BY_HEX, new Exchanger<Object>());
        exchangers.put(Types.INTEGER, new Exchanger<Object>());
        exchangers.put(Types.UPDATE_BY_HEX, new Exchanger<Object>());

        ViewThread view = new ViewThread(exchangers);
        ServiceThread service = new ServiceThread(exchangers);
        threadPool.submit(service);
        System.out.println("Main");
        threadPool.submit(view);
    }
}
