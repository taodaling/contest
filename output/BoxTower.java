import java.util.Arrays;
import java.util.Random;

public class BoxTower {
    IntegerGenericBIT bit = new IntegerGenericBIT(50, Math::max, 0);
    int[] dm;
    Box[] boxes;
    Box[] sorted;
    int ans = 0;

    public int tallestTower(int[] x, int[] y, int[] z) {
        int n = x.length;
        boxes = new Box[n];
        IntegerList list = new IntegerList();
        list.addAll(x);
        list.addAll(y);
        list.addAll(z);
        list.add(0);
        list.unique();
        dm = list.toArray();
        for (int i = 0; i < n; i++) {
            x[i] = list.binarySearch(x[i]);
            y[i] = list.binarySearch(y[i]);
            z[i] = list.binarySearch(z[i]);
        }

        for (int i = 0; i < n; i++) {
            boxes[i] = new Box();
            boxes[i].xyz[0] = x[i];
            boxes[i].xyz[1] = y[i];
            boxes[i].xyz[2] = z[i];
            Randomized.shuffle(boxes[i].xyz);
        }
        sorted = boxes.clone();

        dfs(n - 1, 0);
        return ans;
    }

    public void solve() {
        int n = boxes.length;
        for (int i = 0; i < n; i++) {
            if (sorted[i].xyz[0] < sorted[i].xyz[1]) {
                sorted[i].min = sorted[i].xyz[0];
                sorted[i].max = sorted[i].xyz[1];
            } else {
                sorted[i].min = sorted[i].xyz[1];
                sorted[i].max = sorted[i].xyz[0];
            }
        }

        Arrays.sort(sorted, (a, b) -> a.max == b.max ? a.min - b.min : a.max - b.max);
        for (Box b : sorted) {
            int h = bit.query(b.min) + dm[(b.xyz[2])];
            bit.update(b.min, h);
        }
        ans = Math.max(ans, bit.query(50));
        bit.clear();
    }

    public void dfs(int n, int sum) {
        if (n == -1) {
            if (sum > ans) {
                solve();
            }
            return;
        }
        for (int i = 2; i >= 0; i--) {
            SequenceUtils.swap(boxes[n].xyz, i, 2);
            dfs(n - 1, sum + dm[(boxes[n].xyz[2])]);
        }
    }

}

interface IntegerBinaryFunction {
    int apply(int a, int b);

}

class IntegerList implements Cloneable {
    private int size;
    private int cap;
    private int[] data;
    private static final int[] EMPTY = new int[0];

    public IntegerList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new int[cap];
        }
    }

    public IntegerList(IntegerList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public IntegerList() {
        this(0);
    }

    public void ensureSpace(int req) {
        if (req > cap) {
            while (cap < req) {
                cap = Math.max(cap + 10, 2 * cap);
            }
            data = Arrays.copyOf(data, cap);
        }
    }

    public void add(int x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(int[] x) {
        addAll(x, 0, x.length);
    }

    public void addAll(int[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(IntegerList list) {
        addAll(list.data, 0, list.size);
    }

    public void sort() {
        if (size <= 1) {
            return;
        }
        Randomized.shuffle(data, 0, size);
        Arrays.sort(data, 0, size);
    }

    public void unique() {
        if (size <= 1) {
            return;
        }

        sort();
        int wpos = 1;
        for (int i = 1; i < size; i++) {
            if (data[i] != data[wpos - 1]) {
                data[wpos++] = data[i];
            }
        }
        size = wpos;
    }

    public int binarySearch(int x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public int[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public String toString() {
        return Arrays.toString(toArray());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IntegerList)) {
            return false;
        }
        IntegerList other = (IntegerList) obj;
        return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
    }

    public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) {
            h = h * 31 + Integer.hashCode(data[i]);
        }
        return h;
    }

    public IntegerList clone() {
        IntegerList ans = new IntegerList();
        ans.addAll(this);
        return ans;
    }

}

class IntegerVersionArray {
    int[] data;
    int[] version;
    int now;
    int def;

    public IntegerVersionArray(int cap) {
        this(cap, 0);
    }

    public IntegerVersionArray(int cap, int def) {
        data = new int[cap];
        version = new int[cap];
        now = 0;
        this.def = def;
    }

    public void clear() {
        now++;
    }

    public void visit(int i) {
        if (version[i] < now) {
            version[i] = now;
            data[i] = def;
        }
    }

    public void set(int i, int v) {
        version[i] = now;
        data[i] = v;
    }

    public int get(int i) {
        visit(i);
        return data[i];
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            if (version[i] < now) {
                continue;
            }
            builder.append(i).append(':').append(data[i]).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

}

class IntegerGenericBIT {
    private IntegerVersionArray data;
    private int n;
    private IntegerBinaryFunction merger;
    private int unit;

    public IntegerGenericBIT(int n, IntegerBinaryFunction merger, int unit) {
        this.n = n;
        data = new IntegerVersionArray(n + 1, unit);
        this.merger = merger;
        this.unit = unit;
        clear();
    }

    public int query(int i) {
        int sum = unit;
        for (; i > 0; i -= i & -i) {
            sum = merger.apply(sum, data.get(i));
        }
        return sum;
    }

    public void update(int i, int mod) {
        if (i <= 0) {
            return;
        }
        for (; i <= n; i += i & -i) {
            data.set(i, merger.apply(data.get(i), mod));
        }
    }

    public void clear() {
        data.clear();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            builder.append(query(i)).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

}

class RandomWrapper {
    private Random random;
    public static RandomWrapper INSTANCE = new RandomWrapper(new Random());

    public RandomWrapper() {
        this(new Random());
    }

    public RandomWrapper(Random random) {
        this.random = random;
    }

    public int nextInt(int l, int r) {
        return random.nextInt(r - l + 1) + l;
    }

}

class SequenceUtils {
    public static void swap(int[] data, int i, int j) {
        int tmp = data[i];
        data[i] = data[j];
        data[j] = tmp;
    }

    public static boolean equal(int[] a, int al, int ar, int[] b, int bl, int br) {
        if ((ar - al) != (br - bl)) {
            return false;
        }
        for (int i = al, j = bl; i <= ar; i++, j++) {
            if (a[i] != b[j]) {
                return false;
            }
        }
        return true;
    }

}

class Randomized {
    public static void shuffle(int[] data) {
        shuffle(data, 0, data.length - 1);
    }

    public static void shuffle(int[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            int tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static int nextInt(int l, int r) {
        return RandomWrapper.INSTANCE.nextInt(l, r);
    }

}

class Box {
    int[] xyz = new int[3];
    int max;
    int min;

}
