package hex.editor.services;

import java.io.File;

public class FilePaths {
    private static final String textFile = "app/src/test/resources/text.txt";
    private static final String saveFile = "app/src/test/resources/save.txt";

    public static String getSaveFile() {
        File resourcesDirectory = new File(saveFile);
        return resourcesDirectory.getAbsolutePath().replace("/app/app/", "/app/");
    }

    public static String getTextFile() {
        File resourcesDirectory = new File(textFile);
        return resourcesDirectory.getAbsolutePath().replace("/app/app/", "/app/");
    }
}