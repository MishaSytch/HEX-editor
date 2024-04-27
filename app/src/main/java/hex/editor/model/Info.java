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

    public String getRow() {
        return String.valueOf(row);
    }

    public String getColumn() {
        return String.valueOf(column);
    }

    public String getChar_info() {
        return char_info;
    }

    public String getHex_info() {
        return hex_info;
    }

    
    
}
