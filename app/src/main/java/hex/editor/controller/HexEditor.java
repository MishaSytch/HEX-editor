package hex.editor.controller;

import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.h;

import java.util.Arrays;
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

    public String[] getHexString() {
        return HexService.getHexFromString(string);
    }

    public String[] getCharsString() {
        return HexService.getCharsFromString(string);
    }

    public String getCharFromHex(String hex) {
        return HexService.getCharsFromHex(new String[]{hex})[0];
    }

    public String[] getCharsFromHex(String[] hex) {
        return HexService.getCharsFromHex(hex);
    }

    public void editOpenedFileByHex(String[] hex) {
        string = Arrays.stream(HexService.getCharsFromHex(hex)).collect(Collectors.joining(""));
    } 

    public void editOpenedFileByChars(String[] chars) {
        string = Arrays.stream(chars).collect(Collectors.joining(""));
    } 

    public Integer[] find(String[] bytes) {
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Array has null length"); 
        }
        List<Integer> ints = new ArrayList<Integer>();
        String[] hex = getCharsString();
        int i_bytes = 0;
        int start;
        for (int i = 0; i < hex.length; i++) {
            start = i;
            while(hex[i].equals(bytes[i_bytes])) {
                if (i_bytes + 1 == bytes.length) break;
                i++;
                i_bytes++;
            }
            if (i - start + 1 == bytes.length) ints.add(start);
            i_bytes = 0; 
        }
        return ints.toArray(Integer[]::new);
    }

    public Integer[] findByMask(String[] bytes) {
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Array has null length"); 
        }
        List<Integer> ints = new ArrayList<Integer>();
        String[] hex = getCharsString();
        int i_bytes = 0;
        int start;
        for (int i = 0; i < hex.length; i++) {
            start = i;
            while(hex[i].equals(bytes[i_bytes])) {
                if (i_bytes + 1 == bytes.length) break;
                i++;
                i_bytes++;
            }
            if (i - start + 1 == bytes.length) ints.add(start);
            i_bytes = 0; 
        }
        return ints.toArray(Integer[]::new);
    }
}
