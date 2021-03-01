package contest;

import template.datastructure.RMQ;
import template.datastructure.RMQBySegment;
import template.datastructure.SegTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.utils.Debug;
import template.utils.Sum;
import template.utils.Update;

public class CDrazilAndPark {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] d = in.ri(n);
        int[] h = in.ri(n);
        long[] xs = new long[2 * n];
        long[] A = new long[2 * n];
        long[] B = new long[2 * n];
        for (int i = 1; i < 2 * n; i++) {
            xs[i] = xs[i - 1] + d[(i - 1) % n];
        }
        for (int i = 0; i < 2 * n; i++) {
            A[i] = 2L * h[i % n] + xs[i];
            B[i] = 2L * h[i % n] - xs[i];
        }

        long inf = (long) 1e18;
        SegTree<SumImpl, Update.NilUpdate> st = new SegTree<>(0, 2 * n - 1, SumImpl::new,
                Update.NilUpdate.SUPPLIER, i -> {
            SumImpl sum = new SumImpl();
            sum.a = A[i];
            sum.b = B[i];
            sum.ab = -inf;
            return sum;
        });

        SumImpl qry = new SumImpl();
        for (int i = 0; i < m; i++) {
            debug.debug("st", st);
            int l = in.ri() - 1;
            int r = in.ri() - 1;
            int L, R;
            if (l <= r) {
                L = r + 1;
                R = l + n - 1;
            } else {
                L = r + 1;
                R = l - 1;
            }
            assert L < R;
            qry.clear();
            st.query(L, R, 0, 2 * n - 1, qry);
            out.println(qry.ab);
        }
    }
}


class SumImpl implements Sum<SumImpl, Update.NilUpdate> {
    long a;
    long b;
    long ab;
    static long inf = (long) 1e18;

    void clear() {
        a = -inf;
        b = -inf;
        ab = -inf;
    }

    @Override
    public void add(SumImpl sum) {
        ab = Math.max(ab, sum.ab);
        ab = Math.max(b + sum.a, ab);
        a = Math.max(a, sum.a);
        b = Math.max(b, sum.b);
    }

    @Override
    public void update(Update.NilUpdate nilUpdate) {

    }

    @Override
    public void copy(SumImpl sum) {
        a = sum.a;
        b = sum.b;
        ab = sum.ab;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    @Override
    public String toString() {
        return "(" + a + "," + b + "," + ab + ")";
    }
}