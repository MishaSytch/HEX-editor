package hex.editor.controller.Thread;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import hex.editor.controller.HexEditor;
import hex.editor.model.Types;

public class ServiceThread implements Runnable {
    private final Exchanger<Object> fileExchanger;
    private final Exchanger<Object> charsExchanger;
    private final Exchanger<Object> hexExchanger;
    private final Exchanger<Object> SEARCH_BY_STRING_Exchanger;
    private final Exchanger<Object> SEARCH_BY_HEX_Exchanger;
    private final Exchanger<Object> integerExchanger;
    private final Exchanger<Object> UPDATE_BY_HEXExchanger;
    private boolean close = false;

    private HexEditor hexEditor;

    public ServiceThread(Map<Types, Exchanger<Object>> exchangers) {
        this.fileExchanger = exchangers.get(Types.FILE);
        this.charsExchanger = exchangers.get(Types.CHARS);
        this.hexExchanger = exchangers.get(Types.HEX);
        this.SEARCH_BY_STRING_Exchanger = exchangers.get(Types.SEARCH_BY_STRING);
        this.SEARCH_BY_HEX_Exchanger = exchangers.get(Types.SEARCH_BY_HEX);
        this.integerExchanger = exchangers.get(Types.INTEGER);
        this.UPDATE_BY_HEXExchanger = exchangers.get(Types.UPDATE_BY_HEX);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        boolean isWaiting = true;
        while (true) {
            if (close) return;

            File file = null;
            List<List<String>> hex = null;
            List<List<String>> chars = null;
            String SEARCH_BY_STRING = null;
            List<String> SEARCH_BY_HEX = null;
            List<List<String>> UPDATE_BY_HEX = null;
            try {
                if (isWaiting)
                    System.out.println("Service: wait");
                isWaiting = false;

                try {
                    file = (File)fileExchanger.exchange(null, 1, TimeUnit.MILLISECONDS);
                } catch (TimeoutException ignored) {
                }

                if (file != null) {
                    gotFile(file);
                    isWaiting = true;
                }

                if (hexEditor != null) {
                    try {
                        hex = (List<List<String>>)hexExchanger.exchange(null, 1, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException ignored) {
                    }

                    if (hex != null) {
                        gotHex(hex);
                        isWaiting = true;
                    }

                    try {
                        chars = (List<List<String>>)charsExchanger.exchange(null, 1, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException ignored) {
                    }

                    if (chars != null) {
                        gotChars(chars);
                        isWaiting = true;
                    }

                    try {
                        SEARCH_BY_STRING = (String)SEARCH_BY_STRING_Exchanger.exchange(null, 1, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException ignored) {
                    }

                    if (SEARCH_BY_STRING != null) {
                        gotSEARCH_BY_STRING(SEARCH_BY_STRING);
                        isWaiting = true;
                    }

                    try {
                        SEARCH_BY_HEX = (List<String>)SEARCH_BY_HEX_Exchanger.exchange(null, 1, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException ignored) {
                    }

                    if (SEARCH_BY_HEX != null) {
                        gotSEARCH_BY_HEX(SEARCH_BY_HEX);
                        isWaiting = true;
                    }

                    try {
                        UPDATE_BY_HEX = (List<List<String>>)UPDATE_BY_HEXExchanger.exchange(null, 1, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException ignored) {
                    }

                    if (UPDATE_BY_HEX != null) {
                        gotUPDATE_BY_HEX(UPDATE_BY_HEX);
                        isWaiting = true;
                    }

                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }  

    private void gotUPDATE_BY_HEX(List<List<String>> UPDATE_BY_HEX) {
        System.out.println("Service: Data is updating...");
        hexEditor.editOpenedFileByHex(UPDATE_BY_HEX);
        try {
            UPDATE_BY_HEXExchanger.exchange(null, 1, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException ignored) {
        }
        System.out.println("Service: Data was updated");
    }

    // Получение файла, его чтение, отправка строк в форме HEX 
    private void gotFile(File file) {
        try {
            System.out.println("Service: File was added");
            hexEditor = new HexEditor(file.getAbsolutePath());

            System.out.println("Service: File to Hex");

            hexExchanger.exchange(hexEditor.getHexLines());
            
            System.out.println("Service: Hex was sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    // Получение строк в форме HEX, перевод в CHAR, отправка 
    private void gotHex(List<List<String>> data) {
        try {
            System.out.println("Service: Hex were added");
            
            charsExchanger.exchange(data.stream().map(x -> hexEditor.getCharsFromHex(x)).collect(Collectors.toList()));
            System.out.println("Service: Chars were sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
    
    // Получение строк CHAR, перевод в HEX, отправка
    private void gotChars(List<List<String>> data) {
        try {
            System.out.println("Service: Chars were added");
            
            hexExchanger.exchange(data.stream().map(x -> hexEditor.getHexFromChars(x)).collect(Collectors.toList()));
            System.out.println("Service: Hex were sent");
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void gotSEARCH_BY_STRING(String mask) {
        try {
            System.out.println("Service: mask was added");
            if (hexEditor.getStrings() != null) {
                integerExchanger.exchange(hexEditor.findByMask(mask));
                System.out.println("Service: Position was sent");
            } else {
                integerExchanger.exchange(new ArrayList<>());
                System.out.println("Service: File is null");
            }
            
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void gotSEARCH_BY_HEX(List<String> hex) {
        try {
            System.out.println("Service: hex for search was added");
            if (hexEditor.getStrings() != null) {
                integerExchanger.exchange(hexEditor.find(hex));
                System.out.println("Service: Position was sent");
            } else {
                integerExchanger.exchange(new ArrayList<>());
                System.out.println("Service: File is null");
            }

            } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public void close() {
        close = true;
    }
}
