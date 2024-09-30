package xyz.someboringnerd.waifuhax.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathUtils {
    public static String join(String... paths) {
        StringBuilder sb = new StringBuilder();
        for (String path : paths) {
            sb.append(path);
            sb.append(FileSystems.getDefault().getSeparator());
        }
        return sb.toString();
    }

    public static String readFileToString(String filePath) {
        Path path = Paths.get(filePath);

        try {
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeStringToFile(String join, String string) {
        Path path = Paths.get(join);

        try {
            Files.write(path, string.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
