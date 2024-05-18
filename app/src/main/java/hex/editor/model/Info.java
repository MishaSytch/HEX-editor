package hex.editor.model;

import hex.editor.controller.HexEditor;

public class Info {
    private final int row;
    private final int column;
    private final String char_info;
    private final String hex_info;
    
    
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
                + "Hex: " + hex_info + "<br>" + "<br>"
                + "2 byte: " + hexToBinary(2, hex_info) + "<br>"
                + "4 byte: " + hexToBinary(4, hex_info) + "<br>"
                + "8 byte: " + hexToBinary(8, hex_info);
    }

    private String hexToBinary(int byteCount,String hex) {
        byte[] bytes = HexEditor.hexStringToByteArray(hex);
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            binary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return binary.substring((Math.max(binary.length() - 8 * byteCount, 0)),binary.length());
    }

    private String interpretData(int byteCount, String dataType) {
        byte[] data = HexEditor.hexStringToByteArray(hex_info);

        switch (dataType) {
            case "int":
                return String.valueOf(Info.readAsInt(data, 0));
            case "uint":
                return String.valueOf(Info.readAsLong(data, 0));
            case "float":
                if (byteCount <= 4) {
                    return String.valueOf(Info.readAsFloat(data, 0));
                }
                break;
            case "double":
                if (byteCount <= 8) {
                    return String.valueOf(Info.readAsDouble(data, 0));
                }
                break;
        }
        return "";
    }


    private static long readAsLong(byte[] data, int offset) {
        long value = 0;
        for (int i = 0; i < data.length; i++) {
            value = (value << 8) | (data[offset + i] & 0xFF);
        }
        return value;
    }

    private static int readAsInt(byte[] data, int offset) {
        int value = 0;
        for (int i = 0; i < data.length; i++) {
            value = (value << 8) | (data[offset + i] & 0xFF);
        }
        return value;
    }

    private static double readAsDouble(byte[] data, int offset) {
        return Double.longBitsToDouble(readAsLong(data, offset));
    }

    private static float readAsFloat(byte[] data, int offset) {
        return Float.intBitsToFloat(readAsInt(data, offset));
    }
    
}
