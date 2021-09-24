package on2021_07.on2021_07_27_Codeforces___Codeforces_Round__187__Div__1_.C__Sereja_and_Subsequences;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.utils.Debug;

public class CSerejaAndSubsequences {
    int mod = (int) 1e9 + 7;
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList list = new IntegerArrayList(a);
        list.add(1);
        list.unique();
        int m = list.size();
        NoTagPersistentSegment[] st = new NoTagPersistentSegment[n];
        IntegerHashMap map = new IntegerHashMap(n, false);
        int[] prev = new int[n];
        for (int i = 0; i < n; i++) {
            prev[i] = map.getOrDefault(a[i], -1);
            map.put(a[i], i);
        }
        NoTagPersistentSegment last = NoTagPersistentSegment.NIL;
        for (int i = 0; i < n; i++) {
            int rank = list.binarySearch(a[i]);
            st[i] = last.clone();
            NoTagPersistentSegment sub = prev[i] - 1 < 0 ? NoTagPersistentSegment.NIL :
                    st[prev[i] - 1];
            long sum = st[i].query(0, rank, 0, m - 1)
                    - sub.query(0, rank, 0, m - 1);
            if (prev[i] == -1) {
                sum++;
            }
            long ans = sum % mod * a[i] % mod;
            debug.debug("i", i);
            debug.debug("ans", ans);
            st[i].update(rank, rank, 0, m - 1, ans);
            last = st[i];
        }
        long ans = st[n - 1].query(0, m - 1, 0, m - 1);
        ans = DigitUtils.mod(ans, mod);
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
    long sum;

    public void modify(long x) {
        sum += x;
    }

    public void pushUp() {
        if (this == NIL) {
            return;
        }
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

    public void update(int ll, int rr, int l, int r, long x) {
        if (covered(ll, rr, l, r)) {
            modify(x);
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
        long ans = 0;
        ans += left.query(ll, rr, l, m);
        ans += right.query(ll, rr, m + 1, r);
        return ans;
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
