package on2021_11.on2021_11_15_DMOJ___DMOPC__21_Contest_3.P4___Sorting_Subsequences0;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

public class P4SortingSubsequences {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        int[] p = new int[n];
        int[] v2p = new int[n];
        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = in.ri() - 1;
            v2p[p[i]] = i;
            size[i] = 1;
        }
        Segment st = new Segment(0, n - 1);
        for (int i = 0; i < n; i++) {
            debug.debug("i", i);
            debug.debugArray("p", p);
            debug.debug("st", st);
            int best = st.query(0, n - 1, k - size[i]);
            int bestPos = v2p[best];
            if (best < p[i] && size[bestPos] + size[i] <= k) {
                p[bestPos] = p[i];
                v2p[p[bestPos]] = bestPos;
                size[bestPos] += size[i];
                st.update(best, 0, n - 1, (int) 1e8);
                st.update(p[bestPos], 0, n - 1, size[bestPos]);

                p[i] = best;
                v2p[p[i]] = i;
                size[i] = (int) 1e8;
            } else {
                st.update(p[i], 0, n - 1, (int) 1e8);
                size[i] = (int) 1e8;
            }
        }
        debug.debug("i", n);
        debug.debugArray("p", p);
        debug.debug("st", st);
        for(int i = 0; i < n; i++){
            out.append(p[i] + 1);
            if(i + 1 < n){
                out.append(' ');
            }
        }
        out.println();
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    int size = 1;

    private void modify(int x) {
        size = x;
    }

    public void pushUp() {
        size = Math.min(left.size, right.size);
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

        }
    }

    private boolean enter(int L, int R, int l, int r) {
        return L <= l && R >= r;
    }

    private boolean leave(int L, int R, int l, int r) {
        return L > r || R < l;
    }

    public void update(int index, int l, int r, int x) {
        if (l == r) {
            modify(x);
            return;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (index <= m) {
            left.update(index, l, m, x);
        } else {
            right.update(index, m + 1, r, x);
        }
        pushUp();
    }

    public int query(int l, int r, int threshold) {
        if (l == r) {
            return l;
        }
        pushDown();
        int m = DigitUtils.floorAverage(l, r);
        if (left.size <= threshold) {
            return left.query(l, m, threshold);
        } else {
            return right.query(m + 1, r, threshold);
        }
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
            builder.append(size).append(",");
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