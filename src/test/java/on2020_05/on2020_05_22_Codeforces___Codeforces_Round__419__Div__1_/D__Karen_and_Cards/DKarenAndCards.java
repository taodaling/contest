package on2020_05.on2020_05_22_Codeforces___Codeforces_Round__419__Div__1_.D__Karen_and_Cards;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntToIntegerFunction;

import java.util.Arrays;

public class DKarenAndCards {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] limits = new int[3];
        in.populate(limits);
        int[][] cards = new int[n + 1][3];
        for (int i = 0; i < n; i++) {
            in.populate(cards[i]);
        }
        cards[n][2] = limits[2];
        n++;
        Arrays.sort(cards, (a, b) -> Integer.compare(a[2], b[2]));
        int[][] suffix = new int[n][2];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 2; j++) {
                suffix[i][j] = cards[i][j];
                if (i + 1 < n) {
                    suffix[i][j] = Math.max(suffix[i][j], suffix[i + 1][j]);
                }
            }
        }

        SegmentPQ pq = new SegmentPQ(1, limits[0], i -> 0);
        Segment seg = new Segment(1, limits[0]);
        PeekResult pr = new PeekResult();
        QueryResult qr = new QueryResult();
        int last = 0;
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int r = i;
            while (r + 1 < n && cards[r + 1][2] == cards[r][2]) {
                r++;
            }

            //transfer
            {
                int y = suffix[i][1] + 1;
                while (true) {
                    pr.reset();
                    pq.peek(1, limits[0], 1, limits[0], pr);
                    if (pr.empty || pr.val < y) {
                        break;
                    }
                    pq.pop(pr.index, pr.index, 1, limits[0]);
                    seg.active(pr.index, pr.index, 1, limits[0], pr.val);
                }
            }
            {
                int x = suffix[i][0] + 1;
                int y = suffix[i][1] + 1;
                qr.reset();
                seg.query(x, limits[0], 1, limits[0], qr);
                long intersect = qr.sum - (long) (y - 1) * qr.size;
                long area = (long) (limits[0] - x + 1) * (limits[1] - y + 1);
                ans += (area - intersect) * (cards[i][2] - last);
                last = cards[i][2];
            }
            //update
            {
                for (int j = i; j <= r; j++) {
                    int x = cards[j][0];
                    int y = cards[j][1];
                    pq.update(1, x, 1, limits[0], y);
                    seg.update(1, x, 1, limits[0], y);
                }
            }
            i = r;
        }

        out.println(ans);
    }
}

class PeekResult {
    int index;
    int val;
    boolean empty;

    public void reset() {
        index = -1;
        val = Integer.MIN_VALUE;
        empty = true;
    }

    public void update(int index, int val) {
        if (val > this.val) {
            this.index = index;
            this.val = val;
            this.empty = false;
        }
    }
}

class SegmentPQ implements Cloneable {
    private SegmentPQ left;
    private SegmentPQ right;
    private int max;
    private int dirty = Integer.MIN_VALUE;
    private int size;

    private void modify(int x) {
        if (size == 0) {
            return;
        }
        max = Math.max(max, x);
        dirty = Math.max(dirty, x);
    }

    public void pushUp() {
        size = left.size + right.size;
        max = Math.max(left.max, right.max);
    }

    public void pushDown() {
        left.modify(dirty);
        right.modify(dirty);
        dirty = Integer.MIN_VALUE;
    }

    public SegmentPQ(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new SegmentPQ(l, m, func);
            right = new SegmentPQ(m + 1, r, func);
            pushUp();
        } else {
            size = 1;
            max = func.apply(l);
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }


    public void pop(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            max = Integer.MIN_VALUE;
            size = 0;
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.pop(ll, rr, l, m);
        right.pop(ll, rr, m + 1, r);
        pushUp();
    }

    public void peek(int ll, int rr, int l, int r, PeekResult result) {
        if (noIntersection(ll, rr, l, r) || size == 0) {
            return;
        }
        if (l == r) {
            result.update(l, max);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        if (left.max > right.max) {
            left.peek(ll, rr, l, m, result);
        } else {
            right.peek(ll, rr, m + 1, r, result);
        }
        pushUp();
    }

    private SegmentPQ deepClone() {
        SegmentPQ seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected SegmentPQ clone() {
        try {
            return (SegmentPQ) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    private void toString(StringBuilder builder) {
        if (left == null && right == null) {
            builder.append(max).append(",");
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

class QueryResult {
    long sum;
    int size;

    public void reset() {
        sum = 0;
        size = 0;
    }

    public void update(long sum, int size) {
        this.sum += sum;
        this.size += size;
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static long inf = (long) 1e18;
    private long sum;
    private long min = inf;
    private long secMin = inf;
    private int minSize = 0;
    private int size = 0;

    private void active(long x) {
        sum = min = x;
        minSize = 1;
        size = 1;
    }

    private void modify(long x) {
        if (size == 0 || min >= x) {
            return;
        }
        sum += (x - min) * minSize;
        min = x;
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
        secMin = Math.max(min == left.min ? left.secMin : left.min,
                min == right.min ? right.secMin : right.min);
        minSize = (min == left.min ? left.minSize : 0) + (min == right.min ? right.minSize : 0);
        sum = left.sum + right.sum;
        size = left.size + right.size;
    }

    public void pushDown() {
        left.modify(min);
        right.modify(min);
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
        }
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r) || min >= x) {
            return;
        }
        if (covered(ll, rr, l, r) && secMin > x) {
            modify(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void active(int ll, int rr, int l, int r, long x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            active(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.active(ll, rr, l, m, x);
        right.active(ll, rr, m + 1, r, x);
        pushUp();
    }

    public void query(int ll, int rr, int l, int r, QueryResult result) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            result.update(sum, size);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.query(ll, rr, l, m, result);
        right.query(ll, rr, m + 1, r, result);
    }

    private Segment deepClone() {
        Segment seg = clone();
        if (seg.left != null) {
            seg.left = seg.left.deepClone();
        }
        if (seg.right != null) {
            seg.right = seg.right.deepClone();
        }
        return seg;
    }

    @Override
    protected Segment clone() {
        try {
            return (Segment) super.clone();
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
        pushUp();
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
