package hex.editor;

public class FilePaths {
    private static String bigFilePath = new String("C:\\Users\\misha\\Documents\\nicetu\\HEX-editor\\app\\src\\test\\java\\hex\\editor\\filesForTest\\bigText.txt"); 
    private static String verySmallFilePath = new String("C:\\Users\\misha\\Documents\\nicetu\\HEX-editor\\app\\src\\test\\java\\hex\\editor\\filesForTest\\verySmallText.txt");
    
    public static String getBigFilePath() {
        return bigFilePath;
    }
    
    public static String getVerySmallFilePath() {
        return verySmallFilePath;
    }

    
}
