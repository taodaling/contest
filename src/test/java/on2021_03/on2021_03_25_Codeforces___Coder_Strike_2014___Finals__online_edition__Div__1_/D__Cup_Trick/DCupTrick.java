package on2021_03.on2021_03_25_Codeforces___Coder_Strike_2014___Finals__online_edition__Div__1_.D__Cup_Trick;



import template.datastructure.Treap;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;

import java.util.function.IntFunction;

public class DCupTrick {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        Treap[] nodes = new Treap[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Treap();
            nodes[i].key = -1;
        }
        Treap root = Treap.build(i -> nodes[i], 0, n - 1);
        for (int i = 0; i < m; i++) {
            int x = in.ri() - 1;
            int y = in.ri();
            Treap[] pair = Treap.splitByRank(root, y - 1);
            Treap[] pair2 = Treap.splitByRank(pair[1], 1);
            if (pair2[0].key == -1) {
                pair2[0].key = x;
            }
            if (pair2[0].key != x) {
                out.println(-1);
                return;
            }
            root = Treap.merge(pair2[0], pair[0]);
            root = Treap.merge(root, pair2[1]);
        }
        int[] used = new int[n];
        for (Treap node : nodes) {
            if (node.key == -1) {
                continue;
            }
            used[node.key]++;
            if (used[node.key] > 1) {
                out.println(-1);
                return;
            }
        }
        int scan = 0;
        for (Treap node : nodes) {
            if (node.key != -1) {
                continue;
            }
            while (used[scan] != 0) {
                scan++;
            }
            node.key = scan;
            used[scan]++;
        }
        for (Treap node : nodes) {
            out.append(node.key + 1).append(' ');
        }
    }
}
