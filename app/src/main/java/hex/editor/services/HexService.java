package hex.editor.services;

import java.util.Arrays;

public class HexService {

    public static String[] getHexFromString(String line) {
        return Arrays.stream(line.split(""))
            .map(str -> str.charAt(0))
            .map(ch -> {
                if((int)ch > 256) return String.format("%04x", (int)ch).toUpperCase();
                return String.format("%02x", (int)ch).toUpperCase();
            })
            .toArray(String[]::new);
    }

    public static String[] getCharsFromString(String line) {
        return line.split("");
    }

    public static String[] getCharsFromHex(String[] hex) {
        return Arrays.stream(hex)
            .map(hx -> ((char)Integer.parseInt(hx, 16)))
            .map(ch -> String.valueOf(ch))
            .toArray(String[]::new);
    }

    public static String[] getHexFromChars(String[] chars) {
        return Arrays.stream(chars)
            .map(str -> str.charAt(0))
            .map(ch -> {
                if((int)ch > 256) return String.format("%04x", (int)ch).toUpperCase();
                return String.format("%02x", (int)ch).toUpperCase();
            })
            .toArray(String[]::new);
    }
}
