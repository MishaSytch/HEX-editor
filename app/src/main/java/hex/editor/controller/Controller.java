package hex.editor.controller;

import java.io.File;
import java.util.concurrent.Exchanger;

import hex.editor.controller.Thread.ServiceThread;
import hex.editor.controller.Thread.ViewThread;

public class Controller {

    public Controller() {
        Exchanger<File> fileExchanger = new Exchanger<File>();
        Exchanger<String[]> dataExchanger = new Exchanger<String[]>();
        ViewThread view = new ViewThread();
        ServiceThread service = new ServiceThread(fileExchanger, dataExchanger);
    }
}
