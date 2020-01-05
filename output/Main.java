import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Stream;
import java.io.Closeable;
import java.util.Map;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "", 1 << 27);
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
            C1MadhouseEasyVersion solver = new C1MadhouseEasyVersion();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class C1MadhouseEasyVersion {
        FastInput in;
        FastOutput out;

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            this.in = in;
            this.out = out;

            if (n == 1) {
                int[] ans = new int[2];
                read(1, 1, ans);
                answer(ans);
                return;
            }

            int[] ans = new int[n + 1];

            int m = n / 2;
            read(1, m, ans);
            read(m + 1, n, ans);

            int[] buf = new int[n + 1];
            if (isPalindrome(ans, 1, m) && isPalindrome(ans, m + 1, n)) {
            } else if (isPalindrome(ans, 1, m)) {
                int l = m + 1;
                int r = n;
                while (ans[l] == ans[r]) {
                    l++;
                    r--;
                }

                read(l, l, buf);
                if (ans[l] != buf[l]) {
                    SequenceUtils.reverse(ans, m + 1, n);
                }
            } else if (isPalindrome(ans, m + 1, n)) {
                int l = 1;
                int r = m;
                while (ans[l] == ans[r]) {
                    l++;
                    r--;
                }

                read(l, l, buf);
                if (ans[l] != buf[l]) {
                    SequenceUtils.reverse(ans, 1, m);
                }
            } else {
                int l1 = 1;
                int r1 = m;
                int l2 = m + 1;
                int r2 = n;
                while (ans[l1] == ans[r1]) {
                    l1++;
                    r1--;
                }
                while (ans[l2] == ans[r2]) {
                    l2++;
                    r2--;
                }
                read(l1, l2, buf);
                if (equal(buf, ans, l1, m)) {
                } else if (invEqual(buf, ans, l1, m, l1, l2)) {
                    SequenceUtils.reverse(buf, l1, l2);
                } else {
                    SequenceUtils.reverse(ans, 1, m);
                    if (equal(buf, ans, l1, m)) {
                    } else if (invEqual(buf, ans, l1, m, l1, l2)) {
                        SequenceUtils.reverse(buf, l1, l2);
                    }
                }

                if (ans[r2] != buf[r2]) {
                    SequenceUtils.reverse(ans, m + 1, n);
                }
            }

            answer(ans);
        }

        public boolean equal(int[] seq, int[] ans, int l, int r) {
            for (int i = l; i <= r; i++) {
                if (seq[i] != ans[i]) {
                    return false;
                }
            }
            return true;
        }

        public boolean invEqual(int[] seq, int[] ans, int l, int r, int ll, int rr) {
            seq = seq.clone();
            SequenceUtils.reverse(seq, ll, rr);
            return equal(seq, ans, l, r);
        }

        public void answer(int[] seq) {
            out.append("! ");
            for (int i = 1; i < seq.length; i++) {
                out.append((char) ('a' + seq[i]));
            }
            out.flush();
        }

        public boolean isPalindrome(int[] seq, int l, int r) {
            return l > r ? true : (seq[l] == seq[r] && isPalindrome(seq, l + 1, r - 1));
        }

        public void read(int l, int r, int[] ans) {
            int n = r - l + 1;
            int cnt = n * (n - 1) / 2 + n;

            out.printf("? %d %d", l, r).println().flush();

            List<Metadata> metadataList = new ArrayList<>(cnt);
            for (int i = 0; i < cnt; i++) {
                metadataList.add(read());
            }

            Map<Integer, List<Metadata>> groupBySum = metadataList.stream().collect(Collectors.groupingBy(x -> x.sum));
            int[] buf = new int[n];
            Metadata whole = groupBySum.get(n).get(0);
            if (l == r) {
                for (int i = 0; i < whole.cnts.length; i++) {
                    if (whole.cnts[i] != 0) {
                        ans[l] = i;
                        return;
                    }
                }
            }

            Metadata left = groupBySum.get(n - 1).get(0);
            Metadata right = groupBySum.get(n - 1).get(1);
            buf[0] = whole.index(right);
            buf[n - 1] = whole.index(left);
            for (int i = n - 2; n - i - 1 <= i; i--) {
                List<Metadata> list = groupBySum.get(i);
                Metadata lPart = null;
                Metadata rPart = null;
                for (Metadata data : list) {
                    if (left.differ(data) == 1) {
                        lPart = data;
                        break;
                    }
                }
                for (Metadata data : list) {
                    if (data != lPart && right.differ(data) == 1) {
                        rPart = data;
                        break;
                    }
                }

                buf[n - i - 1] = right.index(lPart);
                buf[i] = left.index(rPart);
                left.dec(buf[i]);
                right.dec(buf[n - i - 1]);
            }

            for (int i = 0; i < n; i++) {
                ans[i + l] = buf[i];
            }

            System.err.print("" + l + "," + r + "=");
            for (int i = l; i <= r; i++) {
                System.err.append("" + (char) (ans[i] + 'a'));
            }
            System.err.println();
        }

        public Metadata read() {
            String s = in.readString();
            return new Metadata(s);
        }

    }

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
        private byte[] buf = new byte[1 << 13];
        private int bufLen;
        private int bufOffset;
        private int next;

        public FastInput(InputStream is) {
            this.is = is;
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

    }

    static class Metadata {
        int[] cnts = new int['z' - 'a' + 1];
        int sum;

        public Metadata(String s) {
            int n = s.length();
            for (int i = 0; i < n; i++) {
                cnts[s.charAt(i) - 'a']++;
            }
            for (int x : cnts) {
                sum += x;
            }
        }

        public void dec(int i) {
            sum--;
            cnts[i]--;
        }

        public int differ(Metadata x) {
            int ans = 0;
            for (int i = 0; i < cnts.length; i++) {
                ans += Math.abs(cnts[i] - x.cnts[i]);
            }
            return ans;
        }

        public int index(Metadata a) {
            for (int i = 0; i < cnts.length; i++) {
                if (cnts[i] != a.cnts[i]) {
                    return i;
                }
            }
            return -1;
        }

    }

    static class SequenceUtils {
        public static void swap(int[] data, int i, int j) {
            int tmp = data[i];
            data[i] = data[j];
            data[j] = tmp;
        }

        public static void reverse(int[] data, int l, int r) {
            while (l < r) {
                swap(data, l, r);
                l++;
                r--;
            }
        }

    }

    static class FastOutput implements AutoCloseable, Closeable {
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

        public FastOutput append(String c) {
            cache.append(c);
            return this;
        }

        public FastOutput printf(String format, Object... args) {
            cache.append(String.format(format, args));
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

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }
}

