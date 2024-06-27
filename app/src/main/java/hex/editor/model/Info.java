package hex.editor.model;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Info {
    private final int row;
    private final int column;
    private final String char_info;


    public String getChar() {
        return char_info;
    }
    private final String hex_info;
    
    
    public String getHex() {
        return hex_info;
    }

    public Info(int row, int column, String char_info, String hex_info) {
        this.row = row;
        this.column = column;
        this.char_info = char_info;
        this.hex_info = hex_info;
    }

    public String getInfo() {
        return "<html>"
                + "Row: " + row + "<br>"
                + "Column: " + column + "<br>"
                + "Char: " + char_info + "<br>"
                + "Hex: " + hex_info + "<br>";
    }

    public static String getBytes(List<Info> infos) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\nbytes:\n");
        StringBuilder text = new StringBuilder();
        for (Info info : infos) {
            text.append(info.getChar());
        }
        sb.append("\t"
            + hexToRepresent(text.toString())
            + "<br>"
        );
        return sb.toString();
    }

    private static String hexToRepresent(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return readData(bytes, data.length());
    }

    private static String readData(byte[] data, int size) {
        String result;
        if (size == 2) {
            short value = ByteBuffer.wrap(data).getShort();
            result = String.valueOf(value);
        } else if (size == 4) {
            int value = ByteBuffer.wrap(data).getInt();
            result = String.valueOf(value);
        } else if (size == 8) {
            double value = ByteBuffer.wrap(data).getDouble();
            result = String.valueOf(value);
        } else {
            result = "Is not supported length of cells";
        }
        return result;
    }
    
}
