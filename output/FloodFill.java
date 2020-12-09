import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Comparator;

public class FloodFill {
    public int[] getCell(int[] X, int[] Y, long A) {
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < 10; j++) {
//                int time = (int)1e9;
//                for(int t = 0; t < X.length; t++){
//                    time = Math.min(Math.max(Math.abs(X[t] - i),
//                            Math.abs(Y[t] - j)), time);
//                }
//                if(time == 2){
//                    System.err.append('*');
//                }else{
//                    System.err.append('.');
//                }
//            }
//            System.err.println();
//        }

        int n = X.length;
        long finalA = A;
        IntBinarySearch bs = new IntBinarySearch() {

            public boolean check(int mid) {
                RectUnionArea.Rect[] rects = new RectUnionArea.Rect[n];
                for (int i = 0; i < n; i++) {
                    rects[i] = new RectUnionArea.Rect();
                    rects[i].l = X[i] - mid;
                    rects[i].r = X[i] + mid;
                    rects[i].b = Y[i] - mid;
                    rects[i].t = Y[i] + mid;
                }
                return RectUnionArea.unionArea(rects) - n >= finalA;
            }
        };
        int time = bs.binarySearch(0, (int) 2e8);
        //debug.debug("time", time);
        List<RectUnionArea.Rect> deleted = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            RectUnionArea.Rect r = new RectUnionArea.Rect();
            r.l = X[i] - time + 1;
            r.r = X[i] + time - 1;
            r.b = Y[i] - time + 1;
            r.t = Y[i] + time - 1;
            deleted.add(r);
        }
        A -= RectUnionArea.unionArea(deleted.toArray(new RectUnionArea.Rect[0])) - n;
        Map<Integer, IntegerIntervalMap> cols = new HashMap<>();
        Map<Integer, IntegerIntervalMap> rows = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int l = X[i] - time;
            int r = X[i] + time;
            int b = Y[i] - time;
            int t = Y[i] + time;
            cols.computeIfAbsent(l, x -> new IntegerIntervalMap()).add(b, t + 1);
            cols.computeIfAbsent(r, x -> new IntegerIntervalMap()).add(b, t + 1);
            rows.computeIfAbsent(b, x -> new IntegerIntervalMap()).add(l, r + 1);
            rows.computeIfAbsent(t, x -> new IntegerIntervalMap()).add(l, r + 1);
        }
        for (Map.Entry<Integer, IntegerIntervalMap> col : cols.entrySet()) {
            int x = col.getKey();
            IntegerIntervalMap colMap = col.getValue();
            for (RectUnionArea.Rect d : deleted) {
                if (d.l <= x && d.r >= x) {
                    colMap.remove((int) d.b, (int) d.t + 1);
                }
            }
            for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
                int y = row.getKey();
                IntegerIntervalMap rowMap = row.getValue();
                if (colMap.contain(y, y + 1)) {
                    rowMap.remove(x, x + 1);
                }
            }
        }
        for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
            int y = row.getKey();
            IntegerIntervalMap map = row.getValue();
            for (RectUnionArea.Rect d : deleted) {
                if (d.b <= y && d.t >= y) {
                    map.remove((int) d.l, (int) d.r + 1);
                }
            }
        }

        CountSegment cs = new CountSegment();
        int lower = (int) -1e9;
        int upper = (int) 1e9;
        for (Map.Entry<Integer, IntegerIntervalMap> col : cols.entrySet()) {
            int x = col.getKey();
            IntegerIntervalMap colMap = col.getValue();
            cs.update(x, x, lower, upper, colMap.total());
        }
        for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
            int y = row.getKey();
            IntegerIntervalMap map = row.getValue();
            for (IntegerIntervalMap.Interval interval : map) {
                cs.update(interval.l, interval.r - 1, lower, upper, 1);
            }
        }
        int x = cs.kth(lower, upper, A);
        IntegerIntervalMap now = cols.computeIfAbsent(x, t -> new IntegerIntervalMap());
        for (Map.Entry<Integer, IntegerIntervalMap> row : rows.entrySet()) {
            int y = row.getKey();
            IntegerIntervalMap map = row.getValue();
            if (map.contain(x, x + 1)) {
                now.add(y, y + 1);
            }
        }

        int y = -1;
        A -= cs.kth(lower, x - 1, lower, upper);
        for (IntegerIntervalMap.Interval interval : now) {
            if (interval.length() < A) {
                A -= interval.length();
                continue;
            }
            y = (int) (interval.l + A - 1);
            break;
        }

        return new int[]{x, y};
    }

}

