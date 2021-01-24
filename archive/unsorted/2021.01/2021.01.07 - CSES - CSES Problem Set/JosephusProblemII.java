package contest;

import template.datastructure.Treap;
import template.io.FastInput;
import template.io.FastOutput;

public class JosephusProblemII {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        Treap root = Treap.NIL;
        for (int i = 1; i <= n; i++) {
            Treap t = new Treap();
            t.key = i;
            t.pushUp();
            root = Treap.merge(root, t);
        }

        while (root.size > 0) {
            int x = k % root.size + 1;
            Treap[] res1 = Treap.splitByRank(root, x);
            Treap[] res2 = Treap.splitByRank(res1[0], x - 1);
            out.println(res2[1].key);
            root = Treap.merge(res1[1], res2[0]);
        }
    }
}
