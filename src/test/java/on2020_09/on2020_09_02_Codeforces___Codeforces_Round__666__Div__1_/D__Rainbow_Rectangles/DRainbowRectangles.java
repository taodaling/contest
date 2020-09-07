package on2020_09.on2020_09_02_Codeforces___Codeforces_Round__666__Div__1_.D__Rainbow_Rectangles;



import template.datastructure.Range2DequeAdapter;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Modular;
import template.primitve.generated.datastructure.IntToLongFunction;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongPreSum;

import java.util.Arrays;
import java.util.TreeSet;

public class DRainbowRectangles {
    int mod = (int) 1e9 + 7;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int l = in.readInt();

        Point[] pts = new Point[n];
        IntegerArrayList xs = new IntegerArrayList();
        IntegerArrayList ys = new IntegerArrayList();
        for (int i = 0; i < n; i++) {
            pts[i] = new Point();
            pts[i].x = in.readInt();
            pts[i].y = in.readInt();
            pts[i].c = in.readInt() - 1;
            xs.add(pts[i].x);
            ys.add(pts[i].y);
        }
        xs.add(0);
        xs.add(l - 1);
        ys.add(0);
        ys.add(l - 1);
        xs.unique();
        ys.unique();

        for (Point pt : pts) {
            pt.x = xs.binarySearch(pt.x);
            pt.y = ys.binarySearch(pt.y);
        }

        int[] xArrays = xs.toArray();
        int m = xArrays.length;
        int[] yArrays = ys.toArray();

        int[] up = new int[yArrays.length];
        int[] bot = new int[yArrays.length];
        for (int i = 0; i < bot.length; i++) {
            if (i == 0) {
                bot[i] = 1;
            } else {
                bot[i] = yArrays[i] - yArrays[i - 1];
            }

            if (i == bot.length - 1) {
                up[i] = 1;
            } else {
                up[i] = yArrays[i + 1] - yArrays[i];
            }
        }

        int[] left = new int[m];
        int[] right = new int[m];
        for (int i = 0; i < m; i++) {
            if (i == 0) {
                left[i] = 1;
            } else {
                left[i] = xArrays[i] - xArrays[i - 1];
            }

            if (i == m - 1) {
                right[i] = 1;
            } else {
                right[i] = xArrays[i + 1] - xArrays[i];
            }
        }
        LongPreSum rPs = new LongPreSum(i -> right[i], right.length);

        Point[] ptsSortByY = pts.clone();
        Point[] ptsSortByX = pts.clone();
        Arrays.sort(ptsSortByY, (a, b) -> Integer.compare(a.y, b.y));
        Arrays.sort(ptsSortByX, (a, b) -> Integer.compare(a.x, b.x));
        TreeSet<Point>[] sets = new TreeSet[k];
        for (int i = 0; i < k; i++) {
            sets[i] = new TreeSet<>((a, b) -> a.x == b.x ? Integer.compare(a.y, b.y) : Integer.compare(a.x, b.x));
        }

        int[] next = new int[m];
        long[] remains = new long[m];
        Counter counter = new Counter(k);