class DigitUtils {
    private DigitUtils() {
    }

    public static int floorAverage(int x, int y) {
        return (x & y) + ((x ^ y) >> 1);
    }

}

class Range2DequeAdapter<T> implements SimplifiedDeque<T> {
    IntFunction<T> data;
    int l;
    int r;

    public Range2DequeAdapter(IntFunction<T> data, int l, int r) {
        this.data = data;
        this.l = l;
        this.r = r;
    }

    public boolean isEmpty() {
        return l > r;
    }

    public T peekFirst() {
        return data.apply(l);
    }

    public T removeFirst() {
        return data.apply(l++);
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int iter = l;


            public boolean hasNext() {
                return iter <= r;
            }


            public T next() {
                return data.apply(iter++);
            }
        };
    }

}

class IntegerIntervalMap implements Iterable<IntegerIntervalMap.Interval> {
    private int total = 0;
    private TreeMap<Integer, IntegerIntervalMap.Interval> map = new TreeMap<>();

    private void add(IntegerIntervalMap.Interval interval) {
        if (interval.length() <= 0) {
            return;
        }
        map.put(interval.l, interval);
        total += interval.length();
    }

    private void remove(IntegerIntervalMap.Interval interval) {
        map.remove(interval.l);
        total -= interval.length();
    }

    public int total() {
        return total;
    }

    public IntegerIntervalMap.Interval floor(int x) {
        Map.Entry<Integer, IntegerIntervalMap.Interval> entry = map.floorEntry(x);
        return entry == null ? null : entry.getValue();
    }

    public boolean contain(int l, int r) {
        IntegerIntervalMap.Interval interval = floor(l);
        return interval != null && interval.r >= r;
    }

    public Iterator<IntegerIntervalMap.Interval> iterator() {
        return map.values().iterator();
    }

    public void add(int l, int r) {
        if (l >= r) {
            return;
        }
        IntegerIntervalMap.Interval interval = new IntegerIntervalMap.Interval();
        interval.l = l;
        interval.r = r;
        while (true) {
            Map.Entry<Integer, IntegerIntervalMap.Interval> ceilEntry = map.ceilingEntry(interval.l);
            if (ceilEntry == null || ceilEntry.getValue().l > interval.r) {
                break;
            }
            IntegerIntervalMap.Interval ceil = ceilEntry.getValue();
            remove(ceil);
            interval.r = Math.max(interval.r, ceil.r);
        }
        while (true) {
            Map.Entry<Integer, IntegerIntervalMap.Interval> floorEntry = map.floorEntry(interval.l);
            if (floorEntry == null || floorEntry.getValue().r < interval.l) {
                break;
            }
            IntegerIntervalMap.Interval floor = floorEntry.getValue();
            remove(floor);
            interval.l = Math.min(interval.l, floor.l);
            interval.r = Math.max(interval.r, floor.r);
        }
        add(interval);
    }

    public void remove(int l, int r) {
        if (l >= r) {
            return;
        }
        while (true) {
            Map.Entry<Integer, IntegerIntervalMap.Interval> ceilEntry = map.ceilingEntry(l);
            if (ceilEntry == null || ceilEntry.getValue().l >= r) {
                break;
            }
            IntegerIntervalMap.Interval ceil = ceilEntry.getValue();
            remove(ceil);
            ceil.l = r;
            add(ceil);
        }
        while (true) {
            Map.Entry<Integer, IntegerIntervalMap.Interval> floorEntry = map.floorEntry(l);
            if (floorEntry == null || floorEntry.getValue().r <= l) {
                break;
            }
            IntegerIntervalMap.Interval floor = floorEntry.getValue();
            remove(floor);
            if (floor.r > r) {
                IntegerIntervalMap.Interval left = floor;
                IntegerIntervalMap.Interval right = new IntegerIntervalMap.Interval();
                right.l = r;
                right.r = left.r;
                left.r = l;
                add(left);
                add(right);
                break;
            }
            floor.r = l;
            add(floor);
        }
    }

