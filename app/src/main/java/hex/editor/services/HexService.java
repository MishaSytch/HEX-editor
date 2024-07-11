package hex.editor.services;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HexService {

    public static List<String> getHexFromString(String line) throws NullPointerException {
        List<String> list = new ArrayList<>();
        byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
        for (byte b : bytes) {
            list.add(String.format("%02x", b));
        }
    
        return list;
    }

    public static List<String> getCharsFromHex(List<String> hex) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        if (hex.isEmpty()) throw new ArrayIndexOutOfBoundsException();
        
        return hex.stream()
            .filter(x -> {
                if (
                    x.equals(" ") || x.isEmpty()
                    || (byte)(x.toUpperCase().charAt(0)) >= (byte)'A' && (byte)(x.toUpperCase().charAt(0)) <= (byte)'F'
                    || (byte)(x.toUpperCase().charAt(0)) >= (byte)'0' && (byte)(x.toUpperCase().charAt(0)) <= (byte)'9'
                )
                    return true;
                throw new NumberFormatException();
            })
            .map(hx -> hx.isEmpty() ? "" : String.valueOf((char)Integer.parseInt(hx, 16))) 
            .collect(Collectors.toList());
    }
}
