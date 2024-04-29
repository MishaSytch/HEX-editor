package hex.editor.controller;

import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import hex.editor.services.FileViewer;
import hex.editor.services.HexService;

public class HexEditor {
    private String string;

    public String getString() {
        return string;
    }

    public HexEditor(String path) {
        FileViewer fileViewer = new FileViewer(path);
        string = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public void openNewFile(String path) {
        FileViewer fileViewer = new FileViewer(path);
        string = fileViewer.getLines().stream().collect(Collectors.joining(""));
    }

    public String[] getHexString() throws NullPointerException {
        return HexService.getHexFromString(string);
    }

    public String[] getCharsString() throws NullPointerException {
        return HexService.getCharsFromString(string);
    }

    public String getCharFromHex(String hex) throws NumberFormatException, NullPointerException  {
        return HexService.getCharsFromHex(new String[]{hex})[0];
    }

    public String getHexFromChar(String ch) throws ArrayIndexOutOfBoundsException, NullPointerException {
        String res;
        try {
            res = HexService.getHexFromChars(new String[]{ch})[0];
        } catch (ArrayIndexOutOfBoundsException exception) {
            throw new NullPointerException();
        }
        return res;
    }

    public String[] getCharsFromHex(String[] hex) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        return HexService.getCharsFromHex(hex);
    }

    public String[] getHexFromChars(String[] chars) throws ArrayIndexOutOfBoundsException, NullPointerException {
        return HexService.getHexFromChars(chars);
    }

    public void editOpenedFileByHex(String[] hex) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        string = Arrays.stream(HexService.getCharsFromHex(hex)).collect(Collectors.joining(""));
    } 

    public void editOpenedFileByChars(String[] chars) {
        string = Arrays.stream(chars).collect(Collectors.joining(""));
    } 

    public Integer[] find(String[] searchingHex) {
        if (searchingHex.length == 0) {
            throw new IllegalArgumentException("Array has null length"); 
        }
        List<Integer> ints = new ArrayList<Integer>();
        String[] hexFromString = getHexString();
        int i_bytes = 0;
        int start;
        for (int i = 0; i < hexFromString.length; i++) {
            start = i;
            while(hexFromString[i++].equalsIgnoreCase(searchingHex[i_bytes])) {
                if (++i_bytes == searchingHex.length) 
                    break;
            }
            if (i_bytes == searchingHex.length) ints.add(start);
            i_bytes = 0; 
        }
        return ints.toArray(Integer[]::new);
    }

    public Integer[] findByMask(String mask) {
        if (mask.length() == 0) {
            throw new IllegalArgumentException("Mask is empty"); 
        }
        List<Integer> ints = new ArrayList<Integer>();
        String[] hex = getHexString();
        
        Pattern regexp = Pattern.compile(mask.toUpperCase());

        for (int i = 0; i < hex.length; i++) {
            Matcher match = regexp.matcher(hex[i]);
            if (match.find()) ints.add(i);

        }
        return ints.toArray(Integer[]::new);
    }
}