        SegmentBeat sb = new SegmentBeat(0, m - 1);
        long ans = 0;
        for (int i = 0; i < yArrays.length; i++) {
            for (TreeSet<Point> set : sets) {
                set.clear();
            }

            //calc remain
            counter.clear();
            Range2DequeAdapter<Point> tail = new Range2DequeAdapter<>(x -> ptsSortByX[x], 0, ptsSortByX.length - 1);
            Range2DequeAdapter<Point> head = new Range2DequeAdapter<>(x -> ptsSortByX[x], 0, ptsSortByX.length - 1);
            int last = 0;
            for (int j = 0; j < m; j++) {
                while (!head.isEmpty() && head.peekFirst().x < j) {
                    Point h = head.removeFirst();
                    if (h.y < i) {
                        continue;
                    }
                    counter.remove(h.c);
                }
                while (!tail.isEmpty() && counter.distinct < k) {
                    Point h = tail.removeFirst();
                    if (h.y < i) {
                        continue;
                    }
                    last = h.x;
                    counter.add(h.c);
                }
                if (counter.distinct == k) {
                    next[j] = last;
                } else {
                    next[j] = m;
                }
                remains[j] = rPs.post(next[j]);
            }
            for (Point pt : pts) {
                if (pt.y < i) {
                    continue;
                }
                sets[pt.c].add(pt);
            }

            sb.init(0, m - 1, x -> left[x], x -> remains[x]);
            Range2DequeAdapter<Point> top = new Range2DequeAdapter<>(x -> ptsSortByY[x], 0, ptsSortByY.length - 1);
            for (int j = yArrays.length - 1; j >= i; j--) {
                while (!top.isEmpty() && top.peekLast().y > j) {
                    Point back = top.removeLast();
                    sets[back.c].remove(back);
                    Point floor = sets[back.c].floor(back);
                    Point ceil = sets[back.c].ceiling(back);
                    int from = floor == null ? 0 : floor.x + 1;
                    long min = ceil == null ? 0 : rPs.post(ceil.x);
                    sb.updateMin(from, m - 1, 0, m - 1, min);
                }
                //calc ans
                long contrib = sb.querySum(0, m - 1, 0, m - 1) % mod * up[j] % mod * bot[i] % mod;
                ans += contrib;
            }
        }

        ans %= mod;
        out.println(ans);
    }
}

class Counter {
    int[] cnts;
    int distinct;

    public Counter(int n) {
        cnts = new int[n];
    }

    public void clear() {
        Arrays.fill(cnts, 0);
        distinct = 0;
    }

    public void add(int x) {
        cnts[x]++;
        if (cnts[x] == 1) {
            distinct++;
        }
    }

    public void remove(int x) {
        cnts[x]--;
        if (cnts[x] == 0) {
            distinct--;
        }
    }
}

class SegmentBeat implements Cloneable {
    private SegmentBeat left;
    private SegmentBeat right;
    private long first;
    private long second;
    private long firstCnt;
    private static long inf = (long) 2e18;
    private static int mod = (int) 1e9 + 7;
    private static Modular modular = new Modular(mod);

    private long sum;

    private void setMin(long x) {
        if (first <= x) {
            return;
        }
        sum -= (first - x) * firstCnt;
        sum = modular.valueOf(sum);
        first = x;
    }

    public void pushUp() {
        first = Math.max(left.first, right.first);
        second = Math.max(left.first == first ? left.second : left.first, right.first == first ? right.second : right.first);
        firstCnt = (left.first == first ? left.firstCnt : 0) + (right.first == first ? right.firstCnt : 0);
        sum = (left.sum + right.sum) % mod;
    }

    public void pushDown() {
        left.setMin(first);
        right.setMin(first);
    }

    public SegmentBeat(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new SegmentBeat(l, m);
            right = new SegmentBeat(m + 1, r);
            pushUp();
        } else {
        }
    }

    public void init(int l, int r, IntToLongFunction size, IntToLongFunction remain) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.init(l, m, size, remain);
            right.init(m + 1, r, size, remain);
            pushUp();
        } else {
            first = remain.apply(l);
            second = -inf;
            firstCnt = size.apply(l);
            sum = first * firstCnt % mod;
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void updateMin(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            if (first <= x) {
                return;
            }
            if (second < x) {
                setMin(x);
                return;
            }
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updateMin(ll, rr, l, m, x);
        right.updateMin(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long querySum(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.querySum(ll, rr, l, m) +
                right.querySum(ll, rr, m + 1, r);
    }

    public long queryMax(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return -inf;
        }
        if (covered(ll, rr, l, r)) {
            return first;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.max(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }

    private SegmentBeat deepClone() {
        SegmentBeat seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected SegmentBeat clone() {
        try {
            return (SegmentBeat) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(sum).append(",");
            return;
        }
        pushDown();
        left.toString(builder);
        right.toString(builder);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        deepClone().toString(builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}


class Point {
    int x;
    int y;
    int c;
}


