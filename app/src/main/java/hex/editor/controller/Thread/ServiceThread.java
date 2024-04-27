package hex.editor.controller.Thread;

import java.io.File;
import java.util.Map;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import hex.editor.controller.HexEditor;
import hex.editor.model.Types;

public class ServiceThread implements Runnable {
    private Exchanger<Object> fileExchanger;
    private Exchanger<Object> charsExchanger;
    private Exchanger<Object> hexExchanger;
    private Exchanger<Object> SEARCH_BY_STRING_Exchanger;
    private Exchanger<Object> SEARCH_BY_HEX_Exchanger;
    private Exchanger<Object> integerExchanger;

    private HexEditor hexEditor;

    public ServiceThread(Map<Types, Exchanger<Object>> exchangers) {
        this.fileExchanger = exchangers.get(Types.FILE);
        this.charsExchanger = exchangers.get(Types.CHARS);
        this.hexExchanger = exchangers.get(Types.HEX);
        this.SEARCH_BY_STRING_Exchanger = exchangers.get(Types.SEARCH_BY_STRING);
        this.SEARCH_BY_HEX_Exchanger = exchangers.get(Types.SEARCH_BY_HEX);
        this.integerExchanger = exchangers.get(Types.INTEGER);
    }

    @Override
    public void run() {
        boolean isWaiting = true;
        while (true) {
            File file = null;
            String[] hex = null;
            String[] chars = null;
            String SEARCH_BY_STRING = null;
            String[] SEARCH_BY_HEX = null;
            try {
                if (isWaiting)
                    System.out.println("Service: wait");
                isWaiting = false;

                try {
                    file = (File)fileExchanger.exchange(null, 10, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                }

                if (file != null) {
                    gotFile(file);
                    isWaiting = true;
                };

                try {
                    hex = (String[])hexExchanger.exchange(null, 10, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                }

                if (hex != null) {
                    gotHex(hex);
                    isWaiting = true;
                };

                try {
                    chars = (String[])charsExchanger.exchange(null, 10, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                }

                if (chars != null) {
                    gotChars(chars);
                    isWaiting = true;
                }

                try {
                    SEARCH_BY_STRING = (String)SEARCH_BY_STRING_Exchanger.exchange(null, 10, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                }

                if (SEARCH_BY_STRING != null) {
                    gotSEARCH_BY_STRING(SEARCH_BY_STRING);
                    isWaiting = true;
                }

                try {
                    SEARCH_BY_HEX = (String[])SEARCH_BY_HEX_Exchanger.exchange(null, 10, TimeUnit.MILLISECONDS);
                } catch (TimeoutException e) {
                }

                if (SEARCH_BY_HEX != null) {
                    gotSEARCH_BY_HEX(SEARCH_BY_HEX);
                    isWaiting = true;
                }

            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }    

    private void gotFile(File file) {
        try {
            System.out.println("Service: File added");
            hexEditor = new HexEditor(file.getAbsolutePath());

            System.out.println("Service: File to Hex");
            String[] hex = hexEditor.getHexString();
            hexExchanger.exchange(hex);
            System.out.println("Service: Hex sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void gotHex(String[] data) {
        try {
            System.out.println("Service: Hex added");
            if (data.length == 1) {  
                data = new String[]{hexEditor.getCharFromHex(data[0])};
            } else {
                data = hexEditor.getCharsFromHex(data);
            }
            charsExchanger.exchange(data);
            System.out.println("Service: Chars sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void gotChars(String[] data) {
        try {
            System.out.println("Service: Chars added");
            if (data.length == 1) {  
                data = new String[]{hexEditor.getHexFromChar(data[0])};
            } else {
                data = hexEditor.getHexFromChars(data);
            }
            hexExchanger.exchange(data);
            System.out.println("Service: Hex sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void gotSEARCH_BY_STRING(String mask) {
        try {
            System.out.println("Service: mask added");
            
            Integer[] pos = hexEditor.findByMask(mask);

            integerExchanger.exchange(pos);

            System.out.println("Service: Position sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void gotSEARCH_BY_HEX(String[] hex) {
        try {
            System.out.println("Service: hex for search added");
            
            Integer[] pos = hexEditor.find(hex);

            integerExchanger.exchange(pos);

            System.out.println("Service: Position sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
