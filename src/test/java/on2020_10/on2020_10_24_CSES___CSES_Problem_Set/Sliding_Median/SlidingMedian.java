package on2020_10.on2020_10_24_CSES___CSES_Problem_Set.Sliding_Median;



import template.datastructure.Treap;
import template.io.FastInput;
import template.math.DigitUtils;

import java.io.PrintWriter;

public class SlidingMedian {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int k = in.readInt();
        int kth = DigitUtils.ceilDiv(k, 2);
        int[] x = new int[n];
        in.populate(x);
        Treap t = Treap.NIL;
        for (int i = 0; i < n; i++) {
            Treap[] p1 = Treap.splitByKey(t, x[i]);
            Treap newNode = new Treap();
            newNode.key = x[i];
            p1[0] = Treap.merge(p1[0], newNode);
            t = Treap.merge(p1[0], p1[1]);

            if (i >= k - 1) {
                int ans = Treap.getKeyByRank(t, kth);
                out.println(ans);

                Treap[] p2 = Treap.splitByKey(t, x[i - k + 1]);
                p2[0] = Treap.splitByRank(p2[0], p2[0].size - 1)[0];
                t = Treap.merge(p2[0], p2[1]);
            }
        }
    }
}
