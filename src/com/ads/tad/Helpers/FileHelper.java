package com.ads.tad.Helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

public class FileHelper {
    public static final String SAVE_PATH = System.getProperty("user.dir") + File.separator;

    /**
     * Saves a String to a file.
     * 
     * @param content
     * @param prefix
     * @return the file where the String was saved to.
     * @throws IOException
     */
    public static String save(String content, String prefix) throws IOException {
        String fileName = getFileName(prefix);
        File savedFile = new File(fileName);

        if (!savedFile.exists()) {
            savedFile.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(savedFile.getAbsoluteFile());
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(content);
        bufferedWriter.close();

        return fileName;
    }

    public static String read(String prefix) throws IOException {
        String fileName = getFileName(prefix);
        File savedFile = new File(fileName);

        if (!savedFile.exists()) {
            return null;
        }

        FileReader fileReader = new FileReader(savedFile.getAbsoluteFile());
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String content = bufferedReader.lines().collect(Collectors.joining("\n"));
        bufferedReader.close();
        return content;
    }

    private static String getFileName(String prefix) {
        String fileName = SAVE_PATH + prefix;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fileName).append(".tad");
        return stringBuilder.toString();
    }
}
