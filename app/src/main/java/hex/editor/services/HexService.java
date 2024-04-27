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

    public static String[] getCharsFromHex(String[] hex) throws NumberFormatException {
        return Arrays.stream(hex)
            .filter(x -> {
                if (
                    x != "" 
                    || (byte)(x.toUpperCase().charAt(0)) >= (byte)'A' && (byte)(x.toUpperCase().charAt(0)) <= (byte)'F'
                    || (byte)(x.toUpperCase().charAt(0)) >= (byte)'0' && (byte)(x.toUpperCase().charAt(0)) <= (byte)'9'
                )
                    return true;
                throw new NumberFormatException();
            })
            .map(hx -> ((char)Integer.parseInt(hx, 16)))
            .map(ch -> String.valueOf(ch))
            .toArray(String[]::new);
    }

    public static String[] getHexFromChars(String[] chars) {
        return Arrays.stream(chars)
            .filter(x -> x != "")
            .map(str -> str.charAt(0))
            .map(ch -> {
                if((int)ch > 256) return String.format("%04x", (int)ch).toUpperCase();
                return String.format("%02x", (int)ch).toUpperCase();
            })
            .toArray(String[]::new);
    }
}
