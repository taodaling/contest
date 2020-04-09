import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Deque;
import java.util.ArrayList;
import java.io.UncheckedIOException;
import java.util.List;
import java.io.Closeable;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.util.ArrayDeque;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
    public static void main(String[] args) throws Exception {
        new TaskAdapter().run();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            Travel solver = new Travel();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Travel {
        HashMap<String, Integer> boyNames = new HashMap<>(800);
        HashMap<String, Integer> girlNames = new HashMap<>(800);
        List<String> boys = new ArrayList<>(800);
        List<String> girls = new ArrayList<>(800);

        public int readBoyIndex(FastInput in) {
            String name = in.readString();
            Integer index = boyNames.get(name);
            if (index == null) {
                index = boys.size();
                boys.add(name);
                boyNames.put(name, index);
            }
            return index;
        }

        public int readGirlIndex(FastInput in) {
            String name = in.readString();
            Integer index = girlNames.get(name);
            if (index == null) {
                index = girls.size();
                girls.add(name);
                girlNames.put(name, index);
            }
            return index;
        }

        public void solve(int testNumber, FastInput in, FastOutput out) {
            int n = in.readInt();
            int[][] boyPref = new int[n][n];
            int[][] girlPref = new int[n][n];
            for (int i = 0; i < n; i++) {
                int index = readBoyIndex(in);
                for (int j = 0; j < n; j++) {
                    boyPref[index][readGirlIndex(in)] = n - j;
                }
            }
            for (int i = 0; i < n; i++) {
                int index = readGirlIndex(in);
                for (int j = 0; j < n; j++) {
                    girlPref[index][readBoyIndex(in)] = n - j;
                }
            }

            out.println("YES");
            StableMarriage sm = new StableMarriage(boyPref, girlPref);
            for (int i = 0; i < n; i++) {
                out.append(boys.get(i)).append(' ').append(girls.get(sm.wifeOf(i))).println();
            }
        }

    }

    static class StableMarriage {
        private StableMarriage.Girl[] girls;
        private StableMarriage.Boy[] boys;

        public StableMarriage(final int[][] boyFavors, final int[][] girlFavors) {
            int n = boyFavors.length;
            boys = new StableMarriage.Boy[n];
            girls = new StableMarriage.Girl[n];
            for (int i = 0; i < n; i++) {
                girls[i] = new StableMarriage.Girl();
                girls[i].id = i;
            }
            for (int i = 0; i < n; i++) {
                final StableMarriage.Boy boy = new StableMarriage.Boy();
                boy.id = i;
                Arrays.sort(girls, (a, b) -> -Integer.compare(boyFavors[boy.id][a.id], boyFavors[boy.id][b.id]));
                boy.remainChoices.addAll(Arrays.asList(girls));
                boys[i] = boy;
            }
            Arrays.sort(girls, (a, b) -> Integer.compare(a.id, b.id));

            Deque<StableMarriage.Boy> unmarried = new ArrayDeque<>(Arrays.asList(boys));
            while (!unmarried.isEmpty()) {
                StableMarriage.Boy head = unmarried.removeFirst();
                StableMarriage.Girl girl = head.remainChoices.removeFirst();
                if (girl.fere == null) {
                    combine(head, girl);
                } else if (girlFavors[girl.id][girl.fere.id] < girlFavors[girl.id][head.id]) {
                    girl.fere.fere = null;
                    unmarried.addLast(girl.fere);
                    combine(head, girl);
                } else {
                    unmarried.addFirst(head);
                }
            }
        }

        public int wifeOf(int id) {
            return boys[id].fere.id;
        }

        private void combine(StableMarriage.Boy boy, StableMarriage.Girl girl) {
            boy.fere = girl;
            girl.fere = boy;
        }

        private static class Girl {
            StableMarriage.Boy fere;
            int id;

        }

        private static class Boy {
            StableMarriage.Girl fere;
            Deque<StableMarriage.Girl> remainChoices = new ArrayDeque<>();
            int id;

        }

    }

    static class FastOutput implements AutoCloseable, Closeable, Appendable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput append(CharSequence csq) {
            cache.append(csq);
            return this;
        }

        public FastOutput append(CharSequence csq, int start, int end) {
            cache.append(csq, start, end);
            return this;
        }

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

        public FastOutput println(String c) {
            return append(c).println();
        }

        public FastOutput println() {
            cache.append(System.lineSeparator());
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

    static class FastInput {
        private final InputStream is;
        private StringBuilder defaultStringBuf = new StringBuilder(1 << 13);
        private byte[] buf = new byte[1 << 20];
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
}

