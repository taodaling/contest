package template.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FastOutput implements AutoCloseable, Closeable, Appendable {
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

    private static final int THRESHOLD = 1 << 13;
    private final Writer os;
    private StringBuilder cache = new StringBuilder(THRESHOLD * 2);

    public StringBuilder getCache() {
        return cache;
    }

    private void afterWrite() {
        if (cache.length() < THRESHOLD) {
            return;
        }
        flush();
    }

    public FastOutput(Writer os) {
        this.os = os;
    }

    public FastOutput(OutputStream os) {
        this(new OutputStreamWriter(os));
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

    public FastOutput append(float c) {
        cache.append(new BigDecimal(c).toPlainString());
        afterWrite();
        return this;
    }

    public FastOutput append(double c) {
        cache.append(new BigDecimal(c).toPlainString());
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
        return append(System.lineSeparator());
    }

    public FastOutput clear() {
        cache.setLength(0);
        return this;
    }

    public FastOutput flush() {
        try {
            os.append(cache);
            os.flush();
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
            os.close();
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
