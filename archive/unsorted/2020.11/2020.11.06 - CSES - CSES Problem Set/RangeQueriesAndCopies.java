package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.ArrayList;
import java.util.List;

public class RangeQueriesAndCopies {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] data = new int[n];
        in.populate(data);
        List<NoTagPersistentSegment> seg = new ArrayList<>(m + 1);
        seg.add(NoTagPersistentSegment.NIL);
        NoTagPersistentSegment init = NoTagPersistentSegment.NIL.clone();
        for (int i = 0; i < n; i++) {
            init.update(i, i, 0, n - 1, data[i]);
        }
        seg.add(init);
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                int k = in.readInt();
                int a = in.readInt() - 1;
                int x = in.readInt();
                seg.get(k).update(a, a, 0, n - 1, x);
            } else if (t == 2) {
                int k = in.readInt();
                int l = in.readInt() - 1;
                int r = in.readInt() - 1;
                long ans = seg.get(k).query(l, r, 0, n - 1);
                out.println(ans);
            } else {
                int k = in.readInt();
                seg.add(seg.get(k).clone());
            }
        }
    }
}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    private NoTagPersistentSegment left;
    private NoTagPersistentSegment right;
    long sum;

    public void pushUp() {
        sum = left.sum + right.sum;
    }

    public NoTagPersistentSegment() {
        left = right = NIL;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (covered(ll, rr, l, r)) {
            sum = x;
            return;
        }
        int m = DigitUtils.floorAverage(l, r);
        if (!noIntersection(ll, rr, l, m)) {
            left = left.clone();
            left.update(ll, rr, l, m, x);
        }
        if (!noIntersection(ll, rr, m + 1, r)) {
            right = right.clone();
            right.update(ll, rr, m + 1, r, x);
        }
        pushUp();
    }

    public long query(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum;
        }
        int m = DigitUtils.floorAverage(l, r);
        return left.query(ll, rr, l, m) +
                right.query(ll, rr, m + 1, r);
    }

    @Override
    public NoTagPersistentSegment clone() {
        try {
            return (NoTagPersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
