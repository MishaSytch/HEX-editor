package hex.editor;

public class FilePaths {
    private static String bigFilePath = new String("/home/msych/Документы/HEX-editor/app/src/test/java/hex/editor/filesForTest/bigFile.txt"); 
    private static String verySmallFilePath = new String("/home/msych/Документы/HEX-editor/app/src/test/java/hex/editor/filesForTest/verySmallText.txt");
    private static String oneChar = new String("/home/msych/Документы/HEX-editor/app/src/test/java/hex/editor/filesForTest/oneChar.txt");

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
