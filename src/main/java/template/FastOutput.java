package template;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;

public class FastOutput implements AutoCloseable, Closeable {
    private StringBuilder cache = new StringBuilder(10 << 20);
    private final Writer os;

    public FastOutput(Writer os) {
        this.os = os;
    }

    public FastOutput(OutputStream os) {
        this(new OutputStreamWriter(os));
    }

    public FastOutput append(char c) {
        cache.append(c);
        return this;
    }

    public FastOutput append(int c) {
        cache.append(c);
        return this;
    }

    public FastOutput append(long c) {
        cache.append(c);
        return this;
    }

    public FastOutput append(float c) {
        cache.append(c);
        return this;
    }

    public FastOutput append(double c) {
        cache.append(c);
        return this;
    }

    public FastOutput append(String c) {
        cache.append(c);
        return this;
    }

    public FastOutput append(Object c) {
        cache.append(c);
        return this;
    }

    public FastOutput printf(String format, Object... args) {
        cache.append(String.format(format, args));
        return this;
    }

    public FastOutput println(char c) {
        cache.append(c).append('\n');
        return this;
    }

    public FastOutput println(String c) {
        cache.append(c).append('\n');
        return this;
    }

    public FastOutput println(int c) {
        cache.append(c).append('\n');
        return this;
    }

    public FastOutput println(long c) {
        cache.append(c).append('\n');
        return this;
    }

    public FastOutput println(float c) {
        cache.append(c).append('\n');
        return this;
    }

    public FastOutput println(double c) {
        cache.append(c).append('\n');
        return this;
    }

    public FastOutput println(Object c) {
        cache.append(c).append('\n');
        return this;
    }

    public FastOutput println() {
        cache.append('\n');
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

    @Override
    public String toString() {
        return cache.toString();
    }
}
