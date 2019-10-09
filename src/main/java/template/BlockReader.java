package template;

import java.io.IOException;
import java.io.InputStream;

public class BlockReader {
    InputStream is;
    byte[] dBuf;
    int dPos, dSize, next;
    static final int EOF = -1;

    public void skipBlank() {
        while (Character.isWhitespace(next)) {
            next = nextByte();
        }
    }

    StringBuilder builder = new StringBuilder();

    public String nextBlock() {
        builder.setLength(0);
        skipBlank();
        while (next != EOF && !Character.isWhitespace(next)) {
            builder.append((char) next);
            next = nextByte();
        }
        return builder.toString();
    }

    public int nextInteger() {
        skipBlank();
        int ret = 0;
        boolean rev = false;
        if (next == '+' || next == '-') {
            rev = next == '-';
            next = nextByte();
        }
        while (next >= '0' && next <= '9') {
            ret = (ret << 3) + (ret << 1) + next - '0';
            next = nextByte();
        }
        return rev ? -ret : ret;
    }

    public int nextBlock(char[] data, int offset) {
        skipBlank();
        int index = offset;
        int bound = data.length;
        while (next != EOF && index < bound && !Character.isWhitespace(next)) {
            data[index++] = (char) next;
            next = nextByte();
        }
        return index - offset;
    }

    public boolean hasMore() {
        skipBlank();
        return next != EOF;
    }

    public BlockReader(InputStream is) {
        this(is, 8192);
    }

    public BlockReader(InputStream is, int bufSize) {
        this.is = is;
        dBuf = new byte[bufSize];
        next = nextByte();
    }

    public int nextByte() {
        while (dPos >= dSize) {
            if (dSize == -1) {
                return EOF;
            }
            dPos = 0;
            try {
                dSize = is.read(dBuf);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return dBuf[dPos++];
    }
}