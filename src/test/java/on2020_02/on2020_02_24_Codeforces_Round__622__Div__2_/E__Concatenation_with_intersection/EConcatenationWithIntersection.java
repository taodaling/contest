package on2020_02.on2020_02_24_Codeforces_Round__622__Div__2_.E__Concatenation_with_intersection;



import template.io.FastInput;
import template.io.FastOutput;
import template.string.ZAlgorithm;

public class EConcatenationWithIntersection {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[] a = in.readString().toCharArray();
        char[] b = in.readString().toCharArray();
        char[] s = in.readString().toCharArray();

        int[] pre = new int[n];
        int[] suf = new int[n];

        ZAlgorithm pZ = new ZAlgorithm(m + n + 1, i -> {
            if (i < m) {
                return s[i];
            }
            if (i == m) {
                return '#';
            }
            return a[i - m - 1];
        });
        for (int i = 0; i < n; i++) {
            pre[i] = pZ.applyAsInt(i + m + 1);
        }

        ZAlgorithm sZ = new ZAlgorithm(m + n + 1, i -> {
            if (i < m) {
                return s[m - 1 - i];
            }
            if (i == m) {
                return '#';
            }
            return b[n - (i - m)];
        });
        for (int i = 0; i < n; i++) {
            suf[i] = sZ.applyAsInt((n - 1 - i) + m + 1);
        }

        Segment segtree = new Segment(0, m);
        int l = 0;
        int r = -1;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            //limit - i + 1 < m => limit < m + i - 1
            int limit = Math.min(n - 1, m + i - 2);
            while (r < limit) {
                r++;
                segtree.update(m - suf[r], m - 1, 0, m, 1);
            }
            while (l < i) {
                segtree.update(m - suf[l], m - 1, 0, m, -1);
                l++;
            }
            if(pre[i] == 0){
                continue;
            }
            sum += segtree.query(1, pre[i], 0, m);
        }
        out.println(sum);
    }
}

class Segment implements Cloneable {
    private Segment left;
    private Segment right;
    private long sum;
    private long dirty;
    private int size;

    public void pushUp() {
        size = left.size + right.size;
        sum = left.sum + right.sum;
    }

    private void modify(long x) {
        sum += x * size;
        dirty += x;
    }

    public void pushDown() {
        if (dirty != 0) {
            left.modify(dirty);
            right.modify(dirty);
            dirty = 0;
        }
    }

    public Segment(int l, int r) {
        if (l < r) {
            int m = (l + r) >> 1;
            left = new Segment(l, m);
            right = new Segment(m + 1, r);
            pushUp();
        } else {
            size = 1;
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
            modify(x);
            return;
        }
        pushDown();
        int m = (l + r) >> 1;
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        pushDown();
        int m = (l + r) >> 1;
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
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
