package hex.editor.services;

import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
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
            heXservice.readLinesFromFile(oneChar);
            String[] hex = heXservice.getHex();
            String[] res = new String[]{"0432"};

            Assertions.assertArrayEquals(hex, res);
    }

    @Test
    void testGetHex_small() {
            // try (Scanner scanner = new Scanner(new File(verySmallFilePath))) {
            //     StringBuilder stringBuilder = new StringBuilder();
            //     while (scanner.hasNext()){
            //         stringBuilder.append(scanner.nextLine());
            //     }
            //     String ch = stringBuilder.toString();
            //     String[] res = Arrays.stream(ch.split(""))
            //         .map(x -> Integer.toString(x.getBytes()[0], 16).toUpperCase())
            //         .toArray(String[]::new);
                String[] res = {
                    "50", "75", "6C", "76", "69", "6E", "61", "72", "20", "65", "6C", "65",
                    "6D", "65", "6E", "74", "75", "6D", "20", "69", "6E", "74", "65", "67",
                    "65", "72", "2E", "0417", "0430", "043C", "043E", "0440", "043E", "0437", "043A", "0438", "20",
                    "043D", "0430", "0441", "0442", "0443", "043F", "0438", "043B", "0438", "20", "33", "2D", "0433", "043E",
                    "20", "0447", "0438", "0441", "043B", "0430", "21"
                };

                heXservice.readLinesFromFile(verySmallFilePath);
                String[] hex = heXservice.getHex();

                Assertions.assertArrayEquals(hex, res);
            // } catch (FileNotFoundException exception) {}
    }

    @Test
    void testGetChars() throws FileNotFoundException {
        heXservice.readLinesFromFile(verySmallFilePath);
        try (Scanner scanner = new Scanner(new File(verySmallFilePath))) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
            }
            String[] lines = stringBuilder.toString().split("");

            Assertions.assertArrayEquals(heXservice.getChars(), lines);
        } catch (FileNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Test
    void testGetCharsFromHex() {
        
        String[] hex = 
        String[] chars = heXservice.getCharsFromHex(hex)
    }
}
