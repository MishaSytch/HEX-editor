package hex.editor.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;

import hex.editor.services.FileViewer;
import hex.editor.services.HexService;

public class HexEditor {
    private List<String> strings = null;

    public List<String> getStrings() {
        return strings;
    }

    public HexEditor(String path) {
        openFile(path);
    }

    public HexEditor() {

    }

    public void openNewFile(String path) {
        openFile(path);
    }

    private void openFile(String path) {
        FileViewer fileViewer = new FileViewer(path);
        strings = fileViewer.getLines();
    }

    public List<List<String>> getHexLines() throws NullPointerException {
        return strings.stream().map(HexService::getHexFromString).collect(Collectors.toList());
    }

    public List<List<String>> getCharLines() throws NullPointerException {
        return strings.stream().map(HexService::getCharsFromString).collect(Collectors.toList());
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
        strings = hex.stream()
            .map(HexService::getCharsFromHex)
            .map(x -> x.stream().collect(Collectors.joining("")))
            .collect(Collectors.toList());
    } 

    public void editOpenedFileByChars(List<List<String>> chars) {
        strings = chars.stream()
            .map(x -> x.stream().collect(Collectors.joining("")))
            .collect(Collectors.toList());
    } 

    public List<List<Integer>> find(List<String> searchingHex) {
        if (strings == null) {
            throw new NullPointerException();
        }
        if (searchingHex.isEmpty()) {
            throw new IllegalArgumentException("Array has null length"); 
        }
        List<List<Integer>> pos_row = new ArrayList<>();
        List<List<String>> hexFromString = getHexLines();
        
        for (int row = 0; row < hexFromString.size(); row++) {
            List<Integer> pos_column = new ArrayList<>();

            for (int column = 0; column < hexFromString.get(row).size(); column++) {
                int i_byte = 0;
                int start = column;
                while(hexFromString.get(row).get(column++).equalsIgnoreCase(searchingHex.get(i_byte))) {
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
        if (strings == null) {
            throw new NullPointerException();
        }
        if (mask.length() == 0) {
            throw new IllegalArgumentException("Mask is empty"); 
        }

        List<List<Integer>> pos_rows = new ArrayList<>();
        List<List<String>> hex = getHexLines();

        for (int row = 0; row < hex.size(); row++) {
            Pattern regexp = Pattern.compile(mask.toUpperCase());
            List<Integer> pos_column = new ArrayList<>();

            if (!hex.get(row).isEmpty()) {
                for (int column = 0; column < hex.get(row).size(); column++) {
                    Matcher match = regexp.matcher(hex.get(row).get(column));
                    if (match.find()) pos_column.add(column);
                }
            }
            pos_rows.add(pos_column);
        }
        return pos_rows;
    }
}
