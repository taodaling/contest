package on2021_03.on2021_03_16_Codeforces___Codeforces_Round__FF__Div__1_.C__DZY_Loves_Fibonacci_Numbers;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

public class CDZYLovesFibonacciNumbers {
    int mod = (int) 1e9 + 9;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] a = in.ri(n);
        Segment st = new Segment(0, n - 1, i -> a[i]);
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            if (t == 1) {
                long[][] vec = new long[][]{{1}, {1}};
                Modify mod = new Modify();
                mod.data = vec;
                st.update(l, r, 0, n - 1, mod);
            } else {
                long sum = st.query(l, r, 0, n - 1) % mod;
                out.println(sum);
            }
        }
    }
}

class Modify {
    long[][] data;
}

class Segment implements Cloneable {
    private static long mod = (int) 1e9 + 9;
    private Segment left;
    private Segment right;
    private long[][] sumVec;
    private long[][] move;
    private long[][] vec;
    private long sum;

    private static long[][] mul(long[][] a, long[][] b) {
        int n = a.length;
        int m = b[0].length;
        int mid = a[0].length;
        assert a[0].length == b.length;
        long[][] ans = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < mid; k++) {
                    ans[i][j] += a[i][k] * b[k][j];
                }
                ans[i][j] %= mod;
            }
        }
        return ans;
    }

    private static void mulAndStore(long[][] a, long[][] b) {
        long x = b[0][0];
        long y = b[1][0];
        b[0][0] = (a[0][0] * x + a[0][1] * y) % mod;
        b[1][0] = (a[1][0] * x + a[1][1] * y) % mod;
    }

    private static long[][] sum(long[][] a, long[][] b) {
        int n = a.length;
        int m = a[0].length;
        long[][] ans = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = a[i][j] + b[i][j];
                if (ans[i][j] >= mod) {
                    ans[i][j] -= mod;
                }
            }
        }
        return ans;
    }

    private static long mulVec(long[][] a, long[][] b) {
        assert a.length == 1 && b[0].length == 1;
        long ans = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        ans %= mod;
        return ans;
    }

    private void modify(long[][] dirty) {
        sum += mulVec(sumVec, dirty);
        if (sum >= mod) {
            sum -= mod;
        }
        for (int i = 0; i < 2; i++) {
            vec[i][0] += dirty[i][0];
            if (vec[i][0] >= mod) {
                vec[i][0] -= mod;
            }
        }
    }

    public void pushUp() {
        sum = left.sum + right.sum;
        if (sum >= mod) {
            sum -= mod;
        }
    }

    public void firstPushUp() {
        move = mul(right.move, left.move);
        sumVec = sum(left.sumVec, mul(right.sumVec, left.move));
        vec = new long[][]{{0}, {0}};
    }

    public void pushDown() {
        if (vec[0][0] != 0 || vec[1][0] != 0) {
            //pushdown
            left.modify(vec);
            mulAndStore(left.move, vec);
            right.modify(vec);
            vec[0][0] = vec[1][0] = 0;
        }
    }

    public Segment(int l, int r, IntToIntegerFunction func) {
        if (l < r) {
            int m = DigitUtils.floorAverage(l, r);
            left = new Segment(l, m, func);
            right = new Segment(m + 1, r, func);
            firstPushUp();
            pushUp();
        } else {
            sumVec = new long[][]{{1, 0}};
            vec = new long[][]{{0}, {0}};
            move = new long[][]{{0, 1}, {1, 1}};
            sum = func.apply(l);
        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r, Modify modify) {
        if (leave(L, R, l, r)) {
            return;
        }
        if (enter(L, R, l, r)) {
            modify(modify.data);
            mulAndStore(move, modify.data);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        left.update(L, R, l, m, modify);
        right.update(L, R, m + 1, r, modify);
        pushUp();
    }

    public long query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return 0;
        }
        if (enter(L, R, l, r)) {
            return sum;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.query(L, R, l, m) +
                right.query(L, R, m + 1, r);
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

