package hex.editor.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import hex.editor.model.Position;
import hex.editor.model.Positions;
import hex.editor.services.HexService;

public class HexEditor {
    public static String getCharFromHex(String hex) throws NumberFormatException, ArrayIndexOutOfBoundsException, NullPointerException  {
        if (hex == null) throw new NullPointerException();

        List<String> list = new ArrayList<>();
        list.add(hex);
        return HexService.getCharsFromHex(list).get(0);
    }

    public static void find(Positions positions, List<String> searchingHex, List<List<String>> hex) throws NullPointerException {
        if (hex == null) {
            throw new NullPointerException();
        }
        for (int row = 0; row < hex.size(); row++) {
            if (!hex.get(row).isEmpty()) {
                for (int column = 0; column < hex.get(row).size(); column++) {
                    int index = 0;
                    int currentColumn = column;
                    int currentRow = row;
                    while (index < searchingHex.size() && currentRow < hex.size() && currentColumn < hex.get(currentRow).size() && searchingHex.get(index).equalsIgnoreCase(hex.get(currentRow).get(currentColumn))) {
                        if (index == searchingHex.size() - 1) {
                            for (int i = 0; i < searchingHex.size(); i++) {
                                positions.add(new Position(row, column++));
                                if (column == hex.get(row).size()) {
                                    column = 0;
                                    row++;
                                }
                            }
                            break;
                        }

                        currentColumn++;
                        index++;

                        if (currentColumn == hex.get(currentRow).size()) {
                            currentColumn = 0;
                            currentRow++;
                        }
                    }
                    column = currentColumn;
                    row = currentRow;
                }
            }
        }
    }

    public static void findByMask(Positions positions, String mask, List<List<String>> hex) {
        if (hex == null) {
            throw new NullPointerException();
        }
        if (mask.isEmpty()) {
            throw new IllegalArgumentException("Mask is empty"); 
        }
        for (int row = 0; row < hex.size(); row++) {
            Pattern regexp = Pattern.compile(mask.toLowerCase());
            if (!hex.get(row).isEmpty()) {
                for (int column = 0; column < hex.get(row).size(); column++) {
                    if (hex.get(row).get(column) != null) {
                        Matcher match = regexp.matcher(hex.get(row).get(column).toLowerCase());
                        if (match.find()) positions.add(new Position(row, column));
                    }
                }
            }
        }
    }
}
