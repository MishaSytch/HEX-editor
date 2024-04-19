package hex.editor.services;

import java.util.Arrays;

public class HexService {

    public static String[] getHexFromString(String lines) {
        return Arrays.stream(lines.split(""))
            .map(s -> s.charAt(0))
            .map(c -> {
                if((int)c > 256) return String.format("%04x", (int)c).toUpperCase();
                return String.format("%02x", (int)c).toUpperCase();
            })
            .toArray(String[]::new);
    }

    public static String[] getCharsFromString(String lines) {
        return lines.split("");
    }

    public static String[] getCharsFromHex(String[] hex) {
        return Arrays.stream(hex)
            .map(h -> ((char)Integer.parseInt(h, 16)))
            .map(ch -> String.valueOf(ch))
            .toArray(String[]::new);
    }

    public static String[] getHexFromChars(String[] chars) {
        return Arrays.stream(chars)
            .map(s -> s.charAt(0))
            .map(c -> {
                if((int)c > 256) return String.format("%04x", (int)c).toUpperCase();
                return String.format("%02x", (int)c).toUpperCase();
            })
            .toArray(String[]::new);
    }
}
