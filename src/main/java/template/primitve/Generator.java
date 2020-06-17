package template.primitve;

import template.io.FastInput;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Generator {
    public static void main(String[] args) {
        FastInput fi = new FastInput(System.in);
        System.out.print("Directory for input file: ");
        File src = new File(fi.readString());
        System.out.print("Directory for output file: ");
        File dst = new File(fi.readString());
        System.out.print("Package for output file: ");
        String packageTo = "package " + fi.readString() + ";";

        List<Function<FileItem, FileItem>> functions = new ArrayList<>();
        functions.add(new Transformer("Long", "long", "%d", packageTo));
        functions.add(new Transformer("Integer", "int", "%d", packageTo));
        functions.add(new Transformer("Double", "double", "%f", packageTo));

        new Generator().handleFile(src, dst, functions);
        System.out.print("Done!");
    }


    Charset charset = Charset.forName("utf-8");

    private static class Transformer implements Function<FileItem, FileItem> {
        final String big;
        final String small;
        final String format;
        final String packageTo;

        private Transformer(String big, String small, String format, String packageTo) {
            this.big = big;
            this.small = small;
            this.format = format;
            this.packageTo = packageTo;
        }

        public String replaceAll(String s) {
            return s.replaceAll("Long", big).replaceAll("long", small);
        }

        @Override
        public FileItem apply(FileItem fileItem) {
            int index = fileItem.content.indexOf(';');
            String packageInfo = fileItem.content.substring(0, index + 1);
            String remain = fileItem.content.substring(index + 1);
            return new FileItem(replaceAll(fileItem.filename), packageTo + replaceAll(remain));
        }
    }

    private void handleFile(File src, File dst, List<Function<FileItem, FileItem>> functions) {
        for (File sub : src.listFiles()) {
            try (FileInputStream fis = new FileInputStream(sub)) {
                byte[] data = new byte[(int) sub.length()];
                fis.read(data);
                FileItem item = new FileItem(sub.getName(), new String(data, charset));
                for (Function<FileItem, FileItem> func : functions) {
                    FileItem output = func.apply(item);
                    File newFile = new File(dst, output.filename);
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        fos.write(output.content.getBytes(charset));
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static class FileItem {
        final String filename;
        final String content;

        private FileItem(String filename, String content) {
            this.filename = filename;
            this.content = content;
        }
    }


}
