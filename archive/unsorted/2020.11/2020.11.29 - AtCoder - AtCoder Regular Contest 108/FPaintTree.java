package contest;

import template.binary.Bits;
import template.datastructure.SegTree;
import template.graph.Graph;
import template.graph.UndirectedEdge;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.Power;
import template.utils.CloneSupportObject;
import template.utils.Sum;
import template.utils.Update;

import java.util.ArrayList;
import java.util.List;

public class FPaintTree {
    public static void main(String[] args) {
        int ans = 0;
        for (int i = 0; i < 1 << 4; i++) {
            if (Bits.get(i, 0) != 0 || Bits.get(i, 3) != 1) {
                continue;
            }
            List<Integer> zero = new ArrayList<>();
            List<Integer> one = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                if (Bits.get(i, j) == 0) {
                    zero.add(j);
                } else {
                    one.add(j);
                }
            }
            long len = 0;
            if (zero.size() > 1) {
                len = Math.max(len, zero.get(zero.size() - 1) - zero.get(0));
            }
            if (one.size() > 1) {
                len = Math.max(len, one.get(one.size() - 1) - one.get(0));
            }
            ans += len;
        }
        System.out.println(ans);
    }

    int mod = (int) 1e9 + 7;
    Power pow = new Power(mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        g = Graph.createGraph(n);
        for (int i = 0; i < n - 1; i++) {
            Graph.addUndirectedEdge(g, in.ri() - 1, in.ri() - 1);
        }

        int[] depth = new int[n];
        dfs(0, -1, depth);
        int end1 = findFarthest(depth);
        dfs(end1, -1, depth);
        int end2 = findFarthest(depth);
        int diameter = depth[end2];


        int[] a = new int[n];
        int[] b = new int[n];
        dfs(end1, -1, a);
        dfs(end2, -1, b);

        SumImpl s = new SumImpl();
        UpdateImpl u = new UpdateImpl();

        SegTree<SumImpl, UpdateImpl> st = new SegTree<>(0, diameter, SumImpl::new, UpdateImpl::new,
                i -> {
                    SumImpl ans = new SumImpl();
                    ans.s = i == 0 ? 1 : 0;
                    ans.size = 1;
                    return ans;
                });


        for (int i = 0; i < n; i++) {
            if (i == end1 || i == end2) {
                continue;
            }
            int x = a[i];
            int y = b[i];
            if (x > y) {
                int tmp = x;
                x = y;
                y = tmp;
            }
            s.clear();
            st.query(0, x, 0, diameter, s);
            long sx = s.s;
            s.clear();
            st.query(0, y, 0, diameter, s);
            long sy = s.s;

            u.a = 0;
            u.b = 0;
            st.update(0, x, 0, diameter, u);
            u.a = 0;
            u.b = sx;
            st.update(x, x, 0, diameter, u);
            u.a = 1;
            u.b = sy;
            st.update(y, y, 0, diameter, u);
            u.a = 2;
            u.b = 0;
            st.update(y + 1, diameter, 0, diameter, u);
        }

        long ans = 0;
        for (int i = 0; i <= diameter; i++) {
            s.clear();
            st.query(i, i, 0, diameter, s);
            long way = s.s;
            ans += way * i % mod;
        }
        ans += (long)pow.pow(2, n - 2) * diameter % mod;
        ans = ans * 2 % mod;
        out.println(ans);
    }

    public int findFarthest(int[] d) {
        int ans = 0;
        for (int i = 1; i < d.length; i++) {
            if (d[ans] < d[i]) {
                ans = i;
            }
        }
        return ans;
    }

    List<UndirectedEdge>[] g;

    public void dfs(int root, int p, int[] d) {
        d[root] = p == -1 ? 0 : d[p] + 1;
        for (UndirectedEdge e : g[root]) {
            if (e.to == p) {
                continue;
            }
            dfs(e.to, root, d);
        }
    }
}


class UpdateImpl extends CloneSupportObject<UpdateImpl> implements Update<UpdateImpl> {
    static long mod = (long) 1e9 + 7;
    long a;
    long b;

    @Override
    public void update(UpdateImpl update) {
        a = a * update.a % mod;
        b = (b * update.a + update.b) % mod;
    }

    @Override
    public void clear() {
        a = 1;
        b = 0;
    }

    @Override
    public boolean ofBoolean() {
        return !(a == 1 && b == 0);
    }
}

class SumImpl implements Sum<SumImpl, UpdateImpl> {
    static long mod = (long) 1e9 + 7;
    long s;
    long size;

    @Override
    public void add(SumImpl sum) {
        s = DigitUtils.modplus(s, sum.s, mod);
        size += sum.size;
    }

    @Override
    public void update(UpdateImpl update) {
        s = (s * update.a + size * update.b) % mod;
    }

    @Override
    public void copy(SumImpl sum) {
        s = sum.s;
        size = sum.size;
    }

    @Override
    public SumImpl clone() {
        SumImpl ans = new SumImpl();
        ans.copy(this);
        return ans;
    }

    public void clear() {
        size = 0;
        s = 0;
    }
}