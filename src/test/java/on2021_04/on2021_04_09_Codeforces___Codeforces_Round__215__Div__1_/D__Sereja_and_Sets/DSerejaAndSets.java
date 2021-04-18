package on2021_04.on2021_04_09_Codeforces___Codeforces_Round__215__Div__1_.D__Sereja_and_Sets;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.polynomial.FastWalshHadamardTransform;
import template.utils.Debug;

public class DSerejaAndSets {
    Debug debug = new Debug(true);


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int d = in.ri();


        Segment st = new Segment(0, n - 1);
        for (int i = 0; i < m; i++) {
            int s = in.ri();
            for (int j = 0; j < s; j++) {
                int x = in.ri() - 1;
                int begin = Math.max(0, x - d + 1);
                st.update(begin, x, 0, n - 1, 1 << i);
            }
        }

        long[] cnt = new long[1 << m];
        for (int i = 0; i + d - 1 < n; i++) {
            cnt[st.query(i, i, 0, n - 1)]++;
        }
        FastWalshHadamardTransform.orFWT(cnt, 0, cnt.length - 1);
        int max = 0;
        for (int i = 0; i < 1 << m; i++) {
            if (cnt[i] == 0) {
                max = Math.max(max, Integer.bitCount(i));
            }
        }
debug.debug("st", st);
        out.println(m - max);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    int or;
    int dirty;

    private void modify(int x) {
        or |= x;
        dirty |= x;
    }

    public void pushUp() {
        or = left.or | right.or;
    }

    public void pushDown() {
        if(dirty != 0){
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
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

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int L, int R, int l, int r, int x) {
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

    public int query(int L, int R, int l, int r) {
        if (leave(L, R, l, r)) {
            return 0;
        }
        if (enter(L, R, l, r)) {
            return or;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        return left.query(L, R, l, m) |
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
            builder.append(or).append(",");
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