package framework.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileUtils {
    public static String readFile(File file) {
        return readFile(file, StandardCharsets.UTF_8);
    }

    public static String readFile(File file, Charset charset) {
        byte[] data = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file)) {
            int len = fis.read(data);
            if (len != data.length) {
                throw new IOException();
            }
            return new String(data, charset);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeFile(File file, String s) {
        writeFile(file, s, StandardCharsets.UTF_8);
    }

    public static void writeFile(File file, String s, Charset charset) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(s.getBytes(charset));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