    public String toString() {
        return map.values().toString();
    }

    public static class Interval {
        public int l;
        public int r;

        public int length() {
            return r - l;
        }

        public String toString() {
            return "[" + l + "," + r + ")";
        }

    }

}

class RandomWrapper {
    private Random random;
    public static final RandomWrapper INSTANCE = new RandomWrapper(new Random());

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
    public static boolean equal(long[] a, int al, int ar, long[] b, int bl, int br) {
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

class LongArrayList implements Cloneable {
    private int size;
    private int cap;
    private long[] data;
    private static final long[] EMPTY = new long[0];

    public LongArrayList(int cap) {
        this.cap = cap;
        if (cap == 0) {
            data = EMPTY;
        } else {
            data = new long[cap];
        }
    }

    public LongArrayList(long[] data) {
        this(0);
        addAll(data);
    }

    public LongArrayList(LongArrayList list) {
        this.size = list.size;
        this.cap = list.cap;
        this.data = Arrays.copyOf(list.data, size);
    }

    public LongArrayList() {
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

    private void checkRange(int i) {
        if (i < 0 || i >= size) {
            throw new ArrayIndexOutOfBoundsException("Access [" + i + "]");
        }
    }

    public long get(int i) {
        checkRange(i);
        return data[i];
    }

    public void add(long x) {
        ensureSpace(size + 1);
        data[size++] = x;
    }

    public void addAll(long[] x) {
        addAll(x, 0, x.length);
    }

    public void addAll(long[] x, int offset, int len) {
        ensureSpace(size + len);
        System.arraycopy(x, offset, data, size, len);
        size += len;
    }

    public void addAll(LongArrayList list) {
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

    public int binarySearch(long x) {
        return Arrays.binarySearch(data, 0, size, x);
    }

    public int size() {
        return size;
    }

    public long[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public String toString() {
        return Arrays.toString(toArray());
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof LongArrayList)) {
            return false;
        }
        LongArrayList other = (LongArrayList) obj;
        return SequenceUtils.equal(data, 0, size - 1, other.data, 0, other.size - 1);
    }

    public int hashCode() {
        int h = 1;
        for (int i = 0; i < size; i++) {
            h = h * 31 + Long.hashCode(data[i]);
        }
        return h;
    }

    public LongArrayList clone() {
        LongArrayList ans = new LongArrayList();
        ans.addAll(this);
        return ans;
    }

}

interface SimplifiedDeque<T> extends SimplifiedStack<T> {
}

class Randomized {
    public static void shuffle(long[] data, int from, int to) {
        to--;
        for (int i = from; i <= to; i++) {
            int s = nextInt(i, to);
            long tmp = data[i];
            data[i] = data[s];
            data[s] = tmp;
        }
    }

    public static int nextInt(int l, int r) {
        return RandomWrapper.INSTANCE.nextInt(l, r);
    }

}

abstract class IntBinarySearch {
    public abstract boolean check(int mid);

    public int binarySearch(int l, int r) {
        return binarySearch(l, r, false);
    }

    public int binarySearch(int l, int r, boolean lastFalse) {
        if (l > r) {
            throw new IllegalArgumentException();
        }
        while (l < r) {
            int mid = DigitUtils.floorAverage(l, r);
            if (check(mid)) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        if (lastFalse) {
            if (check(l)) {
                l--;
            }
        }
        return l;
    }

}

class ActiveSegment implements Cloneable {
    private ActiveSegment left;
    private ActiveSegment right;
    private long min;
    private long minWeight;
    private long dirty;

    private void modify(long x) {
        min += x;
        dirty += x;
        assert min >= 0;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        minWeight = 0;
        if (min == left.min) {
            minWeight += left.minWeight;
        }
        if (min == right.min) {
            minWeight += right.minWeight;
        }
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public ActiveSegment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new ActiveSegment(l, m);
            right = new ActiveSegment(m + 1, r);
        } else {
        }
    }

    public void init(int l, int r, IntToLongFunction weight) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.init(l, m, weight);
            right.init(m + 1, r, weight);
            pushUp();
        } else {
            min = 0;
            minWeight = weight.apply(l);
        }
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long queryAll() {
        return min == 0 ? minWeight : 0;
    }

    private ActiveSegment deepClone() {
        ActiveSegment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    protected ActiveSegment clone() {
        try {
            return (ActiveSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(min).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

}

interface IntToLongFunction {
    long apply(int x);

}

class CountSegment implements Cloneable {
    private static final CountSegment NIL = new CountSegment();
    private CountSegment left = NIL;
    private CountSegment right = NIL;
    private long sum;
    private long dirty;

    static {
        NIL.left = NIL.right = NIL;
    }

    public void pushUp() {
        sum = left.sum + right.sum;
    }

    public void pushDown(int l, int r) {
        if (left == NIL) {
            left = left.clone();
            right = right.clone();
        }
        if (dirty != 0) {
            int m = DigitUtils.floorAverage(l, r);
            left.modify(l, m, dirty);
            right.modify(m + 1, r, dirty);
            dirty = 0;
        }
    }

    public void modify(int l, int r, long d) {
        assert this != NIL;
        dirty += d;
        sum += (r - l + 1) * d;
    }

    public CountSegment() {
    }

    private boolean enter(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean leave(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int mod) {
        if (leave(ll, rr, l, r)) {
            return;
        }
        if (enter(ll, rr, l, r)) {
            modify(l, r, mod);
            return;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, mod);
        right.update(ll, rr, m + 1, r, mod);
        pushUp();
    }

    public int kth(int l, int r, long k) {
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        pushDown(l, r);
        if (left.sum >= k) {
            return left.kth(l, m, k);
        }
        return right.kth(m + 1, r, k - left.sum);
    }

    public long kth(int ll, int rr, int l, int r) {
        if (leave(ll, rr, l, r)) {
            return 0;
        }
        if (enter(ll, rr, l, r)) {
            return sum;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        return left.kth(ll, rr, l, m) +
                right.kth(ll, rr, m + 1, r);
    }

    public CountSegment clone() {
        try {
            return (CountSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}

class RectUnionArea {
    public static long unionArea(RectUnionArea.Rect[] rects) {
        LongArrayList list = new LongArrayList(rects.length * 2);
        for (RectUnionArea.Rect r : rects) {
            list.add(r.l);
            list.add(r.r + 1);
        }
        list.unique();
        int m = list.size();
        RectUnionArea.Rect[] sortByB = rects.clone();
        RectUnionArea.Rect[] sortByT = rects.clone();
        Arrays.sort(sortByB, Comparator.comparingLong(x -> x.b));
        Arrays.sort(sortByT, Comparator.comparingLong(x -> x.t));
        Range2DequeAdapter<RectUnionArea.Rect> dqByB = new Range2DequeAdapter<>(i -> sortByB[i], 0, sortByB.length - 1);
        Range2DequeAdapter<RectUnionArea.Rect> dqByT = new Range2DequeAdapter<>(i -> sortByT[i], 0, sortByT.length - 1);
        ActiveSegment as = new ActiveSegment(0, m - 2);
        as.init(0, m - 2, i -> list.get(i + 1) - list.get(i));
        long sum = as.queryAll();
        long last = 0;
        long ans = 0;
        while (!dqByT.isEmpty()) {
            long now = dqByT.peekFirst().t + 1;
            if (!dqByB.isEmpty()) {
                now = Math.min(now, dqByB.peekFirst().b);
            }
            ans += (sum - as.queryAll()) * (now - last);
            while (!dqByB.isEmpty() && dqByB.peekFirst().b == now) {
                RectUnionArea.Rect head = dqByB.removeFirst();
                as.update(list.binarySearch(head.l), list.binarySearch(head.r + 1) - 1, 0, m - 2, 1);
            }
            while (!dqByT.isEmpty() && dqByT.peekFirst().t + 1 == now) {
                RectUnionArea.Rect head = dqByT.removeFirst();
                as.update(list.binarySearch(head.l), list.binarySearch(head.r + 1) - 1, 0, m - 2, -1);
            }
            last = now;
        }
        return ans;
    }

    public static class Rect {
        public long l;
        public long r;
        public long b;
        public long t;

    }

}

interface SimplifiedStack<T> extends Iterable<T> {
}
