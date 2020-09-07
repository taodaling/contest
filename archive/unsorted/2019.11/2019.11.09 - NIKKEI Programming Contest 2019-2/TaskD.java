package contest;

import template.FastInput;
import template.FastOutput;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[][] ops = new int[m][3];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 3; j++) {
                ops[i][j] = in.readInt();
            }
        }

        Map<Integer, List<int[]>> group = Arrays.stream(ops)
                .collect(Collectors.groupingBy(x -> x[0]));

        Segment seg = new Segment(1, n);
        seg.updatePlus(1, 1, 1, n, 0);
        for (int i = 1; i <= n; i++) {
            long dist = seg.queryMax(i, i, 1, n);
            for (int[] op : group.getOrDefault(i, Collections.emptyList())) {
                seg.updatePlus(op[0], op[1], 1, n, dist + op[2]);
            }
        }

        long ans = seg.queryMax(n, n, 1, n);
        if (ans >= (long) 1e17) {
            out.println(-1);
        } else {
            out.println(ans);
        }
    }
}


class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private static long inf = (long) 1e18;
    private long setMin = inf;

    private void setMin(long x) {
        setMin = Math.min(x, setMin);
    }

    public void pushUp() {
    }

    public void pushDown() {
        left.setMin(setMin);
        right.setMin(setMin);
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
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
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            setMin(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.updatePlus(ll, rr, l, m, x);
        right.updatePlus(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return inf;
        }
        if (covered(ll, rr, l, r)) {
            return setMin;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return Math.min(left.queryMax(ll, rr, l, m),
                right.queryMax(ll, rr, m + 1, r));
    }
}