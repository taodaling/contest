import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.UncheckedIOException;
import java.io.Closeable;
import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 29);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            VeryShortProblem solver = new VeryShortProblem();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class VeryShortProblem {
        Debug debug = new Debug(false);
        boolean error = false;
        String s;
        int offset;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            try {
                BufferedReader bis = new BufferedReader(new InputStreamReader(in.getInputStream()));
                int step = 0;
                while (true) {
                    step++;

                    if (step >= 10000) {
                        //  while(true) {}
                    }
                    //in.skipOneEmptyLine();

                    String number = bis.readLine();
                    debug.debug("number", number);
                    if (number.equals("#")) {
                        break;
                    }
                    int digit = Integer.parseInt(bis.readLine());

                    setString(number);
                    BigDecimal value = null;
                    try {
                        value = readRealNumber();
                    } catch (IllegalArgumentException e) {
                    }
                    if (!isGoodEnd()) {
                        out.println("Not a floating point number");
                        continue;
                    }

                    value = value.setScale(digit, RoundingMode.DOWN);
                    if (value.equals(BigDecimal.ZERO)) {
                        value = BigDecimal.ZERO;
                    }
                    StringBuilder builder = new StringBuilder();
                    builder.append(value.toPlainString());
                    if (digit > 0) {
                        int addition = digit;
                        int index = builder.indexOf(".");
                        if (index < 0) {
                            builder.append(".");
                        } else {
                            addition = digit - (builder.length() - 1 - index);
                        }
                        for (int i = 0; i < addition; i++) {
                            builder.append('0');
                        }
                    }
                    if (builder.length() >= 1000) {
                        while (true) {
                        }
                    }
                    out.println(builder);
                }
            } catch (IOException e) {
            }
        }

        public boolean isDigit() {
            return peek() >= '0' && peek() <= '9';
        }

        public int readDigit() {
            addAssert(isDigit());
            return read() - '0';
        }

        public boolean isUnsignedIntegerNumber() {
            return isDigit();
        }

        public String readUnsignedIntegerNumber() {
            addAssert(isUnsignedIntegerNumber());
            StringBuilder builder = new StringBuilder();
            while (isDigit()) {
                builder.append(readDigit());
            }
            return builder.toString();
        }

        public boolean isSign() {
            return peek() == '+' || peek() == '-';
        }

        public int readSign() {
            return read() == '+' ? 1 : -1;
        }

        public boolean isIntegerNumber() {
            return isUnsignedIntegerNumber() || isSign();
        }

        public void addAssert(boolean val) {
            if (!val) {
                error = true;
                throw new IllegalArgumentException();
            }
        }

        public BigInteger readIntegerNumber() {
            addAssert(isIntegerNumber());
            if (isUnsignedIntegerNumber()) {
                return new BigInteger(readUnsignedIntegerNumber());
            }
            int sign = readSign();
            BigInteger ans = new BigInteger(readUnsignedIntegerNumber());
            if (sign < 0) {
                ans = ans.negate();
            }
            return ans;
        }

        public boolean isExponentSymbol() {
            return peek() == 'e' || peek() == 'E';
        }

        public void readExponentSymbol() {
            addAssert(isExponentSymbol());
            read();
        }

        public boolean isExponent() {
            return isExponentSymbol();
        }

        public BigDecimal readExponent() {
            addAssert(isExponent());
            readExponentSymbol();
            BigInteger val = readIntegerNumber();
            BigInteger unsigned = val.abs();
            int sign = val.signum();
            if (unsigned.compareTo(BigInteger.valueOf(300)) >= 0) {
                unsigned = BigInteger.valueOf(300);
            }
            if (sign >= 0) {
                return BigDecimal.valueOf(10).pow(unsigned.intValue());
            }
            return new BigDecimal("0.1").pow(unsigned.intValue());
        }

        public boolean isSimpleUnsignedRealNumber() {
            return isUnsignedIntegerNumber() || peek() == '.';
        }

        public BigDecimal readFractionPart() {
            read();
            return new BigDecimal("0." + readUnsignedIntegerNumber());
        }

        public BigDecimal readSimpleUnsignedRealNumber() {
            addAssert(isSimpleUnsignedRealNumber());
            if (peek() == '.') {
                return readFractionPart();
            }
            BigDecimal intPart = new BigDecimal(readUnsignedIntegerNumber());
            if (peek() == '.') {
                intPart = intPart.add(readFractionPart());
            }
            return intPart;
        }

        public boolean isSimpleRealNumber() {
            return isSimpleUnsignedRealNumber() || isSign();
        }

        public BigDecimal readSimpleRealNumber() {
            addAssert(isSimpleRealNumber());
            if (isSimpleUnsignedRealNumber()) {
                return readSimpleUnsignedRealNumber();
            }
            int sign = readSign();
            BigDecimal value = readSimpleUnsignedRealNumber();
            if (sign < 0) {
                value = value.negate();
            }
            return value;
        }

        public BigDecimal readRealNumber() {
            BigDecimal real = readSimpleRealNumber();
            if (isExponent()) {
                real = real.multiply(readExponent());
            }
            return real;
        }

        public boolean isGoodEnd() {
            return !error && offset == s.length();
        }

        public void setString(String s) {
            this.s = s;
            offset = 0;
            error = false;
        }

        public int peek() {
            return offset >= s.length() ? -1 : s.charAt(offset);
        }

        public int read() {
            return s.charAt(offset++);
        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
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

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
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

        public FastOutput println(String c) {
            return append(c).println();
        }

        public FastOutput println(Object c) {
            return append(c).println();
        }

        public FastOutput println() {
            return append('\n');
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

        public void close() {
            flush();
            try {
                writer.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }

    static class Debug {
        private boolean offline;
        private PrintStream out = System.err;

        public Debug(boolean enable) {
            offline = enable && System.getSecurityManager() == null;
        }

        public Debug debug(String name, String x) {
            if (offline) {
                out.printf("%s=%s", name, x);
                out.println();
            }
            return this;
        }

    }

    static class FastInput {
        private final InputStream is;

        public FastInput(InputStream is) {
            this.is = is;
        }

        public InputStream getInputStream() {
            return is;
        }

    }
}

