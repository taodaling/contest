package on2019_12.on2019_12_04_Educational_Codeforces_Round_66__Rated_for_Div__2_.G___Yet_Another_Partiton_Problem;



import template.datastructure.IntDequeBeta;
import template.datastructure.LiChaoSegment;
import template.geometry.ConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.SequenceUtils;

public class TaskG {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        int[][] dp = new int[k + 1][1 + n];
        SequenceUtils.deepFill(dp, (int) 1e9);
        dp[0][0] = 0;
        int[] leftUntil = new int[n + 1];
        IntDequeBeta deque = new IntDequeBeta(n);
        for (int i = 1; i <= n; i++) {
            while (!deque.isEmpty() && a[deque.peekLast()] < a[i]) {
                deque.removeLast();
            }
            if (deque.isEmpty()) {
                leftUntil[i] = 0;
            } else {
                leftUntil[i] = deque.peekLast();
            }
            deque.addLast(i);
        }

        Segment seg = new Segment(0, n);
        for (int i = 1; i <= k; i++) {
            ConvexHullTrick cht = new ConvexHullTrick();
            seg.clear();
            seg.update(0, 0, 0, n, 0, -dp[i - 1][0]);
            for (int j = 1; j <= n; j++) {
                double maxB = seg.query(leftUntil[i], j - 1, 0, n, a[j]);
                cht.insert(a[j], maxB);
                double maxDp = cht.query(-j);
                dp[i][j] = (int) DigitUtils.round(-maxDp);
                seg.update(j, j, 0, n, i, -dp[i - 1][j]);
            }
        }

        int ans = dp[k][n];
        out.println(ans);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private ConvexHullTrick cht = new ConvexHullTrick();

    public void pushUp() {
    }

    public void pushDown() {
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

    public void update(int ll, int rr, int l, int r, double a, double b) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            cht.insert(a, b);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, a, b);
        right.update(ll, rr, m + 1, r, a, b);
        pushUp();
        cht.insert(a, b);
    }

    public double query(int ll, int rr, int l, int r, double x) {
        if (noIntersection(ll, rr, l, r)) {
            return -1e50;
        }
        if (covered(ll, rr, l, r)) {
            return cht.query(x);
        }
        pushDown();
        int m = (l + r) >> 1;
        return Math.max(left.query(ll, rr, l, m, x),
                right.query(ll, rr, m + 1, r, x));
    }

    public void clear() {
        cht.clear();
        if (left != null) {
            left.clear();
            right.clear();
        }
    }
}
