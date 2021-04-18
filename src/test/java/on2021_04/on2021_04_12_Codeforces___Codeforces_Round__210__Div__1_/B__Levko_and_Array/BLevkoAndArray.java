package on2021_04.on2021_04_12_Codeforces___Codeforces_Round__210__Div__1_.B__Levko_and_Array;



import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.LongArrayList;

import java.util.Arrays;
import java.util.function.IntPredicate;

public class BLevkoAndArray {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] a = in.ri(n);
        dp = new int[n];
        IntPredicate predicate = mid -> check(a, mid) >= n - k;
        int ans = BinarySearch.firstTrue(predicate, 0, (int) 2e9);
        out.println(ans);
    }

    int[] dp;

    public int check(int[] a, int x) {
        Arrays.fill(dp, 0);
        for (int i = a.length - 1; i >= 0; i--) {
            dp[i] = 1;
            for (int j = i + 1; j < a.length; j++) {
                int step = j - i;
                if (DigitUtils.ceilDiv(Math.abs(a[i] - a[j]), step) <= x) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return Arrays.stream(dp).max().orElse(-1);
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static long inf = (long) 1e18;

    private long min;

    private void modify(long x) {
        min = Math.min(min, x);
    }

    public void pushUp() {
        min = Math.min(left.min, right.min);
    }

    public void pushDown() {
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            min = inf;
        }
    }

    public void init(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left.init(l, m);
            right.init(m + 1, r);
            pushUp();
        } else {
            min = inf;
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r, long x) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m, x);
        right.update(L, R, m + 1, r, x);
        pushUp();
    }

    public long query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return inf;
        }
        if (enter(L, R, l, r)) {
            return min;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.query(L, R, l, m),
                right.query(L, R, m + 1, r));
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
            builder.append("val").append(",");
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