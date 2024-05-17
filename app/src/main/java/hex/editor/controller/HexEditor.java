package hex.editor.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;

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

    public List<List<Integer>> find(List<String> searchingHex) {
        if (hex == null) {
            throw new NullPointerException();
        }
        if (searchingHex.isEmpty()) {
            throw new IllegalArgumentException("Array has null length"); 
        }
        List<List<Integer>> pos_row = new ArrayList<>();

        for (List<String> strings : hex) {
            List<Integer> pos_column = new ArrayList<>();

            for (int column = 0; column < strings.size(); column++) {
                int i_byte = 0;
                int start = column;
                while (strings.get(column++).equalsIgnoreCase(searchingHex.get(i_byte))) {
                    if (++i_byte == searchingHex.size())
                        break;
                }
                if (i_byte == searchingHex.size()) {
                    for (int i = start; i < start + i_byte; i++) {
                        pos_column.add(i);
                    }
                }
                column = start;
            }
            pos_row.add(pos_column);
        }
        return pos_row;
    }

    public List<List<Integer>> findByMask(String mask) {
        if (hex == null) {
            throw new NullPointerException();
        }
        if (mask.isEmpty()) {
            throw new IllegalArgumentException("Mask is empty"); 
        }
        List<List<Integer>> pos_rows = new ArrayList<>();
        for (List<String> strings : hex) {
            Pattern regexp = Pattern.compile(mask.toUpperCase());
            List<Integer> pos_column = new ArrayList<>();

            if (!strings.isEmpty()) {
                for (int column = 0; column < strings.size(); column++) {
                    Matcher match = regexp.matcher(strings.get(column));
                    if (match.find()) pos_column.add(column);
                }
            }
            pos_rows.add(pos_column);
        }
        return pos_rows;
    }
}
