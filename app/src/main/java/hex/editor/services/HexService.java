package hex.editor.services;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HexService {

    public static List<String> getHexFromString(String line) throws NullPointerException {
        return Arrays.stream(line.split(""))
            .filter(x -> x.length() > 0)
            .map(str -> str.charAt(0))
            .map(ch -> {
                if((int)ch > 256) return String.format("%04x", (int)ch).toUpperCase();
                return String.format("%02x", (int)ch).toUpperCase();
            })
            .collect(Collectors.toList());
    }

    public static List<String> getCharsFromString(String line) throws NullPointerException {
        return Arrays.stream(line.split("")).collect(Collectors.toList());
    }

    public static List<String> getCharsFromHex(List<String> hex) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        if (hex.isEmpty()) throw new ArrayIndexOutOfBoundsException();
        
        return hex.stream()
            .filter(x -> {
                if (
                    x.equals(" ") || x.length() == 0
                    || (byte)(x.toUpperCase().charAt(0)) >= (byte)'A' && (byte)(x.toUpperCase().charAt(0)) <= (byte)'F'
                    || (byte)(x.toUpperCase().charAt(0)) >= (byte)'0' && (byte)(x.toUpperCase().charAt(0)) <= (byte)'9'
                )
                    return true;
                throw new NumberFormatException();
            })
            .map(hx -> hx.length() == 0 ? "" : (char)Integer.parseInt(hx, 16))
            .map(ch -> String.valueOf(ch))
            .collect(Collectors.toList());
    }

    public static List<String> getHexFromChars(List<String> chars) throws ArrayIndexOutOfBoundsException, NullPointerException {
        if (chars.isEmpty()) throw new ArrayIndexOutOfBoundsException();

        return chars.stream()
            .map(str -> str.charAt(0))
            .map(ch -> {
                if((int)ch > 256) return String.format("%04x", (int)ch).toUpperCase();
                return String.format("%02x", (int)ch).toUpperCase();
            })
            .collect(Collectors.toList());
    }
}
