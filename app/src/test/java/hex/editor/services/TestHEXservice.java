package hex.editor.services;

import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import hex.editor.FilePaths;


public class TestHEXservice {
    static String bigFilePath; 
    static String verySmallFilePath;
    static String oneChar;
    static HEXservice heXservice;

    @BeforeAll
    static void getter() {
        bigFilePath = FilePaths.getBigFilePath();
        verySmallFilePath = FilePaths.getVerySmallFilePath();
        oneChar = FilePaths.getOneChar();
        heXservice = new HEXservice();
    }

    @Test 
    void testGetHex_one() {
        try (Scanner scanner = new Scanner(new File(oneChar))) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()){
                stringBuilder.append(scanner.next());
            }
            String ch = stringBuilder.toString();

            heXservice.readLinesFromFile(oneChar);
            String hex = heXservice.getHex()[0];

            String res = Integer.toHexString(ch.getBytes()[0]);

            Assertions.assertEquals(hex, res);
        } catch (FileNotFoundException exception) {}
    }

    @Test
    void testGetHex_small() {
            try (Scanner scanner = new Scanner(new File(verySmallFilePath))) {
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNext()){
                    stringBuilder.append(scanner.nextLine());
                }
                String ch = stringBuilder.toString();
                String[] res = Arrays.stream(ch.split(""))
                    .map(x -> Integer.toHexString(x.getBytes()[0]))
                    .toArray(String[]::new);
                String s = Integer.toString(1478, 16);
                heXservice.readLinesFromFile(verySmallFilePath);
                String[] hex = heXservice.getHex();

                boolean f = hex[0].equals(res[0]);

                Assertions.assertEquals(hex, res);
            } catch (FileNotFoundException exception) {}
    }

    @Test
    void testGetChars() throws FileNotFoundException {
        heXservice.readLinesFromFile(verySmallFilePath);
        try (Scanner scanner = new Scanner(new File(verySmallFilePath))) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
            }

            char[] lines = stringBuilder.toString().toCharArray();

            Assertions.assertEquals(heXservice.getChars(), lines);
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
