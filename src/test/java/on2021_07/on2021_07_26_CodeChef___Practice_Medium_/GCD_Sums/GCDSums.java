package on2021_07.on2021_07_26_CodeChef___Practice_Medium_.GCD_Sums;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.GCDs;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class GCDSums {
    int mod = (int) 1e9 + 7;
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[] a = in.ri(n);

        IntegerArrayList[] prev = new IntegerArrayList[n];
        for (int i = 0; i < n; i++) {
            prev[i] = new IntegerArrayList(30);
            int g = a[i];
            for (int j = i - 1; j >= 0; j--) {
                int next;
                if ((next = GCDs.gcd(g, a[j])) < g) {
                    prev[i].add(j);
                    g = next;
                } else {
                    for (int k = prev[j].size() - 1; k >= 0; k--) {
                        int cand = prev[j].get(k);
                        if ((next = GCDs.gcd(g, a[cand])) < g) {
                            g = next;
                            prev[i].add(cand);
                        }
                    }
                    break;
                }
            }
            prev[i].reverse();
//            debug.debug("i", i);
//            debug.debug("prev[i]", prev[i]);
        }
        debug.elapse("gcd");
        PersistentSegment.L = 0;
        PersistentSegment.R = n - 1;
        PersistentSegment[] sts = new PersistentSegment[n + 1];
        sts[0] = PersistentSegment.NIL;
        for (int i = 0; i < n; i++) {
            PersistentSegment st = sts[i].clone();
            sts[i + 1] = st;
            int last = i;
            int g = a[i];
            for (int j = prev[i].size() - 1; j >= 0; j--) {
                int index = prev[i].get(j);
                st.update(index + 1, last, 0, n - 1, g);
                g = GCDs.gcd(g, a[index]);
                last = index;
            }
            st.update(0, last, 0, n - 1, g);
//            debug.debug("i", i);
//            debug.debug("st", st);
        }
        debug.elapse("st");

        boolean encrypt = false;
        int last = 0;
        for (int i = 0; i < q; i++) {
            int l = in.ri();
            int r = in.ri();
            if (encrypt) {
                l = (l + last) % n + 1;
                r = (r + last) % n + 1;
            }
            l--;
            r--;

            long ans = sts[r + 1].query(l, r, 0, n - 1, 0);
            last = DigitUtils.mod(ans, mod);
            out.println(last);
        }
        debug.elapse("query");

        return;
    }
}


class PersistentSegment implements Cloneable {
    static final PersistentSegment NIL = new PersistentSegment();
    private PersistentSegment left;
    private PersistentSegment right;
    private static int mod = (int) 1e9 + 7;
    int add;
    int sum;

    public void pushUp() {
        sum = left.sum + right.sum;
        if (sum >= mod) {
            sum -= mod;
        }
    }

    public void pushDown(int l, int r) {
        left = left.clone();
        right = right.clone();

        if (add != 0) {
            int m = DigitUtils.floorAverage(l, r);
            left.modify(add, m - l + 1);
            right.modify(add, r - m);
            add = 0;
        }
    }

    public void modify(int x, int size) {
        add += x;
        if (add >= mod) {
            add -= mod;
        }
        sum = (int) ((sum + (long) size * x) % mod);
    }

    public PersistentSegment() {
        left = right = this;
    }

    private boolean covered(int ll, int rr, int l, int r) {
        return ll <= l && rr >= r;
    }

    private boolean noIntersection(int ll, int rr, int l, int r) {
        return ll > r || rr < l;
    }

    public void update(int ll, int rr, int l, int r, int x) {
        if (noIntersection(ll, rr, l, r)) {
            return;
        }
        if (covered(ll, rr, l, r)) {
            modify(x, r - l + 1);
            return;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        left.update(ll, rr, l, m, x);
        right.update(ll, rr, m + 1, r, x);
        pushUp();
    }

    public long query(int ll, int rr, int l, int r, int tag) {
        if (noIntersection(ll, rr, l, r)) {
            return 0;
        }
        if (covered(ll, rr, l, r)) {
            return sum + (long)(r - l + 1) * tag % mod;
        }
        tag += add;
        if(tag >= mod){
            tag -= mod;
        }
//        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        long ans = 0;
        ans += left.query(ll, rr, l, m, tag);
        ans += right.query(ll, rr, m + 1, r, tag);
        return ans;
    }

    @Override
    public PersistentSegment clone() {
        try {
            return (PersistentSegment) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


    private void toString(StringBuilder builder, int l, int r) {
        if (l == r) {
            builder.append(sum).append(",");
            return;
        }
        pushDown(l, r);
        int m = DigitUtils.floorAverage(l, r);
        left.toString(builder, l, m);
        right.toString(builder, m + 1, r);
    }


    static int L, R;
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        clone().toString(builder, L, R);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }
}
