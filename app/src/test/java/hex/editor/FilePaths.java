package hex.editor;

import java.nio.file.Paths;

public class FilePaths {
    private static String bigFilePath = Paths.get("C:", "Users", "misha", "Documents", "icetu", "HEX-editor", "app", "src", "test", "java", "hex", "editor", "filesForTest", "bigFile.txt").toString();
    private static String verySmallFilePath = Paths.get("C:", "Users", "misha", "Documents", "icetu", "HEX-editor", "app", "src", "test", "java", "hex", "editor", "filesForTest", "verySmallText.txt").toString();
    private static String oneChar = Paths.get("C:", "Users", "misha", "Documents", "icetu", "HEX-editor", "app", "src", "test", "java", "hex", "editor", "filesForTest", "oneChar.txt").toString();

    public static String getOneChar() {
        return oneChar;
    }

    public static String getBigFilePath() {
        return bigFilePath;
    }
    
    public static String getVerySmallFilePath() {
        return verySmallFilePath;
    }
}