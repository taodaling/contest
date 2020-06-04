package on2020_05.on2020_05_14_Codeforces___Codeforces_Round__429__Div__1_.D__Destiny0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntToIntegerFunction;

import java.util.Arrays;

public class DDestiny {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int q = in.readInt();

        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[i] = in.readInt();
        }

        NoTagPersistentSegment[] segs = new NoTagPersistentSegment[n + 1];
        segs[0] = NoTagPersistentSegment.NIL;
        for (int i = 1; i <= n; i++) {
            segs[i] = segs[i - 1].clone();
            segs[i].update(a[i], a[i], 1, n, 1);
        }

        for (int i = 0; i < q; i++) {
            int l = in.readInt();
            int r = in.readInt();
            int k = in.readInt();
            int atLeast = (int) DigitUtils.minimumIntegerGreaterThanDiv(r - l + 1, k);
            int ans = query(1, n, segs[l - 1], segs[r], atLeast);
            out.println(ans);
        }
    }


    public int query(int l, int r, NoTagPersistentSegment sub, NoTagPersistentSegment add, int atLeast) {
        if (add.cnt - sub.cnt < atLeast) {
            return -1;
        }
        if (l == r) {
            return l;
        }
        int m = DigitUtils.floorAverage(l, r);
        int ans = query(l, m, sub.left, add.left, atLeast);
        if (ans == -1) {
            ans = query(m + 1, r, sub.right, add.right, atLeast);
        }
        return ans;
    }
}


class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    NoTagPersistentSegment left;
    NoTagPersistentSegment right;
    int cnt;

    public void pushUp() {
        cnt = left.cnt + right.cnt;
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
            cnt += x;
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

    public int query(int ll, int rr, int l, int r) {
        if (this == NIL || noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return cnt;
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

