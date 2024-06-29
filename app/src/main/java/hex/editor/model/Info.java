package hex.editor.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
                + "Hex: " + hex_info + "<br><br>"
                + hexToRepresent(char_info);
    }

    public static String getBytes(List<Info> infos) {
        StringBuilder text = new StringBuilder();
        for (Info info : infos) {
            text.append(info.getChar());
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(hexToRepresent(text.toString()));

        return sb.toString();
    }

    private static String hexToRepresent(String data) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        return readData(bytes, data.length());
    }

    private static String readData(byte[] data, int size) {
        StringBuilder result = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        switch (size) {
            case 1: {
                byte byteValue = buffer.get();
                int unsignedValue = Byte.toUnsignedInt(byteValue);

                result.append("8-bit value: ").append(byteValue).append("<br>");
                result.append("Unsigned value: ").append(unsignedValue).append("<br>");
            
                break;
            }
            case 2: {
                short signedValue = buffer.getShort();
                int unsignedValue = Short.toUnsignedInt(signedValue);
                
                result.append("16-bit signed: <br>").append(signedValue).append("<br><br>");
                result.append("16-bit unsigned: <br>").append(unsignedValue).append("<br><br>");
            
            
                break;
            }
            case 4: {
                int signedValue = buffer.getInt();
                long unsignedValue = Integer.toUnsignedLong(signedValue);
                float floatValue = buffer.getFloat(buffer.position() - 4);
                
                result.append("32-bit signed: <br>").append(signedValue).append("<br><br>");
                result.append("32-bit unsigned: <br>").append(unsignedValue).append("<br><br>");
                result.append("32-bit float: <br>").append(floatValue).append("<br><br>");
            
                break;
            }
            case 8: {
                long signedValue = buffer.getLong();
                double doubleValue = buffer.getDouble(buffer.position() - 8);
                
                result.append("64-bit signed: <br>").append(signedValue).append("<br><br>");
                result.append("64-bit double: <br>").append(doubleValue).append("<br><br>");
        
                break;
            }
            default: {
                result.append("");

                break;
            }

        } 

        return result.toString();
    }
    
}
