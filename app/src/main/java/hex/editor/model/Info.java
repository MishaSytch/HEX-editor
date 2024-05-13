package hex.editor.model;

public class Info {
    private int row;
    private int column;
    private String char_info;
    private String hex_info;
    
    
    public Info(int row, int column, String char_info, String hex_info) {
        this.row = row;
        this.column = column;
        this.char_info = char_info;
        this.hex_info = hex_info;
    }

    public String getInfo() {
        return new String(
            "<html>"
            + "Row: " + row + "<br>"
            + "Column: " + column + "<br>"
            + "Char: " + char_info + "<br>"
            + "Hex: " + hex_info + "<br>" + "<br>"
            + "U16 bit: " + (short)char_info.charAt(0) + "<br>"
            + "16 bit: " + ((int)char_info.charAt(0) > Math.pow(2, 8)? Math.pow(2, 8) - (int)char_info.charAt(0): (int)char_info.charAt(0)) + "<br>"
            + "U32 bit: " + (int)char_info.charAt(0) + "<br>"
            + "32 bit: " + ((long)char_info.charAt(0) > Math.pow(2, 16)? Math.pow(2, 16) - (long)char_info.charAt(0): (int)char_info.charAt(0)) + "<br>"
            + "U64 bit: " + (long)char_info.charAt(0) + "<br>"
            + "64 bit: " + ((long)char_info.charAt(0) > Math.pow(2, 32)? Math.pow(2, 32) - (long)char_info.charAt(0): (long)char_info.charAt(0)) + "<br>"
            + ""
        );
    }

    
    
}
