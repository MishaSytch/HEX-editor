package hex.editor.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;

import hex.editor.model.Position;
import hex.editor.model.Positions;
import hex.editor.services.HexService;

public class HexEditor {
    private List<List<String>> hex;

    public HexEditor(List<List<String>> hex) {
        this.hex = hex;
    }

    public List<List<String>> getHexLines() throws NullPointerException {
        return hex;
    }

    public List<List<String>> getCharLines() throws NullPointerException {
        return hex.stream().map(HexService::getCharsFromHex).collect(Collectors.toList());
    }

    public String getCharFromHex(String hex) throws NumberFormatException, NullPointerException  {
        List<String> list = new ArrayList<>();
        list.add(hex);
        return HexService.getCharsFromHex(list).get(0);
    }

    public String getHexFromChar(String ch) {
        if (ch == null || ch.isEmpty()) {
            throw new IllegalArgumentException("Input character string is null or empty");
        }
        List<String> list = new ArrayList<>();
        list.add(ch);
        try {
            return HexService.getHexFromChars(list).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Failed to convert character to hex");
        }
    }

    public List<String> getCharsFromHex(List<String> hex) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        return HexService.getCharsFromHex(hex);
    }

    public List<String> getHexFromChars(List<String> chars) throws ArrayIndexOutOfBoundsException, NullPointerException {
        return HexService.getHexFromChars(chars);
    }

    public void editOpenedFileByHex(List<List<String>> hex) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        this.hex = hex;
    } 

    public void editOpenedFileByChars(List<List<String>> chars) {
        hex = chars.stream()
            .map(HexService::getHexFromChars)
            .collect(Collectors.toList());
    } 

    public void find(Positions positions, List<String> searchingHex) {
        if (hex == null) {
            throw new NullPointerException();
        }
        for (int row = 0; row < hex.size(); row++) {
            if (!hex.get(row).isEmpty()) {
                for (int column = 0; column < hex.get(row).size(); column++) {
                    int index = 0;
                    while (searchingHex.get(index).equals(hex.get(row).get(column + index++))) {
                        if (index == searchingHex.size()) {
                            positions.add(new Position(row, column));
                            break;
                        }
                    }
                    column += index - 1;
                }
            }
        }
    }

    public void findByMask(Positions positions, String mask) {
        if (hex == null) {
            throw new NullPointerException();
        }
        if (mask.isEmpty()) {
            throw new IllegalArgumentException("Mask is empty"); 
        }
        for (int row = 0; row < hex.size(); row++) {
            Pattern regexp = Pattern.compile(mask.toUpperCase());
            if (!hex.get(row).isEmpty()) {
                for (int column = 0; column < hex.get(row).size(); column++) {
                    if (hex.get(row).get(column) != null) {
                        Matcher match = regexp.matcher(hex.get(row).get(column));
                        if (match.find()) positions.add(new Position(row, column));
                    }
                }
            }
        }
    }

    public static byte[] hexStringToByteArray(String hexValue) {
        int len = hexValue.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexValue.charAt(i), 16) << 4)
                    + Character.digit(hexValue.charAt(i+1), 16));
        }
        return data;
    }
}
