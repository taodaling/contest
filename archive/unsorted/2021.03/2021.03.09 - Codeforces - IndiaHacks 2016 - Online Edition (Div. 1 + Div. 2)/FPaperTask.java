package contest;

import template.datastructure.PersistentArray;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.string.IntFunctionIntSequenceAdapter;
import template.string.SubstringCompare;

public class FPaperTask {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        char[] s = new char[n];
        in.rs(s);
        int[] right = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            if (i + 1 >= n || s[i] == ')') {
                right[i] = n;
                continue;
            }
            right[i] = i + 1;
            while (right[i] < n && s[right[i]] != ')') {
                right[i] = right[right[i]] + 1;
            }
        }
        NoTagPersistentSegment[] sts = new NoTagPersistentSegment[n + 1];
        sts[n] = NoTagPersistentSegment.NIL;
        for (int i = n - 1; i >= 0; i--) {
            if (right[i] >= n) {
                sts[i] = NoTagPersistentSegment.NIL;
                continue;
            }
            sts[i] = sts[right[i] + 1].clone();
            sts[i].update(right[i], right[i], 0, n - 1, 1);
        }
        SubstringCompare sc = new SubstringCompare(new IntFunctionIntSequenceAdapter(i -> s[i], 0, n - 1));
        int[] lcp = sc.lcp();
        int[] rank = sc.rank();
        long ans = 0;
        for (int i = 0; i < n; i++) {
            int l = i + 1;
            int r = n - 1;
            if (rank[i] < n - 1) {
                l += lcp[rank[i]];
            }
            ans += sts[i].query(l, r, 0, n - 1);
        }
        out.println(ans);
    }
}

class NoTagPersistentSegment implements Cloneable {
    public static final NoTagPersistentSegment NIL = new NoTagPersistentSegment();

    static {
        NIL.left = NIL.right = NIL;
    }

    private NoTagPersistentSegment left;
    private NoTagPersistentSegment right;
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
