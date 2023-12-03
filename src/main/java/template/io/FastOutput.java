package template.io;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.Format;

public class FastOutput implements AutoCloseable, Closeable, Appendable {

//    public Writer getWriter() {
//        return writer;
//    }

    @Override
    public FastOutput append(CharSequence csq) {
        cache.append(csq);
        return this;
    }

    @Override
    public FastOutput append(CharSequence csq, int start, int end) {
        cache.append(csq, start, end);
        return this;
    }

    private static final int THRESHOLD = 32 << 10;
    private OutputStream writer;
    private StringBuilder cache = new StringBuilder(THRESHOLD * 2);
    private static Field stringBuilderValueField;
    private char[] charBuf = new char[THRESHOLD * 2];
    private byte[] byteBuf = new byte[THRESHOLD * 2];

    static {
        try {
            stringBuilderValueField = StringBuilder.class.getSuperclass().getDeclaredField("value");
            stringBuilderValueField.setAccessible(true);
        } catch (Exception e) {
            stringBuilderValueField = null;
        }
        stringBuilderValueField = null;
    }

    public StringBuilder getCache() {
        return cache;
    }

    private void afterWrite() {
        if (cache.length() < THRESHOLD) {
            return;
        }
        flush();
    }


    public FastOutput(OutputStream writer) {
        this.writer = writer;
    }

    public FastOutput append(char c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput append(int c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput append(long c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    static DecimalFormat defaultDoubleFormat = new DecimalFormat("#.############");
    DecimalFormat doubleFormat = defaultDoubleFormat;

    public FastOutput setDoubleFormat(DecimalFormat df){
        this.doubleFormat = df;
        return this;
    }

    public FastOutput append(float c) {
        return append((double)c);
    }

    public FastOutput append(double c) {
        cache.append(doubleFormat.format(c));
        afterWrite();
        return this;
    }

    public FastOutput append(String c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput append(Object c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput append(char[] c) {
        cache.append(c);
        afterWrite();
        return this;
    }

    public FastOutput printf(String format, Object... args) {
        return append(String.format(format, args));
    }

    public FastOutput println(char c) {
        return append(c).println();
    }

    public FastOutput println(String c) {
        return append(c).println();
    }

    public FastOutput println(int c) {
        return append(c).println();
    }

    public FastOutput println(long c) {
        return append(c).println();
    }

    public FastOutput println(float c) {
        return append(c).println();
    }

    public FastOutput println(double c) {
        return append(c).println();
    }

    public FastOutput println(Object c) {
        return append(c).println();
    }

    public FastOutput println(char[] c) {
        return append(c).println();
    }

    public FastOutput println() {
        return append('\n');
    }

    public FastOutput clear() {
        cache.setLength(0);
        return this;
    }

    public FastOutput flush() {
        try {
            if (stringBuilderValueField != null) {
                try {
                    byte[] value = (byte[]) stringBuilderValueField.get(cache);
                    writer.write(value, 0, cache.length());
                } catch (Exception e) {
                    stringBuilderValueField = null;
                }
            }
            if (stringBuilderValueField == null) {
                int n = cache.length();
                if (n > byteBuf.length) {
                    //slow
                    writer.write(cache.toString().getBytes(StandardCharsets.UTF_8));
//                writer.append(cache);
                } else {
                    cache.getChars(0, n, charBuf, 0);
                    for (int i = 0; i < n; i++) {
                        byteBuf[i] = (byte) charBuf[i];
                    }
                    writer.write(byteBuf, 0, n);
                }
            }
            writer.flush();
            cache.setLength(0);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return this;
    }

    @Override
    public void close() {
        flush();
        try {
            writer.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void pop(int k) {
        cache.setLength(cache.length() - k);
    }

    @Override
    public String toString() {
        return cache.toString();
    }
}
