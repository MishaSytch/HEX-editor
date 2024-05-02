package hex.editor.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;

import hex.editor.services.FileViewer;
import hex.editor.services.HexService;

public class HexEditor {
    private List<String> strings;

    public List<String> getStrings() {
        return strings;
    }

    public HexEditor(String path) {
        openFile(path);
    }

    public void openNewFile(String path) {
        openFile(path);
    }

    private void openFile(String path) {
        FileViewer fileViewer = new FileViewer(path);
        strings = fileViewer.getLines();
    }

    public List<List<String>> getHexLines() throws NullPointerException {
        return strings.stream().map(HexService::getHexFromString).toList();
    }

    public List<List<String>> getCharLines() throws NullPointerException {
        return strings.stream().map(HexService::getCharsFromString).toList();
    }

    public String getCharFromHex(String hex) throws NumberFormatException, NullPointerException  {
        List<String> list = new ArrayList<>();
        list.add(hex);
        return HexService.getCharsFromHex(list).get(0);
    }

    public String getHexFromChar(String ch) throws ArrayIndexOutOfBoundsException, NullPointerException {
        String res;
        try {
            List<String> list = new ArrayList<>();
            list.add(ch);
            res = HexService.getHexFromChars(list).get(0);
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new NullPointerException();
        }
        return res;
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
            .toList();
    } 

    public void editOpenedFileByChars(List<List<String>> chars) {
        strings = chars.stream()
            .map(x -> x.stream().collect(Collectors.joining("")))
            .toList();
    } 

    public List<List<Integer>> find(List<String> searchingHex) {
        if (searchingHex.isEmpty()) {
            throw new IllegalArgumentException("Array has null length"); 
        }
        List<List<Integer>> pos_row = new ArrayList<>();
        List<List<String>> hexFromString = getHexLines();
        
        for (int row = 0; row < hexFromString.size(); row++) {
            List<Integer> pos_column = new ArrayList<>();

            for (int column = 1; column < hexFromString.get(row).size(); column++) {
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
        if (mask.length() == 0) {
            throw new IllegalArgumentException("Mask is empty"); 
        }

        List<List<Integer>> pos_rows = new ArrayList<>();
        List<List<String>> hex = getHexLines();

        for (int row = 0; row < hex.size(); row++) {
            Pattern regexp = Pattern.compile(mask.toUpperCase());
            List<Integer> pos_column = new ArrayList<>();

            if (!hex.get(row).isEmpty()) {
                for (int column = 1; column < hex.get(row).size(); column++) {
                    Matcher match = regexp.matcher(hex.get(row).get(column));
                    if (match.find()) pos_column.add(column);
                }
            }
            pos_rows.add(pos_column);
        }
        return pos_rows;
    }
}
