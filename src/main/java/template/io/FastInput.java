package template.io;

import java.io.IOException;
import java.io.InputStream;

public class FastInput {
    private final InputStream is;
    private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);

    private byte[] buf = new byte[1 << 13];
    private int bufLen;
    private int bufOffset;
    private int next;


    public FastInput(InputStream is) {
        this.is = is;
    }

    public void populate(int[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = readInt();
        }
    }

    public void populate(char[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = readChar();
        }
    }

    public void populate(long[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = readLong();
        }
    }

    public void populate(double[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = readDouble();
        }
    }

    public void populate(String[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = readString();
        }
    }

    private int read() {
        while (bufLen == bufOffset) {
            bufOffset = 0;
            try {
                bufLen = is.read(buf);
            } catch (IOException e) {
                bufLen = -1;
            }
            if (bufLen == -1) {
                return -1;
            }
        }
        return buf[bufOffset++];
    }

    public void skipBlank() {
        while (next >= 0 && next <= 32) {
            next = read();
        }
    }

    public String next() {
        return readString();
    }

    public long readModInt(long mod) {
        skipBlank();
        long ans = 0;
        while (next >= '0' && next <= '9') {
            ans = (ans * 10 + next - '0') % mod;
            next = read();
        }
        return ans;
    }

    public long[] readModInt(long mod1, long mod2) {
        skipBlank();
        long ans1 = 0;
        long ans2 = 0;
        while (next >= '0' && next <= '9') {
            ans1 = (ans1 * 10 + next - '0') % mod1;
            ans2 = (ans2 * 10 + next - '0') % mod2;
            next = read();
        }
        return new long[]{ans1, ans2};
    }

    public long rmi(long mod) {
        return readModInt(mod);
    }

    public int ri() {
        return readInt();
    }

    public int[] ri(int n) {
        int[] ans = new int[n];
        populate(ans);
        return ans;
    }

    public long[] rl(int n) {
        long[] ans = new long[n];
        populate(ans);
        return ans;
    }

    public char[] rc(int n) {
        char[] ans = new char[n];
        populate(ans);
        return ans;
    }

    public double[] rd(int n) {
        double[] ans = new double[n];
        populate(ans);
        return ans;
    }

    public int readInt() {
        int sign = 1;

        skipBlank();
        if (next == '+' || next == '-') {
            sign = next == '+' ? 1 : -1;
            next = read();
        }

        int val = 0;
        if (sign == 1) {
            while (next >= '0' && next <= '9') {
                val = val * 10 + next - '0';
                next = read();
            }
        } else {
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }
        }

        return val;
    }

    public long rl() {
        return readLong();
    }

    public long readLong() {
        int sign = 1;

        skipBlank();
        if (next == '+' || next == '-') {
            sign = next == '+' ? 1 : -1;
            next = read();
        }

        long val = 0;
        if (sign == 1) {
            while (next >= '0' && next <= '9') {
                val = val * 10 + next - '0';
                next = read();
            }
        } else {
            while (next >= '0' && next <= '9') {
                val = val * 10 - next + '0';
                next = read();
            }
        }

        return val;
    }

    public double rd() {
        return readDouble();
    }

    public double readDouble() {
        boolean sign = true;
        skipBlank();
        if (next == '+' || next == '-') {
            sign = next == '+';
            next = read();
        }

        double val = 0;
        while (next >= '0' && next <= '9') {
            val = val * 10 + next - '0';
            next = read();
        }
        if (next != '.') {
            return sign ? val : -val;
        }
        next = read();
        double radix = 1;
        while (next >= '0' && next <= '9') {
            val = val * 10 + next - '0';
            radix = radix * 10;
            next = read();
        }
        double result = val / radix;
        return sign ? result : -result;
    }

    public char rc() {
        return readChar();
    }

    public char readChar() {
        skipBlank();
        char c = (char) next;
        next = read();
        return c;
    }

    public boolean meetBlank() {
        return next <= 32;
    }

    public String rs() {
        return readString();
    }

    public String readString(StringBuilder builder) {
        skipBlank();

        while (next > 32) {
            builder.append((char) next);
            next = read();
        }

        return builder.toString();
    }

    public String readString() {
        defaultStringBuf.setLength(0);
        return readString(defaultStringBuf);
    }

    public int readLine(char[] data, int offset) {
        int originalOffset = offset;
        while (next != -1 && next != '\n' && next != '\r') {
            data[offset++] = (char) next;
            next = read();
        }
        return offset - originalOffset;
    }

    public void readLine(StringBuilder builder) {
        while (next != -1 && next != '\n' && next != '\r') {
            builder.append((char) next);
            next = read();
        }
    }

    public void skipEmptyLine() {
        while (next == '\r' || next == '\n') {
            next = read();
        }
    }

    public String readLine() {
        defaultStringBuf.setLength(0);
        readLine(defaultStringBuf);
        return defaultStringBuf.toString();
    }

    public int rs(char[] data, int offset) {
        return readString(data, offset);
    }

    public int rs(byte[] data, int offset) {
        return readString(data, offset);
    }

    public int rs(char[] data) {
        return rs(data, 0);
    }

    public int rs(int[] data) {
        return rs(data, 0);
    }

    public int rs(int[] data, int offset) {
        return readString(data, offset);
    }

    public int readString(char[] data, int offset) {
        skipBlank();

        int originalOffset = offset;
        while (next > 32) {
            data[offset++] = (char) next;
            next = read();
        }

        return offset - originalOffset;
    }

    public int readString(int[] data, int offset) {
        skipBlank();

        int originalOffset = offset;
        while (next > 32) {
            data[offset++] = (char) next;
            next = read();
        }

        return offset - originalOffset;
    }

    public int readString(long[] data, int offset) {
        skipBlank();

        int originalOffset = offset;
        while (next > 32) {
            data[offset++] = (char) next;
            next = read();
        }

        return offset - originalOffset;
    }

    public int readString(double[] data, int offset) {
        skipBlank();

        int originalOffset = offset;
        while (next > 32) {
            data[offset++] = (char) next;
            next = read();
        }

        return offset - originalOffset;
    }

    public int readString(byte[] data, int offset) {
        skipBlank();

        int originalOffset = offset;
        while (next > 32) {
            data[offset++] = (byte) next;
            next = read();
        }

        return offset - originalOffset;
    }

    public boolean hasMore() {
        skipBlank();
        return next != -1;
    }

    public InputStream getInputStream() {
        return is;
    }
}
